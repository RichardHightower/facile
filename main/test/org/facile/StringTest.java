package org.facile;

import static org.facile.Facile.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class StringTest {
	@Test
	public void test() {

		// To Camel
		assertEquals("fooBar", camelCase("foo_bar", false));
		assertEquals("fooBar", camelCase("foo-bar", false));
		assertEquals("fooBar", camelCase("foo bar", false));

		assertEquals("FooBar", camelCase("foo_bar", true));
		assertEquals("FooBar", camelCase("foo-bar", true));
		assertEquals("FooBar", camelCase("foo bar", true));
		
		assertEquals("FooBar", camelCase("Foo_bar"));
		assertEquals("FooBar", camelCase("Foo-bar"));
		assertEquals("FooBar", camelCase("Foo bar"));

		assertEquals("fooBar", camelCase("foo_bar"));
		assertEquals("fooBar", camelCase("foo-bar"));
		assertEquals("fooBar", camelCase("foo bar"));
		
		assertEquals("FooBar", camelCase("FOO BAR", true));
		assertEquals("fooBar", camelCase("FOO-BAR", false));
		assertEquals("FooBar", camelCase("Foo-BAR"));



		assertEquals("[which, httperf]", str(ls(split("which httperf"))));


	}

	ByteArrayOutputStream err;
	ByteArrayOutputStream out;

	private void setupStreams() {
		err = new ByteArrayOutputStream();
		out = new ByteArrayOutputStream();

		PrintStream ps = new PrintStream(err);
		System.setErr(ps);
		ps = new PrintStream(out);
		System.setOut(ps);

	}
	
	@Test 
	public void testStrings() {
		assertEquals("\"Rick Hightower\"", quote("Rick Hightower"));
		assertEquals("'Rick Hightower'", singleQuote("Rick Hightower"));
		assertEquals("Rick Hightower,", comma("Rick Hightower"));
		
		
		
	}
	
	@Test public void printTest() {
		
		setupStreams();
		print("Hi", "Mom");
		assertEquals("Hi Mom \n", out.toString());
		
		setupStreams();		
		print(1, 2, 3, 4, "Hi");
		assertEquals("1 2 3 4 Hi \n", out.toString());
		
	}


}
