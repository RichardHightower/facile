package org.facile;


import static org.facile.Facile.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class StringTest {
	@Test
	public void test() {
		
		String foo = "foo_bar";
		foo = join(split(foo, '_'));
		assertEquals("foobar", foo);
		
		//To Camel
		foo = "foo_bar";
		String[] split = split(foo, "_");
		Appendable buf = buf();
		
		int index = 0;
		for (String s : split) {
			if (index==0) {
				add(buf, s);
			} else {
				add(buf, upper(slc(s, 0, 1)), slc(s, 1));
			}
			index++;
		}
		print(str(buf));
		
	}
}
