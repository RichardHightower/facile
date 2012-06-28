package org.facile;

import java.io.File;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.facile.IO.FileObject;

import static org.facile.Facile.*;

public class ProcessIO {
	@SuppressWarnings("unused")
	private static final Logger log = log(ProcessIO.class);

	public static class ProcessInOut {
		
		private ProcessRunner runner;
		private ProcessOut out;
		
		private volatile boolean done = true;
		
		BlockingQueue<String> queueOut;
		BlockingQueue<String> queueErr;
		
		BlockingQueue<Boolean> wait;
		
		
		public ProcessInOut () {
			this.queueOut = new ArrayBlockingQueue<String>(100);
			this.queueErr = new ArrayBlockingQueue<String>(100);
			this.wait = new ArrayBlockingQueue<Boolean>(1);
		}

		public void run(final int timeout, final List<File> path, final boolean verbose, final String... args) {
			done = false;
			out = new ProcessOut();
			runner = new ProcessRunner(ProcessInOut.this, null, timeout, path, verbose, args);

			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					out.exit = runner.exec();
					out.stdout = runner.stdOut();
					out.stderr = runner.stdErr();
					out.commandLine = join(' ',runner.commandLine);
					done=true;									
				}
			});
			
			thread.start();
			
			try {
				this.wait.poll(3, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				handle(e);
			}
			
		}
		
		public boolean isDone() {
			return done;
		}
		
		public ProcessOut processOut() {
			return out;
		}

		
		public FileObject getStdOut() {
			return new IO.AbstractFile() {
				public String readLine() {
					try {
						return queueOut.poll(1000, TimeUnit.DAYS);
					} catch (InterruptedException e) {
						handle(e);
					}
					return "out";
				}
			};
		}

		public FileObject getStdErr() {
			return new IO.AbstractFile() {
				public String readLine() {
					try {
						return queueErr.poll(1000, TimeUnit.DAYS);
					} catch (InterruptedException e) {
						handle(e);
					}
					return "err";
				}
			};
		}
		
		public FileObject getStdIn() {
			return runner.toProcess;
		}
		
	}
	
	
	public static class ProcessOut {
		public int exit;
		public String stdout;
		public String stderr;
		public String commandLine;
		@Override
		public String toString() {
			return "ProcessOut [\nexit=" + exit + ", \nstdout=" + stdout
					+ ", \nstderr=" + stderr + ", \ncommandLine=" + commandLine
					+ "\n]";
		}
		
	}

	private static void handle(Exception ex) {
		throw new ProcessException(ex);
	}

	public static int exec(String... args) {
		ProcessRunner runner = new ProcessRunner(null, null, 0, null, false, args);
		return runner.exec();
	}

	public static int exec(int timeout, String... args) {
		ProcessRunner runner = new ProcessRunner(null, null, timeout, null, false, args);
		return runner.exec();
	}


	public static ProcessOut run(int timeout, String... args) {
		return run(timeout, null, args);
	}

	public static ProcessOut run(int timeout, List<File> path, String... args) {
		return run(timeout, path, false, args);
	}
	public static ProcessOut run(int timeout, List<File> path, boolean verbose, String... args) {
		

		ProcessOut out = new ProcessOut();
		ProcessRunner runner = new ProcessRunner(null, null, timeout, path, verbose, args);
		out.exit = runner.exec();
		out.stdout = runner.stdOut();
		out.stderr = runner.stdErr();
		out.commandLine = join(' ',runner.commandLine);
		return out;
	}
	
	public static ProcessInOut runAsync(int timeout, List<File> path, boolean verbose, String... args) {
		
		ProcessInOut process = new ProcessInOut();
		process.run(timeout, path, verbose, args);
		
		return process;
		
	}


	public static ProcessOut run(String... args) {
		return run(0, args);
	}
	

	@SuppressWarnings("serial")
	public static class ProcessException extends RuntimeException {

		public ProcessException() {
			super();
		}

		public ProcessException(String m, Throwable t) {
			super(m, t);
		}

		public ProcessException(String m) {
			super(m);
		}

		public ProcessException(Throwable t) {
			super(t);
		}
	}

	public static class ProcessRunner {
		List<String> commandLine;
		String password;
		List<File> path;
		
		ProcessIOThread fromProcessOutput;
		ProcessIOThread fromProcessError;
		int seconds = 0;
		boolean verbose;
		
		volatile FileObject toProcess;
		
		
		ProcessInOut inout ;


		public ProcessRunner(ProcessInOut inout, String password, int seconds, List<File> path, boolean verbose, String... cmdLine) {
			
			
			if (cmdLine.length==1) {
				cmdLine = split(cmdLine[0]);
			}

			
			this.inout = inout;
			this.commandLine = list(cmdLine);
			this.password = password;
			this.seconds = seconds;
			this.path = path;
			this.verbose = verbose;
			
			if (this.path == null) {
				this.path = Sys.path();
			}
		}
		
		public int exec() throws ProcessException {
			int exit = -666;

			String cmd = commandLine.get(0);
			File f = file(cmd);
			if (!f.exists()) {
				for (File dir : path) {
					File fcmd = file(dir, cmd);
					if (fcmd.exists()) {
						cmd = fcmd.getAbsolutePath();
						break;
					}
				}
			}
			

			
			commandLine.set(0, cmd);
			
			ProcessBuilder pb = new ProcessBuilder(commandLine);
			
			String spath = join(File.pathSeparatorChar, path);
			pb.environment().put("PATH", spath);
			
			try {
				final Process process = pb.start();

				toProcess = open(process.getOutputStream());
				
				FileObject stdOut = open(process.getInputStream());
				FileObject stdErr = open(process.getErrorStream());

				if (inout == null) {
					fromProcessError = new ProcessIOThread(stdErr, verbose);
					fromProcessOutput = new ProcessIOThread(stdOut, toProcess,
							password, false, verbose);
				} else {
					fromProcessError = new ProcessIOThread(inout.queueErr, stdErr, verbose);
					fromProcessOutput = new ProcessIOThread(inout.queueOut, stdOut, toProcess,
							password, false, verbose);
					
				}


				fromProcessOutput.start();
				fromProcessError.start();
				
				if (inout!=null) {
					inout.wait.put(true);
				}

				if (seconds == 0) {
					exit = process.waitFor();

					rest(10);
					fromProcessError.interrupt();
					fromProcessOutput.interrupt();
					rest(10);
					fromProcessError.join();
					fromProcessOutput.join();

				} else {
					// Timer thread to make sure process does not hang.
					Thread timer = new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								Thread.sleep(seconds * 1000);
							} catch (InterruptedException e) {
							}
							try {
								// Exit value will throw an exception if the
								// process is still running.
								process.exitValue();
							} catch (IllegalThreadStateException ex) {
								// Try to kill the process.
								process.destroy();
							}
						}
					});

					timer.start(); // Start the timer.
					exit = process.waitFor(); // Wait for the process... timer
												// will kill it if it takes too
												// long.
					timer.interrupt(); // tell timer to die.
					rest(10); // Give time for buffers to get drained.
					fromProcessError.interrupt(); // Send interrupt... time to
													// clean up.
					fromProcessOutput.interrupt();
					rest(10); // Give threads a chance to get signal.
					fromProcessError.join();
					fromProcessOutput.join();
				}
			} catch (Exception e) {
				handle(e);
			}
			return exit;
		}

		public String stdOut() {
			return fromProcessOutput.outputBuffer.toString();
		}

		public String stdErr() {
			return fromProcessError.outputBuffer.toString();
		}

	}

	static class ProcessIOThread extends Thread {
		FileObject fromProcess;
		String password;
		FileObject toProcess;
		StringBuilder outputBuffer = new StringBuilder(256);
		boolean sudo;
		boolean verbose;
		private BlockingQueue<String> queue;

		ProcessIOThread(FileObject fromProcess, boolean verbose) {
			this.fromProcess = fromProcess;
			this.verbose = verbose;
		}
		ProcessIOThread(BlockingQueue<String> queueOut, FileObject fromProcess, boolean verbose) {
			this.queue = queueOut;
			this.fromProcess = fromProcess;
			this.verbose = verbose;
		}

		ProcessIOThread(FileObject fromProcess,
				FileObject toProcess, String password, boolean sudo, boolean verbose) {
			this.sudo = sudo;
			this.fromProcess = fromProcess;
			this.toProcess = toProcess;
			this.verbose = verbose;
			this.toProcess.autoFlush();
			this.password = password;
		}

		public ProcessIOThread(BlockingQueue<String> queueOut,FileObject fromProcess,
				FileObject toProcess, String password, boolean sudo, boolean verbose) {
			this.queue = queueOut;
			this.sudo = sudo;
			this.fromProcess = fromProcess;
			this.toProcess = toProcess;
			this.verbose = verbose;
			this.toProcess.autoFlush();
			this.password = password;
		}

		public void run() {
			if (sudo) {
				rest(100);
				toProcess.println(password);
				toProcess.flush();
			}

			try {
				for (String line : fromProcess) {
					
					if(queue!=null) {
						while (true) {
							try {
								queue.put(line);
								break;
							} catch (InterruptedException e) {
								if (this.isInterrupted()) {
									break;
								} else {
									continue;
								}
							}
						}
					}
					
					fprintln(outputBuffer, line);
					if (verbose) print(line);
					if (this.isInterrupted()) {
						break;
					}
				}
			} finally {
				fromProcess.close();
			}
		}

		public StringBuilder getOutputBuffer() {
			return outputBuffer;
		}

	}

}
