package com.examples;

import static org.facile.ContextParser.*;
import static org.facile.Facile.*;

import java.io.File;
import java.util.List;

//import org.facile.ProcessIO.ProcessOut;
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
	
	static List<File> path;

	public static void runIt() {
		
		if (homeDir==null) {
			homeDir = cwf();
			if (verbose) print ("Home directory was not passed setting to: ", homeDir);
		}
		
		List<String> cmdLine = cmdLine();


		
		if (verbose) print ("Command line arguments", cmdLine());
		
		File outputFolder = file (homeDir, "/output");
	
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
		String runString1 = join("_", array("run1", "host", host1, "port", port1, "uri", uri1, "numConn", numConn, "numCall", numCall, "rates", lowRate, highRate, rateStep));
		@SuppressWarnings("unchecked")
		String runString2 = join("_", array("run2", "host", host2, "port", port2, "uri", uri2, "numConn", numConn, "numCall", numCall, "rates", lowRate, highRate, rateStep));
		
		runString1 = runString1.replace('.', '_').replace('/', '_').replace(' ', '_').replace(':', '_').replace(',', '_');
		runString2 = runString2.replace('.', '_').replace('/', '_').replace(' ', '_').replace(':', '_').replace(',', '_');
		
		//Build an output file dir that uniquely defines this run
		File out1Dir = file(outputFolder,runString1);
		File out2Dir = file(outputFolder,runString2);
		
		out1Dir.mkdirs();
		out2Dir.mkdirs();

		
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
				runServer(1, httperf1, out1Dir, index);
				runServer(2, httperf2, out2Dir, index);
			}
			
		}

		
	}
	
	public static void runServer(int serverNum, String cmdLine, File outDir, int runNum) {
		print ("SERVER", serverNum, ":", mul(50, "*"));
		if (echoCommandLine || verbose) print("RUNNING:", cmdLine);
		ProcessOut run = run(0, path, cmdLine);
		print ("EXIT CODE for SERVER ", serverNum, ":",  run.exit);
		if (verbose) print(run.stdout);
		
		File outputFile = file(outDir, "run_" + runNum + "_out");		
		writeAll(outputFile, lines(cmdLine, run.toString()));
		
		if (run.exit!=0) {
			fprint(System.err, "NOT ABLE TO RUN PROGRAM EXIT CODE : ", run.exit);
			File errorFile = file(outDir, "run_" + runNum + "_err_" + run.exit);
			writeAll(errorFile, lines(cmdLine, run.stderr));
		} 

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
