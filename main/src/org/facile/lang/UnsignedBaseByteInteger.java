package org.facile.lang;

import java.math.BigInteger;

@SuppressWarnings("serial")
public abstract class UnsignedBaseByteInteger extends Number {

	
	protected BigInteger bi;
	private boolean negative;



	public boolean isNegative() {
		return negative;
	}

	public void setNegative(boolean negative) {
		this.negative = negative;
	}

	@Override
	public int intValue() {
		if (negative) {
			return -1 * bi.intValue();
		} else {
			return bi.intValue();
		}
	}

	@Override
	public long longValue() {
		if (negative) {
			return -1L * bi.longValue();
		} else {
			return bi.longValue();
		}

	}

	@Override
	public float floatValue() {
		if (negative) {
			return -1f * bi.floatValue();
		} else {
			return bi.floatValue();
		}
	}

	@Override
	public double doubleValue() {
		if (negative) {
			return -1f * bi.doubleValue();
		} else {
			return bi.doubleValue();
		}
	}
	
	public abstract byte[] toByteArray();
}
