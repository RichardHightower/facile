package com.examples;

import static com.examples.Results.*;
import static org.facile.ContextParser.*;
import static org.facile.ContextParser.Input.IN;
import static org.facile.Facile.*;
import java.io.File;
import java.util.List;
import org.facile.Facile.my;
import org.facile.IO.FileObject;
import org.facile.ProcessIO.ProcessInOut;
import org.facile.ProcessIO.ProcessOut;
import  org.facile.Sys;


public class AutoBench {
	
	// --clients xen.caucho.com:4600,lancre.caucho.com:4600 
	static String [] clients;
	//--uri1 /file_$5.html
	static String uri1;
	// --host1 ch_resin
	static String host1;
	// --port1 8080
	static int port1;
	//--uri2 /file_0k.html
	static String uri2;
	//--host2 ch_nginx
	static String host2;
	//--port2 80 
	static int port2;
	//--num_conn $1
	static int numConn;
	//--num_call 10
	static int numCall;
	//--low_rate $2
	static int lowRate;
	//--high_rate $3
	static int highRate;
	//--rate_step $4
	static int rateStep;
	//--timeout 3
	static int timeout;
	//--file out_con$1_start$2_end$3_step$4_$5.tsv
	static File file;
	static File homeDir;
	static boolean dryRun;
	static boolean verbose;
	static boolean echoCommandLine=true;
	
	static boolean slave=false;
	static int numSlave=2;
	static boolean master=false;
	
	static int processTimeout = 0;
	
	static List<File> path;
	
	static File outputFolder;
	static int slaveId;
	
	static boolean shortForm;

	public static void runIt() {
		
		if (slave) {
			print("Slave started ok " + slaveId);
			FileObject<String> reader = open(System.in);
			String line = reader.readLine();
			expect("", "ack", line);
		}

		
	
		if (homeDir==null) {
			homeDir = cwf();
			if (verbose) print ("Home directory was not passed setting to: ", homeDir);
		}
		
		if (verbose) print ("Command line arguments", cmdLine());
		
		outputFolder = file (homeDir, "/output");
		
		path = Sys.path();

		if (Sys.os().startsWith("Mac OS X")) {
			if (!isIn(file("/usr/bin"), path)) {
				path.add(file("/usr/bin"));
			}
			if (!isIn(file("/usr/local/bin/"), path)) {
				path.add(file("/usr/local/bin/"));
			}
		}
		
		
		@SuppressWarnings("unchecked")
		String runString1 = joinByString("_", array("run1", "host", host1, "port", port1, "uri", uri1, "numConn", numConn, "numCall", numCall, "rates", lowRate, highRate, rateStep));
		@SuppressWarnings("unchecked")
		String runString2 = joinByString("_", array("run2", "host", host2, "port", port2, "uri", uri2, "numConn", numConn, "numCall", numCall, "rates", lowRate, highRate, rateStep));
		
		runString1 = runString1.replace('.', '_').replace('/', '_').replace(' ', '_').replace(':', '_').replace(',', '_');
		runString2 = runString2.replace('.', '_').replace('/', '_').replace(' ', '_').replace(':', '_').replace(',', '_');
		
		//Build an output file dir that uniquely defines this run
		File out1Dir = file(outputFolder,runString1);
		File out2Dir = file(outputFolder,runString2);
		
		out1Dir.mkdirs();
		out2Dir.mkdirs();

		if (!slave) {
			outputHeader();
		}
		
		for (int rate=lowRate, index=0; rate <(highRate + rateStep); rate+=rateStep, index++){


			String httperf1 = sprint("httperf", "--server", host1, "--uri", uri1, "--num-con", numConn,
					"--num-call", numCall,"--timeout", timeout, "--rate", rate,
					"--port", port1);
			String httperf2 =	sprint("httperf", "--server", host2, "--uri", uri2, "--num-con", numConn,
						"--num-call", numCall,"--timeout", timeout, "--rate", rate,
						"--port", port2);
			
			
			

			if (dryRun) {
				print ("Dry run");
				print ("SERVER 1", mul(50, "*"));
				print(httperf1);
				print ("SERVER 2", mul(50, "*"));
				print(httperf2);
			} else {
				if (!slave) print ("Running benchmark run number ", index);
				String out1 = null;
				String out2 = null;
				
				if (master) {
					my myout1 = runSlavesServer(1, rate);
					my myout2 = runSlavesServer(2, rate);
					outputResult(rate, myout1, myout2);

				}
				
				if (!master) {
					out1 = runServer(1, httperf1, out1Dir, index);				
					out2 = runServer(2, httperf2, out2Dir, index);
				}
				if (!slave && !master) {
					outputResult(rate, parseOutput(out1), parseOutput(out2));
				}
			}
			
		}

		
	}
	

