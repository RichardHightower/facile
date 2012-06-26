package com.examples;

import static com.examples.Results.*;
import static org.facile.ContextParser.*;
import static org.facile.ContextParser.Input.IN;
import static org.facile.Facile.*;
import java.io.File;
import java.util.List;
import org.facile.Facile.my;
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
	
	static int processTimeout = 0;
	
	static List<File> path;
	
	static File outputFolder;

	public static void runIt() {
	
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

		
		outputHeader();
		
		for (int rate=lowRate, index=0; rate <highRate; rate+=rateStep, index++){


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
				print ("Running benchmark run number ", index);
				String out1 = runServer(1, httperf1, out1Dir, index);
				String out2 = runServer(2, httperf2, out2Dir, index);
				
				outputResult(500, parseOutput(out1), parseOutput(out2));
			}
			
		}

		
	}
	

	public static String runServer(int serverNum, String cmdLine, File outDir, int runNum) {
		print ("SERVER", serverNum, ":", mul(50, "*"));
		if (echoCommandLine || verbose) print("RUNNING:", cmdLine);
		ProcessOut run = run(processTimeout * 60, path, verbose, cmdLine);
		print ("EXIT CODE for SERVER ", serverNum, ":",  run.exit);
		if (verbose) print(run.stdout);
		
		File outputFile = file(outDir, "run_" + runNum + "_out");		
		writeAll(outputFile, lines(cmdLine, run.toString()));
		
		if (run.exit!=0) {
			fprint(System.err, "NOT ABLE TO RUN PROGRAM EXIT CODE : ", run.exit);
			File errorFile = file(outDir, "run_" + runNum + "_err_" + run.exit);
			writeAll(errorFile, lines(cmdLine, run.stderr));
			rest(1000);
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
	
		String header = join('\t',
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

			if (verbose) print(header);

			appendWriteLine(outputFile(), header);
	}
	private static void outputResult(int rate, my server1, my server2) {
		
				
		
		String result1 = createResultForServer(server1);
		String result2 = createResultForServer(server2);
		
		String result = join('\t', rate, result1,  result2);
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
		
		//server1.i(key, value)
		// dem_req_rate	req_rate_ch_resin	con_rate_ch_resin	min_rep_rate_ch_resin	avg_rep_rate_ch_resin
		// X            req_rate			conn_rate			rep_rate_min			rep_rate_avg

		return join('\t', server.i(req_rate), server.i(conn_rate), server.i(rep_rate_min), server.i(rep_rate_avg),
				
				// max_rep_rate_ch_resin	stddev_rep_rate_ch_resin	resp_time_ch_resin	net_io_ch_resin		errors_ch_resin
				// rep_rate_max				rep_rate_stdv				rep_time			net_io			error_total
				server.i(rep_rate_max), server.i(rep_rate_stdv), server.i(rep_time), server.i(net_io), server.i(error_total),
				
				// status_100_ch_resin	status_200_ch_resin		status_300_ch_resin		status_400_ch_resin		status_500_ch_resin
				// status_100			status_200				status_300				status_400				status_500
				server.i(status_100), server.i(status_200), server.i(status_300), server.i(status_400), server.i(status_500)
				);		
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
