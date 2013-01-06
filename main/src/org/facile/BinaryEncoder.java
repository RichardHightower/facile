package org.facile;

import static org.facile.Facile.*;

import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.facile.lang.Unsigned3ByteInteger;
import org.facile.lang.Unsigned5ByteInteger;
import org.facile.lang.Unsigned6ByteInteger;
import org.facile.lang.Unsigned7ByteInteger;
import org.facile.lang.UnsignedByte;
import org.facile.lang.UnsignedInteger;

public class BinaryEncoder {
	public static final byte NULL = -51; // 0 bytes

	public static final byte BYTE = -52; // 1 byte
	public static final byte UNSIGNED_BYTE = -53; // 1 byte
	public static final byte NEGATIVE_BYTE = -54; // 1 byte

	public static final byte SHORT = -55; // 2 bytes
	public static final byte UNSIGNED_SHORT = -56; // 2 bytes
	public static final byte CHAR = -57; // UNSIGNED_SHORT = CHAR = 2 bytes
	public static final byte NEGATIVE_SHORT = -58; // 2 bytes

	public static final byte UNSIGNED_3_BYTE_INT = -59; // 3 bytes
	public static final byte NEGATIVE_3_BYTE_INT = -60; // 3 bytes

	public static final byte INT = -61; // 4 bytes
	public static final byte UNSIGNED_INT = -62; // 4 bytes
	public static final byte NEGATIVE_INT = -63; // 4 bytes

	public static final byte UNSIGNED_5_BYTE_INT = -64; // 5 bytes
	public static final byte NEGATIVE_5_BYTE_INT = -65; // 5 bytes

	public static final byte UNSIGNED_6_BYTE_INT = -66; // 6 bytes
	public static final byte NEGATIVE_6_BYTE_INT = -67; // 6 bytes

	public static final byte UNSIGNED_7_BYTE_INT = -68; // 7 bytes
	public static final byte NEGATIVE_7_BYTE_INT = -69; // 7 bytes

	public static final byte LONG = -70;

	public static final byte FLOAT = -71;
	public static final byte DOUBLE = -72;
	public static final byte DOUBLE_STR = -73;
	public static final byte BIG_INT = -74;
	public static final byte BIG_DECIMAL = -75;

	public static final byte STRING = -80;
	public static final byte ARRAY = -81;
	public static final byte OBJECT = -82;
	public static final byte OBJECT_DYNAMIC_SCHEMA = -83;
	public static final byte OBJECT_SCHEMA = -84;
	public static final byte OBJECT_REF = -85;
	public static final byte MAP = -86;
	public static final byte MAP_KEYS = -87;

	public static final byte DATE_YEAR_ONLY = -90;
	public static final byte DATE_YEAR_MONTH_ONLY = -91;
	public static final byte DATE_YEAR_MONTH_DAY_ONLY = -92;
	public static final byte DATE_YEAR_MONTH_DAY_HOUR_ONLY = -93;
	public static final byte DATE_YEAR_MONTH_DAY_HOUR_MINUTE_ONLY = -94;
	public static final byte DATE_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_ONLY = -95;
	public static final byte DATE_YEAR_MONTH_DAY_HOUR_TZ_ONLY = -96;
	public static final byte DATE_YEAR_MONTH_DAY_HOUR_MINUTE_TZ_ONLY = -97;
	public static final byte DATE_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_TZ_ONLY = -98;
	public static final byte DATE_ALL_TZ = -99;
	public static final byte DATE_ALL = -100;

	// 1 byte const
	public static final byte C_128 = -101;
	public static final byte C_NEG_100 = -102;
	public static final byte C_256 = -103;

	// 2 byte const
	public static final byte C_512 = -104;
	public static final byte C_1024 = -105;
	public static final byte C_2048 = -106;
	public static final byte C_4096 = -107;
	public static final byte C_8192 = -109;
	public static final byte C_1000 = -110;

