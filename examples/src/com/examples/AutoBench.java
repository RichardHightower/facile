package com.examples;

import static org.facile.ContextParser.*;
import static org.facile.Facile.*;

import java.util.List;

import org.facile.ProcessIO.ProcessOut;



public class AutoBench {

	public static void runIt() {
		ProcessOut out;
		
		List<String> args = args();
		print (args, mul(50,"*"), mul(3,"\n"));
		args.set(0, "httperf");
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
