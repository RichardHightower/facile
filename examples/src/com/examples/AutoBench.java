package com.examples;

import static org.facile.ContextParser.*;
import static org.facile.Facile.*;

import java.util.List;

import org.facile.ProcessIO.ProcessOut;



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
	int rateStep;
	//--timeout 3
	int timeout;
	//--file out_con$1_start$2_end$3_step$4_$5.tsv
	String file;


	public static void runIt() {
		ProcessOut out;
		
		List<String> args = cmdLine();
		args();
		
		print (args, mul(50,"*"), mul(3,"\n"));
		args.add(0, "/usr/local/bin/httperf");
		print(args);
		out = run(array(args));
		
		print(lines(
				"COMMAND LINE",
				out.commandLine,
				"EXIT CODE",
				""+out.exit,
				"STD ERROR",
				out.stderr,
				"STD OUT",
				out.stdout
				));
		
	}
	
	public static void main (String [] args) {
		
		context(args, new Runnable() {
			@Override
			public void run() {
				runIt();
			}
		});

	}



}