	// float const
	public static final byte C_1_0 = -111;
	public static final byte C_10_0 = -112;
	public static final byte C_100_0 = -113;
	public static final byte C_0_0 = -114;
	public static final byte C_1_000_000 = -115; // 1 million
	public static final byte C_10_000_000 = -116; // 10 million
	// UI 3B M 16_777_215 Unsigned3ByteInteger.MAX_VALUE
	public static final byte C_100_000_000 = -117;// 100 million
	public static final byte C_1_000_000_000 = -118;// 1 billion
	// UI M 4_294_967_295 UnsignedInteger.MAX_VALUE
	public static final byte C_1_000_000_000_000 = -119;// 1 trillion
	// UI 5B M 1_099_511_627_775 Unsigned5ByteInteger.MAX_VALUE

	private DataOutput output;

	Map<String, Long> keyToNum = new HashMap<String, Long>();
	long keyNum = 0;

	public <K, V> void encodeMap(Map<K, V> map) throws IOException {
		encodeMap(map, true);
	}

	int depth;
	public <K, V> void encodeMap(Map<K, V> map, boolean flushKeys)
			throws IOException {
		output.write(MAP);
		encodeInteger(map.size());
		
		boolean root = false;
		if (depth==0) {
			root = true;
		}
		
		depth++;

		Set<Map.Entry<K, V>> entrySet = map.entrySet();
		for (Map.Entry<K, V> entry : entrySet) {
			if (entry.getKey() instanceof CharSequence) {
				Long key = null;
				if ((key = keyToNum.get(entry.getKey())) == null) {
					keyNum++;
					key = keyNum;
					keyToNum.put(entry.getKey().toString(), key);
				}
				encodeLong(key);
			} else {
				encodeValue(entry.getKey());
			}
			encodeValue(entry.getValue());
		}

		
		if (flushKeys && root) {
			flushKeys();
		}
	}

