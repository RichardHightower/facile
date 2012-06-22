package org.facile;

import java.util.List;
import java.util.logging.Logger;

import org.facile.IO.FileObject;

import static org.facile.Facile.*;

public class ProcessIO {
	@SuppressWarnings("unused")
	private static final Logger log = log(ProcessIO.class);

	public static class ProcessOut {
		int exit;
		String stdout;
		String stderr;
		String commandLine;
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
		ProcessRunner runner = new ProcessRunner(null, 0, args);
		return runner.exec();
	}

	public static int exec(int timeout, String... args) {
		ProcessRunner runner = new ProcessRunner(null, timeout, args);
		return runner.exec();
	}

	public static ProcessOut run(int timeout, String... args) {
		ProcessOut out = new ProcessOut();
		ProcessRunner runner = new ProcessRunner(null, timeout, args);
		out.exit = runner.exec();
		out.stdout = runner.stdOut();
		out.stderr = runner.stdErr();
		out.commandLine = join(' ',runner.commandLine);
		return out;
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
		ProcessIOThread fromProcessOutput;
		ProcessIOThread fromProcessError;
		int seconds = 0;

		public ProcessRunner(String password, int seconds, String... cmdLine) {
			this.commandLine = list(cmdLine);
			this.password = password;
			this.seconds = seconds;
		}

		public int exec() throws ProcessException {
			int exit = -666;

			ProcessBuilder pb = new ProcessBuilder(commandLine);
			try {
				final Process process = pb.start();

				FileObject<?> toProcess = open(process.getOutputStream());

				FileObject<String> stdOut = open(process.getInputStream());
				FileObject<String> stdErr = open(process.getErrorStream());

				fromProcessError = new ProcessIOThread(stdErr);
				fromProcessOutput = new ProcessIOThread(stdOut, toProcess,
						password, false);

				fromProcessOutput.start();
				fromProcessError.start();

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
		FileObject<String> fromProcess;
		String password;
		FileObject<?> toProcess;
		StringBuilder outputBuffer = new StringBuilder(256);
		boolean sudo = false;

		ProcessIOThread(FileObject<String> fromProcess) {
			this.fromProcess = fromProcess;
		}

		ProcessIOThread(FileObject<String> fromProcess,
				FileObject<?> toProcess, String password, boolean sudo) {
			this.sudo = sudo;
			this.fromProcess = fromProcess;
			this.toProcess = toProcess;

			// this.toProcess.autoFlush();
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
					fprintln(outputBuffer, line);
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
