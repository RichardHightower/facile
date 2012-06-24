package com.examples;

import static org.facile.ContextParser.*;
import static org.facile.Facile.*;

import java.io.File;
import java.util.List;

//import org.facile.ProcessIO.ProcessOut;
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
	static String file;
	
	static boolean dryRun;


	public static void runIt() {
		
		List<String> args = cmdLine();	
		
		print (args);

		print("file", AutoBench.file, 
				"host1", AutoBench.host1,
				"uri1", AutoBench.uri1,
				"host2", AutoBench.host2,
				"uri2", AutoBench.uri2,	
				"numConn", AutoBench.numConn,
				"numCall", AutoBench.numCall,
				"lowRate", AutoBench.lowRate,
				"highRate", AutoBench.highRate,
				"rateStep", AutoBench.rateStep,
				"timeout", AutoBench.timeout
				
				);

		
		//ProcessOut out;
		
		List<File> path = Sys.path();
		
		
		if (Sys.os().startsWith("Mac OS X")) {
			if (!isIn(file("/usr/bin"), path)) {
				path.add(file("/usr/bin"));
			}
			if (!isIn(file("/usr/local/bin/"), path)) {
				path.add(file("/usr/local/bin/"));
			}
		}
		
		
		args.add(0, "/usr/local/bin/httperf");
		
//		out = run (0, path, "which httperf");
//
//		print(lines(
//				"COMMAND LINE",
//				out.commandLine,
//				"EXIT CODE",
//				""+out.exit,
//				"STD ERROR",
//				out.stderr,
//				"STD OUT",
//				out.stdout
//				));
//
//		
		//		out = run(0, path, array(args));
		
//		print(lines(
//				"COMMAND LINE",
//				out.commandLine,
//				"EXIT CODE",
//				""+out.exit,
//				"STD ERROR",
//				out.stderr,
//				"STD OUT",
//				out.stdout
//				));

		lines(
	"Usage: httperf [-hdvV] [--add-header S] [--burst-length N] [--client N/N]",
	"[--close-with-reset] [--debug N] [--failure-status N]",
	"[--help] [--hog] [--http-version S] [--max-connections N]",
	"[--max-piped-calls N] [--method S] [--no-host-hdr]",
	"[--num-calls N] [--num-conns N] [--session-cookies]",
	"[--period [d|u|e]T1[,T2]|[v]T1,D1[,T2,D2]...[,Tn,Dn]",
	"[--print-reply [header|body]] [--print-request [header|body]]",
	"[--rate X] [--recv-buffer N] [--retry-on-failure] [--send-buffer N]",
	"[--server S] [--server-name S] [--port N] [--uri S]",
	"[--ssl] [--ssl-ciphers L] [--ssl-no-reuse]",
	"[--think-timeout X] [--timeout X] [--verbose] [--version]",
	"[--wlog y|n,file] [--wsess N,N,X] [--wsesslog N,X,file]",
	"[--wset N,X]",
	"[--use-timer-cache]");

		for (int rate=lowRate; rate <highRate; rate+=rateStep){
			
			if (dryRun) {
				print ("Dry run");
				print ("SERVER 1", mul(50, "*"));
				print("httperf", "--server", host1, "--uri", uri1, "--num-con", numConn,
					"--num-call", numCall,"--timeout", timeout, "--rate", rate,
					"--port", 8080);
				print ("SERVER 2", mul(50, "*"));
				print("httperf", "--server", host2, "--uri", uri2, "--num-con", numConn,
						"--num-call", numCall,"--timeout", timeout, "--rate", rate,
						"--port", 8080);
			} else {
				
			}
			
		}

		
	}
	
	public static void main (String [] args) {
		
	//--server ch_resin --uri "/file_0k.html" --num-conn 400000 --num-call 10 --timeout 3 --rate 1250 --port 8080 
	
		
		
		context(AutoBench.class, args, new Runnable() {
			@Override
			public void run() {
				runIt();
			}
		});

	}



}