	private void flushKeys() throws IOException {
		output.write(MAP_KEYS);
		encodeInteger(keyToNum.size());
		for (Map.Entry<String, Long> entry : keyToNum.entrySet()) {
			encodeLong(entry.getValue());
			encodeString(entry.getKey());
		}

		keyToNum.clear();
		keyNum = 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void encodeValue(Object value) throws IOException {
		if (value instanceof String) {
			encodeString((String) value);
		} else if (value instanceof CharSequence) {
			encodeString((CharSequence) value);
		} else if (value instanceof Number) {
			encodeNumber((Number) value);
		} else if (value instanceof Date) {
			encodeDate((Date) value);
		} else if (value instanceof Map) {
			encodeMap((Map)value);
		} else if (Facile.isArray(value)) {
			encodeArray(value);
		} else if (value instanceof Collection) {
			encodeArray((Collection)value);
		} else if (value instanceof Calendar) {
			encodeDate((Calendar) value);
		} else {
			encodeObject(value);
		}

	}

	public void encodeObject(Object value) throws IOException {
		System.out.println("encode object " + value.getClass().getName());
		output.write(OBJECT);
		encodeMap(Reflection.toMap(value));		
	}

	public void encodeArray(Collection<?> value) throws IOException {
		Class<?> componentType = Reflection.getComponentType(value);
		Object array = Types.toArray(value);
		encodeArray(array, componentType);
	}

	
	public void encodeArray(Object value) throws IOException {
		Class<?> componentType = value.getClass().getComponentType();
		encodeArray(value, componentType);
	}
	public void encodeArray(Object value, Class<?> componentType) throws IOException {
		if (componentType.isPrimitive()) {
			if (componentType == pint) {
				encodeIntArray((int []) value);
			} else if (componentType == pshort) {
				encodeShortArray((short []) value);
			} else if (componentType == plong) {
				encodeLongArray((long []) value);					
			} else if (componentType == pchar) {
				encodeCharArray((char []) value);									
			} else if (componentType == pdouble) {
				encodeDoubleArray((double []) value);									
			} else if (componentType == pfloat) {
				encodeFloatArray((float []) value);									
			} else if (componentType == pbyte) {
				encodeByteArray((byte []) value);									
			}
		} else if (Types.isBasicType(componentType)) {
			if (componentType == integer) {
				encodeIntArray((Integer []) value);
			} else if (componentType == Short.class) {
				encodeShortArray((Short []) value);
			} else if (componentType == Long.class) {
				encodeLongArray((Long []) value);					
			} else if (componentType == Character.class) {
				encodeCharArray((Character []) value);									
			} else if (componentType == Double.class) {
				encodeDoubleArray((Double []) value);									
			} else if (componentType == Float.class) {
				encodeFloatArray((Float []) value);									
			} else if (componentType == Byte.class) {
				encodeByteArray((Byte []) value);									
			} else if (componentType == Date.class) {
				encodeDateArray((Date []) value);									
			} else if (componentType == Calendar.class) {
				encodeDateArray((Calendar []) value);									
			} else if (Types.isCharSequence(componentType)) {
				encodeStringArray((CharSequence []) value);									
			}	
		} else if (Types.isMap(componentType)) {
			encodeMapArray((Map []) value);									
		} else {
			encodeObjectArray((Object []) value);									
		}

	}

	public void encodeDate(Calendar value) throws IOException {
		encodeDate(value, true);
	}

	public void encodeDate(Date value) throws IOException {
		Calendar instance = Calendar.getInstance();
		instance.setTime(value);
		encodeDate(instance, false);
	}

	public void encodeDate(Date value, boolean timeZone) throws IOException {
		Calendar instance = Calendar.getInstance();
		instance.setTime(value);
		encodeDate(instance, timeZone);
	}

	public void encodeDate(Calendar value, boolean timeZone) throws IOException {

		int year = value.get(Calendar.YEAR); // can be 0
		int month = value.get(Calendar.MONTH);// can be 0
		int day = value.get(Calendar.DAY_OF_MONTH);// can be 1
		int hour = value.get(Calendar.HOUR_OF_DAY); // can be 0
		int minute = value.get(Calendar.MINUTE); // can be 0
		int second = value.get(Calendar.SECOND); // can be 0
		int milisecond = value.get(Calendar.MILLISECOND);// can be 0
		int offset = 0;
		int tz_seconds = 0;
		int tz_minutes = 0;

		if (timeZone) {
			offset = value.getTimeZone().getOffset(value.getTime().getTime());
			tz_seconds = offset / 1000;
			tz_minutes = tz_seconds / 60;
		}

		if (month == 0 && day == 1 && hour == 0 && minute == 0 && second == 0
				&& milisecond == 0) {
			output.write(DATE_YEAR_ONLY);
			output.writeShort(year);
		} else if (day == 1 && hour == 0 && minute == 0 && second == 0
				&& milisecond == 0) {
			output.write(DATE_YEAR_MONTH_ONLY);
			output.writeShort(year);
			output.writeByte((byte) month);
		} else if (hour == 0 && minute == 0 && second == 0 && milisecond == 0) {
			output.write(DATE_YEAR_MONTH_DAY_ONLY);
			output.writeShort(year);
			output.writeByte((byte) month);
			output.writeByte((byte) (day - 1)); // we do 0 based days, not one
												// based
			// because 1 based is stupid
		} else if (minute == 0 && second == 0 && milisecond == 0 && offset == 0) {
			output.write(DATE_YEAR_MONTH_DAY_HOUR_ONLY);
			output.writeShort(year);
			output.writeByte((byte) month);
			output.writeByte((byte) (day - 1)); // we do 0 based days, not one
												// based
			// because 1 based is stupid
			output.writeByte((byte) hour);
		} else if (second == 0 && milisecond == 0 && offset == 0) {
			output.write(DATE_YEAR_MONTH_DAY_HOUR_MINUTE_ONLY);
			output.writeShort(year); // 2 bytes
			output.writeByte((byte) month);
			output.writeByte((byte) (day - 1)); // we do 0 based days, not one
												// based
			// because 1 based is stupid
			output.writeByte((byte) hour);
			output.writeByte((byte) minute);
		} else if (milisecond == 0 && offset == 0) {
			output.write(DATE_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_ONLY);
			output.writeShort(year); // 2 bytes
			output.writeByte((byte) month);
			output.writeByte((byte) (day - 1)); // we do 0 based days, not one
												// based
			// because 1 based is stupid
			output.writeByte((byte) hour);
			output.writeByte((byte) minute);
			output.writeByte((byte) second);
		} else if (offset == 0) {
			output.write(DATE_ALL);
			output.writeShort(year); // 2 bytes
			output.writeByte((byte) month);
			output.writeByte((byte) (day - 1)); // we do 0 based days, not one
												// based
			// because 1 based is stupid
			output.writeByte((byte) hour);
			output.writeByte((byte) minute);
			output.writeByte((byte) second);
			output.writeShort(milisecond);
		} else if (minute == 0 && second == 0 && milisecond == 0 && offset != 0) {
			output.write(DATE_YEAR_MONTH_DAY_HOUR_TZ_ONLY);
			output.writeShort(year);
			output.writeByte((byte) month);
			output.writeByte((byte) (day - 1)); // we do 0 based days, not one
												// based
			// because 1 based is stupid
			output.writeByte((byte) hour);
			output.writeShort(tz_minutes);
		} else if (second == 0 && milisecond == 0 && offset != 0) {
			output.write(DATE_YEAR_MONTH_DAY_HOUR_MINUTE_TZ_ONLY);
			output.writeShort(year); // 2 bytes
			output.writeByte((byte) month);
			output.writeByte((byte) (day - 1)); // we do 0 based days, not one
												// based
			// because 1 based is stupid
			output.writeByte((byte) hour);
			output.writeByte((byte) minute);
			output.writeShort(tz_minutes);
		} else if (milisecond == 0 && offset != 0) {
			output.write(DATE_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_TZ_ONLY);
			output.writeShort(year); // 2 bytes
			output.writeByte((byte) month);
			output.writeByte((byte) (day - 1)); // we do 0 based days, not one
												// based
			// because 1 based is stupid
			output.writeByte((byte) hour);
			output.writeByte((byte) minute);
			output.writeByte((byte) second);
			output.writeShort(tz_minutes);
		} else {
			output.write(DATE_ALL_TZ);
			output.writeShort(year); // 2 bytes
			output.writeByte((byte) month);
			output.writeByte((byte) (day - 1)); // we do 0 based days, not one
												// based
			// because 1 based is stupid
			output.writeByte((byte) hour);
			output.writeByte((byte) minute);
			output.writeByte((byte) second);
			output.writeShort(milisecond);
			output.writeShort(tz_minutes);
		}

	}

	public void encodeNumber(Number value) throws IOException {

		if (value instanceof Integer) {
			encodeInteger(value.intValue());
		} else if (value instanceof Float) {
			encodeFloat(value.floatValue());
		} else if (value instanceof Double) {
			encodeDouble(value.floatValue());
		} else if (value instanceof BigInteger) {
			encodeBigInt((BigInteger) value);
		} else if (value instanceof BigDecimal) {
			encodeBigDecimal((BigDecimal) value);
		} else {
			encodeLong(value.longValue());
		}

	}

	public void encodeBigDecimal(BigDecimal value) throws IOException {
		try {
			long l = value.longValueExact();
			encodeLong(l);
		} catch (ArithmeticException ex) {
			output.write(BIG_DECIMAL);
			BigInteger unscaledValue = value.unscaledValue();
			encodeBigInt(unscaledValue);
			int scale = value.scale();
			encodeInteger(scale);
		}
	}

	public void encodeBigInt(BigInteger value) throws IOException {
		if (value.bitLength() + 1 < 64) {
			encodeLong(value.longValue());
		} else {
			output.write(BIG_INT);
			encodeByteArray(value.toByteArray());
		}

	}

	public void encodeString(CharSequence str) throws IOException {
		encodeString(str.toString());
	}

	public void encodeString(String str) throws IOException {

		output.write(STRING);
		byte[] bytes = str.getBytes("UTF-8");
		encodeInteger(str.length());
		output.write(bytes);
	}

	public void encodeByte(byte b) throws IOException {

		if (b >= -50) {
			output.write(b);
		} else {
			output.write(BYTE);
			output.write(b);
		}
	}

	public void encodeShort(short s) throws IOException {
		if (s == 128) {
			this.output.write(C_128);
		} else if (s == -100) {
			this.output.write(C_NEG_100);
		} else if (s == 256) {
			this.output.write(C_256);
		} else if (s == 512) {
			this.output.write(C_512);
		} else if (s == 1024) {
			this.output.write(C_1024);
		} else if (s == 2048) {
			this.output.write(C_2048);
		} else if (s == 4096) {
			this.output.write(C_4096);
		} else if (s == 8192) {
			this.output.write(C_8192);
		} else if (s == 1000) {
			this.output.write(C_1000);
		} else if (s <= Byte.MAX_VALUE && s >= Byte.MIN_VALUE) {
			encodeByte((byte) s);
		} else if (s <= UnsignedByte.MAX_VALUE && s >= UnsignedByte.MIN_VALUE) {
			encodeUnsignedByte((byte) s);
		} else if (s < 0 && s >= UnsignedByte.MAX_NEGATIVE) {
			encodeNegativeByte((byte) (s * -1));
		} else {
			output.write(SHORT);
			output.writeShort(s);
		}
	}

	public void encodeInteger(int i) throws IOException {
		if (i == 100_000_000) {
			output.write(C_100_000_000);
		} else if (i == 1_000_000_000) {
			output.write(C_1_000_000_000);
		} else if (i <= Short.MAX_VALUE && i >= Short.MIN_VALUE) {
			encodeShort((short) i);
		} else if (i <= Character.MAX_VALUE && i >= Character.MIN_VALUE) {
			encodeChar((char) i);
		} else if (i < 0 && i >= (-1 * Character.MAX_VALUE)) {
			encodeNegativeShort((char) (i * -1));
		}

		else if (i <= Unsigned3ByteInteger.MAX_VALUE
				&& i >= Unsigned3ByteInteger.MIN_VALUE) {
			encodeUnsigned3ByteInteger(i);
		} else if (i < 0 && i >= Unsigned3ByteInteger.MAX_NEGATIVE) {
			encodeNegative3ByteInteger(i * -1);
		}

		else {
			output.write(INT);
			output.writeInt(i);
		}
	}

	public void encodeNegativeShort(char i) throws IOException {
		output.write(NEGATIVE_SHORT);
		output.writeChar(i);

	}

	public void encodeLong(long l) throws IOException {

		if (l <= Integer.MAX_VALUE && l >= Integer.MIN_VALUE) {
			encodeInteger((int) l);
		}

		else if (l <= UnsignedInteger.MAX_VALUE
				&& l >= UnsignedInteger.MIN_VALUE) {
			encodeUnsignedInteger(l);
		} else if (l <= 0 && l >= UnsignedInteger.MAX_NEGATIVE) {
			encodeNegativeInteger(l * -1);
		}

		else if (l <= Unsigned5ByteInteger.MAX_VALUE
				&& l >= Unsigned5ByteInteger.MIN_VALUE) {
			encodeUnsigned5ByteInteger(l);
		} else if (l <= 0 && l >= Unsigned5ByteInteger.MAX_NEGATIVE) {
			encodeNegative5ByteInteger(l * -1);
		} else if (l <= Unsigned6ByteInteger.MAX_VALUE
				&& l >= Unsigned6ByteInteger.MIN_VALUE) {
			encodeUnsigned6ByteInteger(l);
		} else if (l <= 0 && l >= Unsigned6ByteInteger.MAX_NEGATIVE) {
			encodeNegative6ByteInteger(l * -1);
		} else if (l <= Unsigned7ByteInteger.MAX_VALUE
				&& l >= Unsigned7ByteInteger.MIN_VALUE) {
			encodeUnsigned7ByteInteger(l);
		} else if (l <= 0 && l >= Unsigned7ByteInteger.MAX_NEGATIVE) {
			encodeNegative7ByteInteger(l * -1);
		}

		else {
			output.write(LONG);
			output.writeLong(l);
		}
	}

	public void encodeNegative3ByteInteger(int i) throws IOException {
		Unsigned3ByteInteger u3i = new Unsigned3ByteInteger(i);
		output.write(NEGATIVE_3_BYTE_INT);
		output.write(u3i.toByteArray());

	}

	public void encodeUnsigned3ByteInteger(int i) throws IOException {

		if (i == 1_000_000) {
			output.write(C_1_000_000);
		} else if (i == 10_000_000) {
			output.write(C_10_000_000);
		} else {
			Unsigned3ByteInteger u3i = new Unsigned3ByteInteger(i);
			output.write(UNSIGNED_3_BYTE_INT);
			output.write(u3i.toByteArray());
		}

	}

	public void encodeNegative7ByteInteger(long l) throws IOException {
		Unsigned7ByteInteger u7i = new Unsigned7ByteInteger(l);
		output.write(NEGATIVE_7_BYTE_INT);
		output.write(u7i.toByteArray());

	}

	public void encodeUnsigned7ByteInteger(long l) throws IOException {
		Unsigned7ByteInteger u7i = new Unsigned7ByteInteger(l);
		output.write(UNSIGNED_7_BYTE_INT);
		output.write(u7i.toByteArray());

	}

	public void encodeNegative6ByteInteger(long l) throws IOException {
		Unsigned6ByteInteger u6i = new Unsigned6ByteInteger(l);
		output.write(NEGATIVE_6_BYTE_INT);
		output.write(u6i.toByteArray());

	}

	public void encodeUnsigned6ByteInteger(long l) throws IOException {
		Unsigned6ByteInteger u6i = new Unsigned6ByteInteger(l);
		output.write(UNSIGNED_6_BYTE_INT);
		output.write(u6i.toByteArray());
	}

	public void encodeNegative5ByteInteger(long l) throws IOException {
		Unsigned5ByteInteger u5i = new Unsigned5ByteInteger(l);
		output.write(NEGATIVE_5_BYTE_INT);
		output.write(u5i.toByteArray());
	}

	public void encodeUnsigned5ByteInteger(long l) throws IOException {

		if (l == 1_000_000_000_000l) {
			output.write(C_1_000_000_000_000);
		} else {
			Unsigned5ByteInteger u5i = new Unsigned5ByteInteger(l);
			output.write(UNSIGNED_5_BYTE_INT);
			output.write(u5i.toByteArray());
		}
	}

	public void encodeNegativeInteger(long l) throws IOException {
		output.write(NEGATIVE_INT);
		output.writeInt((int) l);
	}

	public void encodeUnsignedInteger(long l) throws IOException {
		output.write(UNSIGNED_INT);
		output.writeInt((int) l);
	}

	public void encodeChar(char c) throws IOException {
		if (c <= Byte.MAX_VALUE && c >= 0) {
			encodeByte((byte) c);
		} else {
			output.write(CHAR);
			output.writeChar(c);
		}
	}

	private void encodeNegativeByte(byte b) throws IOException {
		output.write(NEGATIVE_BYTE);
		output.write(b);
	}

	public void encodeUnsignedByte(short b) throws IOException {
		output.write(UNSIGNED_BYTE);
		output.write(b);
	}

	public void encodeBoolean(boolean v) throws IOException {
		if (v) {
			output.write(1);
		} else {
			output.write(0);
		}
	}

	public void encodeFloat(float v) throws IOException {
		if (v == 1.0) {
			output.write(C_1_0);
		} else if (v == 0.0) {
			output.write(C_0_0);
		} else if (v == 10.0) {
			output.write(C_10_0);
		} else if (v == 100.0) {
			output.write(C_100_0);
		} else {
			Float db = new Float(v);
			if (db.longValue() == v) {
				encodeLong(db.longValue());
			} else {
				output.write(FLOAT);
				output.writeFloat(v);
			}
		}
	}

	public void encodeDouble(double v) throws IOException {
		if (v == 1.0) {
			output.write(C_1_0);
		} else if (v == 0.0) {
			output.write(C_0_0);
		} else if (v == 10.0) {
			output.write(C_10_0);
		} else if (v == 100.0) {
			output.write(C_100_0);
		} else {
			String str = "" + v;
			if (str.length() < 7) {
				output.write(DOUBLE_STR);
				str.substring(1);
				byte[] bytes = str.getBytes("UTF-8");
				encodeInteger(bytes.length);
				output.write(bytes);
			} else {
				Double db = new Double(v);
				if (db.longValue() == v) {
					encodeLong(db.longValue());
				} else {
					output.write(DOUBLE);
					output.writeDouble(v);
				}
			}
		}
	}

	public void setDataOutput(DataOutput dataOutput) {
		this.output = dataOutput;
	}

	public void encodeShortArray(short[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(SHORT);
		encodeInteger(array.length);
		for (short value : array) {
			encodeShort(value);
		}
	}
	
	public void encodeShortArray(Short[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(SHORT);
		encodeInteger(array.length);
		for (short value : array) {
			encodeShort(value);
		}
	}


	public void encodeCharArray(char[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(CHAR);
		encodeInteger(array.length);
		for (char value : array) {
			encodeChar(value);
		}
	}

	public void encodeCharArray(Character[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(CHAR);
		encodeInteger(array.length);
		for (char value : array) {
			encodeChar(value);
		}
	}

	public void encodeLongArray(long[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(LONG);
		encodeInteger(array.length);
		for (long value : array) {
			encodeLong(value);
		}
	}

	public void encodeLongArray(Long[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(LONG);
		encodeInteger(array.length);
		for (long value : array) {
			encodeLong(value);
		}
	}

	public void encodeIntArray(int[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(INT);
		encodeInteger(array.length);
		for (int value : array) {
			encodeInteger(value);
		}
	}

	public void encodeIntArray(Integer[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(INT);
		encodeInteger(array.length);
		for (int value : array) {
			encodeInteger(value);
		}
	}

	public void encodeByteArray(byte[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(BYTE);
		encodeInteger(array.length);
		for (byte value : array) {
			output.writeByte(value);
		}
	}

	public void encodeByteArray(Byte[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(BYTE);
		encodeInteger(array.length);
		for (byte value : array) {
			output.writeByte(value);
		}
	}

	public void encodeDateArray(Date[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(DATE_ALL);
		encodeInteger(array.length);
		for (Date value : array) {
			encodeDate(value);
		}
	}

	public void encodeDateArray(Calendar[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(DATE_ALL);
		encodeInteger(array.length);
		for (Calendar value : array) {
			encodeDate(value);
		}
	}
	

	public void encodeStringArray(CharSequence[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(STRING);
		encodeInteger(array.length);
		for (CharSequence value : array) {
			encodeString(value);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public void encodeMapArray(Map[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(MAP);
		encodeInteger(array.length);
		for (Map value : array) {
			encodeMap(value);
		}
	}

	public void encodeObjectArray(Object[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(OBJECT);
		encodeInteger(array.length);
		for (Object value : array) {
			encodeObject(value);
		}
	}
	
	public void encodeDoubleArray(double[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(DOUBLE);
		encodeInteger(array.length);
		for (double value : array) {
			encodeDouble(value);
		}

	}

	public void encodeDoubleArray(Double[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(DOUBLE);
		encodeInteger(array.length);
		for (double value : array) {
			encodeDouble(value);
		}

	}

	public void encodeFloatArray(float[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(FLOAT);
		encodeInteger(array.length);
		for (float value : array) {
			encodeFloat(value);
		}
	}
	

	public void encodeFloatArray(Float[] array) throws IOException {
		output.writeByte(ARRAY);
		output.writeByte(FLOAT);
		encodeInteger(array.length);
		for (float value : array) {
			encodeFloat(value);
		}
	}

}
