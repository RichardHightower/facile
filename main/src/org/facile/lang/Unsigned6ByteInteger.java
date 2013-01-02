package org.facile.lang;

import java.math.BigInteger;

@SuppressWarnings("serial")
public class Unsigned6ByteInteger extends UnsignedBaseByteInteger {

	public static final long MAX_VALUE =  0xFF_FF_FF_FF_FF_FFL;
	public static final long MIN_VALUE =  0;
	public static final long MAX_NEGATIVE =  -1 * MAX_VALUE;

	byte msb;
	byte m1;
	byte m2;
	byte m3;
	byte m4;
	byte lsb;

	public Unsigned6ByteInteger (byte msb, byte m1, byte m2, byte m3, byte m4, byte lsb) {
		this.m1 = m1;
		this.m2 = m2;
		this.m3 = m3;
		this.m4 = m4;
		this.msb = msb;
		this.lsb = lsb;
		bi = new BigInteger(new byte[]{0, msb, m1, m2, m3, m4, lsb});
	}

	public Unsigned6ByteInteger (long i) {
		
		bi = new BigInteger("" + i);
		byte[] byteArray = bi.toByteArray();

		if (byteArray.length == 6) {
			this.msb = byteArray[0];
			this.m1 = byteArray[1];
			this.m2 = byteArray[2];
			this.m3 = byteArray[3];
			this.m4 = byteArray[4];
			this.lsb = byteArray[5];
		}else if (byteArray.length == 7){
			this.msb = byteArray[1];
			this.m1 = byteArray[2];
			this.m2 = byteArray[3];
			this.m3 = byteArray[4];
			this.m4 = byteArray[5];
			this.lsb = byteArray[6];
		}
	}
	
	public byte[] toByteArray() {
		return new byte[]{msb, m1, m2, m3, m4, lsb};
	}
}