	private static my runSlavesServer(int i, int rate) {
		
		
		List<File> classPath = Sys.classPath();
		int numSlaves =2;
		
		String classpath = calculateClasspath(classPath);
		
		String args = join(' ',
				Sys.java().toString(), "-cp", classpath, AutoBench.class.getName(),	
				"--uri1", uri1, "--host1", host1, "--port1", port1, 
			 "--uri2", uri2, "--host2", host2, "--port2", port2, 
			 "--num_conn", numConn/numSlaves, "--num_call", numCall, 
			 "--low_rate", lowRate/numSlaves, 
			 "--high_rate", highRate/numSlaves, 
			 "--rate_step", rateStep/numSlaves, "--timeout", timeout, 
			 "--file",  file, "--slave",  "yes", "--verbose", "false", "--echo-command-line", "false");

		
		ProcessInOut inout1 = runAsync(0, path, true, args + " --slave-id 1");
		ProcessInOut inout2 = runAsync(0, path, true, args+ " --slave-id 2");
		
		FileObject<?> stdIn1 = inout1.getStdIn();
		FileObject<?> stdIn2 = inout2.getStdIn();
		
		String line = null;
		
		line = inout1.getStdOut().readLine();
		expect("", "Slave started ok 1", line);
		line = inout2.getStdOut().readLine();
		expect("", "Slave started ok 2", line);
		
		stdIn1.println("ack");
		stdIn2.println("ack");
		
		line = inout1.getStdOut().readLine();
		expect("", sprint("SERVER", 1, ":", mul(50, "*")), line);
		line = inout2.getStdOut().readLine();
		expect("", sprint("SERVER", 1, ":", mul(50, "*")), line);
		
		boolean found = false;
		
		while (!found) {
			line = inout1.getStdOut().readLine();
			if (line.startsWith("EXIT CODE FOR SERVER")) {
				found = true;
			}
		}

		found = false;
		while (!found) {
			line = inout2.getStdOut().readLine();
			if (line.startsWith("EXIT CODE FOR SERVER")) {
				found = true;
			}
		}
		
		stdIn1.println("ack");
		stdIn2.println("ack");

		StringBuilder buf = new StringBuilder(512);
		found = false;
		while (!found) {
			line = inout1.getStdOut().readLine();
			buf.append(line);
			if (line.startsWith("#### END SERVER")) {
				found = true;
			}
		}
		String out1 = buf.toString();
		
		buf = new StringBuilder(512);
		found = false;
		while (!found) {
			line = inout2.getStdOut().readLine();
			buf.append(line);
			if (line.startsWith("#### END SERVER")) {
				found = true;
			}
		}
		String out2 = buf.toString();

		my my1 = parseOutput(out1);
		my my2 = parseOutput(out2);
		
		try {
			int iReplies = get(integer, my1, replies);
			iReplies += get(integer, my1, replies);
			my2.i(replies, "" + iReplies);
			//TODO more here
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		
		stdIn1.println("ack");
		stdIn2.println("ack");

		return my1; //not done
	}


	private static String calculateClasspath(List<File> classPath) {
		File abPath = null;
		File facilePath = null;
		for (File p : classPath) {
			List<File> files = files(p, ".*com/examples/AutoBench.*");
			if (files.size() > 0) {
				abPath = p;
			}
			files = files(p, ".*org/facile/Facile.*");
			if (files.size() > 0) {
				facilePath = p;
			}
		}
		
		
		String classpath = null;
		if (abPath.equals(facilePath)) {
			classpath = abPath.toString();
		}else {
			classpath = join(':', abPath, facilePath);
		}
		return classpath;
	}


	public static String runServer(int serverNum, String cmdLine, File outDir, int runNum) {
		print ("SERVER", serverNum, ":", mul(50, "*"));
		if (echoCommandLine || verbose) print("RUNNING:", cmdLine);
		ProcessOut run = run(processTimeout * 60, path, verbose, cmdLine);
		print ("EXIT CODE for SERVER ", serverNum, ":",  run.exit);
		if (verbose) print(run.stdout);
		
		if (slave) {
			FileObject<String> reader = open(System.in);
			String line = reader.readLine();
			expect("", "ack", line);
			File outputFile = file(outDir, "run_" + runNum + "_out_" + slaveId);		
			writeAll(outputFile, lines(cmdLine, run.toString()));

		} else {
			File outputFile = file(outDir, "run_" + runNum + "_out");		
			writeAll(outputFile, lines(cmdLine, run.toString()));
		}
		
		if (run.exit!=0) {
			fprint(System.err, "NOT ABLE TO RUN PROGRAM EXIT CODE : ", run.exit);
			File errorFile = file(outDir, "run_" + runNum + "_err_" + run.exit);
			writeAll(errorFile, lines(cmdLine, run.stderr));
			rest(1000);
		} 
		
		
		
		if (slave) {
			print ("#### START SERVER", run.exit, "####");
			print (run);
			print ("#### END SERVER",  run.exit, "####");
			
			FileObject<String> reader = open(System.in);
			String line = reader.readLine();
			expect("", "ack", line);

		}

		
		return run.stdout;

	}
	
	public static void parseTest() {
		File file = file (cwf(), "src/com/examples/sample.txt");
		
		String sample = readAll(file);
		
		if (file!=null) {
			outputHeader();
		}
		
		outputResult(500, parseOutput(sample), parseOutput(sample));
	}

	private static void outputHeader() {
		String header = null;
		
		if (!shortForm) {
		header = join('\t',
				"dem_req_rate",	
				"req_rate_" + host1,	
				"con_rate_ch_" + host1,	
				"min_rep_rate_" + host1, 
				"avg_rep_rate_" + host1, 
				"max_rep_rate_ch_"+ host1,
				"stddev_rep_rate_" + host1, 
				"resp_time_" + host1,
				"net_io_" + host1,
				"errors_" + host1,
				"status_100_" + host1,
				"status_200_" + host1,
				"status_300_" + host1,
				"status_400_" + host1,
				"status_500_" + host1,
				"req_rate_" + host2,	
				"con_rate_ch_" + host2,	
				"min_rep_rate_" + host2, 
				"avg_rep_rate_" + host2, 
				"max_rep_rate_ch_"+ host2,
				"stddev_rep_rate_" + host2, 
				"resp_time_" + host2,
				"net_io_" + host2,
				"errors_" + host2,
				"status_100_" + host2,
				"status_200_" + host2,
				"status_300_" + host2,
				"status_400_" + host2,
				"status_500_" + host2
				);
		}	else {
			header = join('\t',
					"rate",	
					"rate_" + host1,	
					"io_" + host1,
					"errs_" + host1,
					"resp_time_" + host1,
					"rate_" + host2,	
					"io_" + host2,
					"errs_" + host2,
					"resp_time_" + host2
					);
			
		}

			if (verbose) print(header);

			appendWriteLine(outputFile(), header);
	}
	private static void outputResult(int rate, my server1, my server2) {
		
				
		
		String result1 = createResultForServer(server1);
		String result2 = createResultForServer(server2);
		
		String result = join('\t', rate*numCall, result1,  result2);
		if (verbose) print(result);
		
		appendWriteLine(outputFile(), result);

	
		
	}


	private static File outputFile() {
		
		
		File outFile = null;
		
		if (file == null) {
			outFile = file(outputFolder, "out.tsv");
			
		} else {		
			if (!file.isAbsolute()) {
				outFile = file(outputFolder, file.toString());
			} else {
				outFile = file;
			}
		}
		
		
		return outFile;
	
	}


	private static String createResultForServer(my server) {


		if (shortForm) {
			
			return join('\t', toDouble(server.i(req_rate)), server.i(net_io), server.i(error_total), server.i(rep_time)
					);		

		
		//server1.i(key, value)
		// dem_req_rate	req_rate_ch_resin	con_rate_ch_resin	min_rep_rate_ch_resin	avg_rep_rate_ch_resin
		// X            req_rate			conn_rate			rep_rate_min			rep_rate_avg

		}
		else {
		return join('\t', toDouble(server.i(req_rate)), server.i(conn_rate), server.i(rep_rate_min), server.i(rep_rate_avg),
				
				// max_rep_rate_ch_resin	stddev_rep_rate_ch_resin	resp_time_ch_resin	net_io_ch_resin		errors_ch_resin
				// rep_rate_max				rep_rate_stdv				rep_time			net_io			error_total
				server.i(rep_rate_max), server.i(rep_rate_stdv), server.i(rep_time), server.i(net_io), server.i(error_total),
				
				// status_100_ch_resin	status_200_ch_resin		status_300_ch_resin		status_400_ch_resin		status_500_ch_resin
				// status_100			status_200				status_300				status_400				status_500
				server.i(status_100), server.i(status_200), server.i(status_300), server.i(status_400), server.i(status_500)
				);		
		}
	}


	public static my parseOutput(String output) {
		my $results = my(Results.class, all);

		openString(IN, output);

		while (IN()) {

			if (ok("^Total: .*replies (\\d+)")) {
				$results.i(replies, $1());
			}
			if (ok("^Errors:.*total (\\d+)")) {
				$results.i(error_total, $1());
			}
			if (ok("/{start line}Connection rate: ({digit}+/.{digit})/")) {
				$results.i(conn_rate, $1());
				print("********************  CON RATE", conn_rate, $results.i(conn_rate));
			}
			if (ok("^Request rate: (\\d+\\.\\d)")) {
				$results.i(req_rate, $1());
			}
			if (ok("/^Reply rate .*min (/d+/./d) avg (/d+/./d) max (/d+/./d) stddev (/d+/./d)/")) {
				$results.i(rep_rate_min, $1());
				$results.i(rep_rate_avg, $2());
				$results.i(rep_rate_max, $3());
				$results.i(rep_rate_stdv, $4());
			}
			if (ok("/^Reply time {any} response ({digit}+/.{digit})/")) {
				$results.i(rep_time, $1());
			}
			if (ok("/^Reply status{any}1xx=({digit}+) 2xx=({digit}+) 3xx=({digit}+) 4xx=({digit}+) 5xx=({digit}+){any}/")) {
				$results.i(status_100, $1());
				$results.i(status_200, $2());
				$results.i(status_300, $3());
				$results.i(status_400, $4());
				$results.i(status_500, $5());
			}
			
			
			if (ok("/^Net I{fw}O: ({digit}+/.{digit})/")) {
	            $results.i(net_io, $1());
	        }

		}
		return $results;

	}

	
	public static void main (String [] args) {
		context(AutoBench.class, args, new Runnable() {
			@Override
			public void run() {
				runIt();
			}
		});
	}

//	print (args);
//
//	print("file", AutoBench.file, 
//			"host1", AutoBench.host1,
//			"uri1", AutoBench.uri1,
//			"host2", AutoBench.host2,
//			"uri2", AutoBench.uri2,	
//			"numConn", AutoBench.numConn,
//			"numCall", AutoBench.numCall,
//			"lowRate", AutoBench.lowRate,
//			"highRate", AutoBench.highRate,
//			"rateStep", AutoBench.rateStep,
//			"timeout", AutoBench.timeout				
//			);
//	lines(
//"Usage: httperf [-hdvV] [--add-header S] [--burst-length N] [--client N/N]",
//"[--close-with-reset] [--debug N] [--failure-status N]",
//"[--help] [--hog] [--http-version S] [--max-connections N]",
//"[--max-piped-calls N] [--method S] [--no-host-hdr]",
//"[--num-calls N] [--num-conns N] [--session-cookies]",
//"[--period [d|u|e]T1[,T2]|[v]T1,D1[,T2,D2]...[,Tn,Dn]",
//"[--print-reply [header|body]] [--print-request [header|body]]",
//"[--rate X] [--recv-buffer N] [--retry-on-failure] [--send-buffer N]",
//"[--server S] [--server-name S] [--port N] [--uri S]",
//"[--ssl] [--ssl-ciphers L] [--ssl-no-reuse]",
//"[--think-timeout X] [--timeout X] [--verbose] [--version]",
//"[--wlog y|n,file] [--wsess N,N,X] [--wsesslog N,X,file]",
//"[--wset N,X]",
//"[--use-timer-cache]");



}
