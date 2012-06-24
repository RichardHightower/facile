package org.facile;

import static org.facile.Facile.*;
import static org.junit.Assert.*;

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

		print(join("abc", array("String", "String2")));
		print(join(array("String2", "String3"), "abc"));
		print(join(list("String4", "String5"), "abc"));
		print(join('c', list("String4", "String5")));
		print(join('c', "String", "String2"));
		print(join(array("String2", "String3"), 'c'));
		print(join(list("String4", "String5"), 'c'));
		print(join('c', list("String4", "String5")));

	}


}
