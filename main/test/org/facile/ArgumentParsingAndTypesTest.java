package org.facile;

import static org.facile.Facile.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ArgumentParsingAndTypesTest {
	
	Class <ArgumentParsingAndTypesTest> clz = ArgumentParsingAndTypesTest.class;
	
	// --clients xen.caucho.com:4600,lancre.caucho.com:4600 
	static String [] clients;
	//--uri1 /file_$5.html
	static String uri1;
//	// --host1 ch_resin
	static String host1;
	// --port1 8080
	static int port1;
//	//--uri2 /file_0k.html
	static String uri2;
//	//--host2 ch_nginx
	static String host2;
//	//--port2 80 
//	static int port2;
//	//--num_conn $1
//	static int numConn;
//	//--num_call 10
	static int numCall;
//	//--low_rate $2
//	static int lowRate;
//	//--high_rate $3
//	static int highRate;


	String sargs;
	String [] aargs;
	
	{
		
		sargs = "--uri1 /file_$5.html  --start   --host1 ch_resin --port1 8080 --uri2 /file_0k.html --num_call 10";

//				"--uri1 /file_$5.html --host1 ch_resin --port1 8080 --uri2 /file_0k.html ;";
//				"" +
//				"--host2 ch_nginx --port2 80 --num_conn $1 --num_call 10 --low_rate $2 " +
//				"--high_rate $3 --rate_step $4 --timeout 3 --file " +
//				"out_con$1_start$2_end$3_step$4_$5.tsv";
				
		aargs = split(sargs);

	}
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


	}

	@Test
	public void testTypeConversionDobule() {
		String s = "abcdefg123.0ffffff";
		double i = 0.0;
		
		MyClass foo = new MyClass();
		i = toDouble(foo);
		expect(123.0, i);
		
		Float d = Float.valueOf("123.2");
		i = toDouble(d);
		expect(123.1, 123.2, i);
		
		MyClass2 bar = new MyClass2();
		i = toDouble(bar);
		print(i);

		i = toDouble(s);
		
		expect(123.0, i);
	}

	@Test
	public void processArguments() {
		sargs = "--uri1 /file_$5.html  --start   --host1 ch_resin --port1 8080 --uri2 /file_0k.html --num_call 10";

		
		Map<String, ?> args = cmdToMap(aargs);
		
		List<String> cmdLine = get(slist, args, "all");
		assertNotNull(cmdLine);
		
		String uri = get(string, args, "uri1");
		assertEquals("/file_$5.html", uri);
		
		int numCall = get(integer, args, "numCall");
		assertEquals(10, numCall);
		
		
		List<String> actions = get(slist, args, "actions");
		expect(isIn("start", actions));
		expect(1 , len(actions));
		
	}
	
	@Test
	public void copyArgsTest() {
		//public static void copyArgs(Class<?> clz, Map<String, ?> args) {

		sargs = "--uri1 /file_$5.html  --start   --host1 ch_resin --port1 8080 --uri2 /file_0k.html " +
				" --num_call 10  --clients darth.foobar.com:4600,luke.foobar.com:4600 --dry-run true";

		aargs = split(sargs);

		Map<String, ?> args = cmdToMap(aargs);


		copyArgs(clz, args);
		
		assertEquals ("clients", "darth.foobar.com:4600 luke.foobar.com:4600 ", sprint(clients));
		assertEquals ("uri1", "/file_$5.html", uri1);
		assertEquals ("port1", 8080, port1);
		assertEquals ("numCall", 10, numCall);
		
	}

}
