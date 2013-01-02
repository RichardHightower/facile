package org.facile.lang;

import static org.junit.Assert.*;

import org.facile.lang.UnsignedByte;
import org.junit.Test;

public class UnsignedByteTest {

	@Test
	public void test() {
		UnsignedByte ub = new UnsignedByte(255);
		UnsignedByte ub2 = new UnsignedByte(ub.byteValue());
		assertEquals(255,ub2.intValue());
		ub = new UnsignedByte(240);
		ub2 = new UnsignedByte(ub.byteValue());
		assertEquals(240,ub2.intValue());
		
		ub = new UnsignedByte(128);
		ub2 = new UnsignedByte(ub.byteValue());
		assertEquals(128,ub2.intValue());
		
		ub = new UnsignedByte(127);
		ub2 = new UnsignedByte(ub.byteValue());
		assertEquals(127,ub2.intValue());
		
		ub = new UnsignedByte(126);
		ub2 = new UnsignedByte(ub.byteValue());
		assertEquals(126,ub2.intValue());


		ub = new UnsignedByte(0);
		ub2 = new UnsignedByte(ub.byteValue());
		assertEquals(0,ub2.intValue());
		
		byte in = (byte)255;
		assertEquals(255,UnsignedByte.unsignedByteToInt(in));
		
		in = (byte)240;
		assertEquals(240,UnsignedByte.unsignedByteToInt(in));


	}

}
