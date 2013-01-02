package org.facile.lang;

@SuppressWarnings("serial")
public class UnsignedInteger extends Number {
	public static final long MAX_VALUE = 0xFF_FF_FF_FFL;
	public static final long MIN_VALUE = 0;
	
	public static final long MAX_NEGATIVE = -1 * MAX_VALUE;

	
	private long value;

	public UnsignedInteger (long value) {
		this.value = value;
	}

	public UnsignedInteger (int value) {
		this.value = unsignedIntToLong(value);
	}
	
	public static long unsignedIntToLong(int ivalue) {
		long value;
		if (ivalue < 0) {
			ivalue = (int)(ivalue & 0x7F_FF_FF_FF);
			value = ivalue;
			value = (long)(value | 0x80_00_00_00L);
		} else {
			value = ivalue;
		}
		return value;
		
	}

	@Override
	public int intValue() {
		return (int) value;
	}

	@Override
	public long longValue() {
		return value;
	}

	@Override
	public float floatValue() {
		return (float) value;
	}

	@Override
	public double doubleValue() {
		return (double) value;
	}
	
}
