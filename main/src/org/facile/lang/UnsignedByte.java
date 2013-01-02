package org.facile.lang;

@SuppressWarnings("serial")
public class UnsignedByte extends Number {
	public static final short MAX_VALUE = 255;
	public static final short MIN_VALUE = 0;
	public static final short MAX_NEGATIVE =  -1 * MAX_VALUE;

	
	private short value;

	public UnsignedByte (int value) {
		this.value = (short)value;
	}

	public UnsignedByte (byte value) {
		this.value = unsignedByteToShort(value);
	}
	
	public static short unsignedByteToShort(byte bvalue) {
		short value;
		if (bvalue>Byte.MAX_VALUE || bvalue < 0) {
			bvalue = (byte)(bvalue & 0x7F);
			value = bvalue;
			value = (short)(value | 0x80);
		} else {
			value = bvalue;
		}
		return value;
		
	}

	public static int unsignedByteToInt(byte bvalue) {
		int value;
		if (bvalue>Byte.MAX_VALUE || bvalue < 0) {
			bvalue = (byte)(bvalue & 0x7F);
			value = bvalue;
			value = (short)(value | 0x80);
		} else {
			value = bvalue;
		}
		return value;
	}

	@Override
	public int intValue() {
		return (int) value;
	}

	@Override
	public long longValue() {
		return (long) value;
	}

	@Override
	public float floatValue() {
		return (float) value;
	}

	@Override
	public double doubleValue() {
		return (double) value;
	}
	
	public byte byteValue() {
		return (byte) value;
	}

	public short shortValue() {
		return  value;
	}

}
