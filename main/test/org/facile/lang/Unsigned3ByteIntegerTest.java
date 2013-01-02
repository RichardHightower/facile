package org.facile.lang;

import static org.junit.Assert.*;
import org.junit.Test;

public class Unsigned3ByteIntegerTest {

	@Test
	public void test() {
		Unsigned3ByteInteger ub = new Unsigned3ByteInteger((byte)0xFF, (byte)0xFF, (byte)0xFF);
		Unsigned3ByteInteger ub2 = new Unsigned3ByteInteger(ub.intValue());
		assertEquals(0xFF_FF_FF,ub2.intValue());
		assertEquals(0xFF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[0]));
		assertEquals(0xFF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[1]));
		assertEquals(0xFF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[2]));


		
		ub = new Unsigned3ByteInteger((byte)0x00, (byte)0x00, (byte)0x00);
		ub2 = new Unsigned3ByteInteger(ub.intValue());
		assertEquals(0,ub2.intValue());
		assertEquals(0, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[0]));
		assertEquals(0, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[1]));
		assertEquals(0, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[2]));


		
		ub = new Unsigned3ByteInteger((byte)0xEF, (byte)0xDF, (byte)0xCF);
		ub2 = new Unsigned3ByteInteger(ub.intValue());
		assertEquals(0xEF_DF_CF,ub2.intValue());
		assertEquals(0xEF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[0]));
		assertEquals(0xDF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[1]));
		assertEquals(0xCF, UnsignedByte.unsignedByteToInt(ub2.toByteArray()[2]));

	}

}
