package org.facile.lang;

import java.math.BigInteger;

@SuppressWarnings("serial")
public class Unsigned3ByteInteger extends UnsignedBaseByteInteger {

	public static final int MAX_VALUE =  0xFF_FF_FF;
	public static final int MIN_VALUE =  0;
	public static final int MAX_NEGATIVE =  -1 * MAX_VALUE;

	byte msb;
	byte middle;
	byte lsb;

	public Unsigned3ByteInteger (byte msb, byte middle, byte lsb) {
		this.middle = middle;
		this.msb = msb;
		this.lsb = lsb;
		bi = new BigInteger(new byte[]{0, msb, middle, lsb});
		
		
	}
	public Unsigned3ByteInteger (int i) {
		
		bi = new BigInteger("" + i);
		byte[] byteArray = bi.toByteArray();

		if (byteArray.length == 3) {
			this.msb = byteArray[0];
			this.middle = byteArray[1];
			this.lsb = byteArray[2];
		}else if (byteArray.length == 4){
			this.msb = byteArray[1];
			this.middle = byteArray[2];
			this.lsb = byteArray[3];			
		}
	}

	
	public byte[] toByteArray() {
		return new byte[]{msb, middle, lsb};
	}
}
