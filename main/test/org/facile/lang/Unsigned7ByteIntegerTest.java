package org.facile.lang;

import static org.junit.Assert.*;
import org.junit.Test;

public class Unsigned7ByteIntegerTest {

	@Test
	public void test() {
		Unsigned7ByteInteger ub = new Unsigned7ByteInteger((byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF);
		Unsigned7ByteInteger ub2 = new Unsigned7ByteInteger(ub.longValue());
		assertEquals(0xFF_FF_FF_FF_FF_FF_FFL,ub2.longValue());
		assertEquals(0xFF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[0]));
		assertEquals(0xFF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[1]));
		assertEquals(0xFF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[2]));
		assertEquals(0xFF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[3]));
		assertEquals(0xFF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[4]));
		assertEquals(0xFF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[5]));
		assertEquals(0xFF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[6]));


		
		ub = new Unsigned7ByteInteger((byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00);
		ub2 = new Unsigned7ByteInteger(ub.longValue());
		assertEquals(0,ub2.longValue());
		assertEquals(0, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[0]));
		assertEquals(0, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[1]));
		assertEquals(0, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[2]));
		assertEquals(0, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[3]));
		assertEquals(0, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[4]));
		assertEquals(0, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[5]));
		assertEquals(0, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[6]));


		
		ub = new Unsigned7ByteInteger((byte)0xEF, (byte)0xDF, (byte)0xCF, (byte)0xBF, (byte)0xAF, (byte)0xAA, (byte)0xBB);
		ub2 = new Unsigned7ByteInteger(ub.longValue());
		assertEquals(0xEF_DF_CF_BF_AF_AA_BBL,ub2.longValue());
		assertEquals(0xEF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[0]));
		assertEquals(0xDF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[1]));
		assertEquals(0xCF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[2]));
		assertEquals(0xBF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[3]));
		assertEquals(0xAF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[4]));
		assertEquals(0xAA, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[5]));
		assertEquals(0xBB, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[6]));

	}

}
