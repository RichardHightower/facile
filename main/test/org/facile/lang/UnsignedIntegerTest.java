package org.facile.lang;

import static org.junit.Assert.*;

import org.facile.lang.UnsignedInteger;
import org.junit.Test;

public class UnsignedIntegerTest {

	
	public static final long BIG_INT = 3_147_483_647l;
	
	public static final long LIMIT =  0x7fffffff;

	
	@Test
	public void test() {
		UnsignedInteger ub = new UnsignedInteger(BIG_INT);
		UnsignedInteger ub2 = new UnsignedInteger(ub.intValue());
		assertEquals(BIG_INT,ub2.longValue());
		
		
		ub = new UnsignedInteger(LIMIT-1);
		ub2 = new UnsignedInteger(ub.intValue());
		assertEquals(LIMIT-1,ub2.longValue());
		
		ub = new UnsignedInteger(LIMIT);
		ub2 = new UnsignedInteger(ub.intValue());
		assertEquals(LIMIT,ub2.longValue());


		ub = new UnsignedInteger(LIMIT+1);
		ub2 = new UnsignedInteger(ub.intValue());
		assertEquals(LIMIT+1,ub2.longValue());

		ub = new UnsignedInteger(0);
		ub2 = new UnsignedInteger(ub.intValue());
		assertEquals(0,ub2.intValue());


	}

}
