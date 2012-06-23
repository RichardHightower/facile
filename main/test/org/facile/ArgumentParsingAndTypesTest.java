package org.facile;

import static org.facile.Facile.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ArgumentParsingAndTypesTest {
	
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

	
	class MyClass {
		public String toString() {
			return "abc 123";
		}
	}

	class MyClass2 {
	}


	@Test
	public void testTypeConversion() {
		String s = "abcdefg123ffffff";
		int i = toInt(s);
		expect(123, i);
		
		
		MyClass foo = new MyClass();
		i = toInt(foo);
		expect(123, i);
		
		Double d = Double.valueOf("123.2");
		i = toInt(d);
		expect(123, i);
		
		MyClass2 bar = new MyClass2();
		i = toInt(bar);
		print(i);


	}
	
	@Test
	public void processArguments() {
		
		String sargs = "--uri1 /file_$5.html  --start   --host1 ch_resin --port1 8080 --uri2 /file_0k.html --num_call 10";

//				"--uri1 /file_$5.html --host1 ch_resin --port1 8080 --uri2 /file_0k.html ;";
//				"" +
//				"--host2 ch_nginx --port2 80 --num_conn $1 --num_call 10 --low_rate $2 " +
//				"--high_rate $3 --rate_step $4 --timeout 3 --file " +
//				"out_con$1_start$2_end$3_step$4_$5.tsv";
				
		String [] aargs = split(sargs);
		
		Map<String, ?> args = cmdToMap(aargs);
		
		List<String> cmdLine = get(slist, args, "all");
		assertNotNull(cmdLine);
		
		String uri = get(string, args, "uri1");
		assertEquals("/file_$5.html", uri);
		
		int numCall = get(integer, args, "num_call");
		assertEquals(10, numCall);
		
		
		List<String> actions = get(slist, args, "actions");
		expect(isIn("start", actions));
		expect(1 , len(actions));
		
	}

}
