package org.facile;

import java.io.DataInput;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.facile.lang.Unsigned3ByteInteger;
import org.facile.lang.Unsigned5ByteInteger;
import org.facile.lang.Unsigned6ByteInteger;
import org.facile.lang.Unsigned7ByteInteger;
import org.facile.lang.UnsignedByte;
import org.facile.lang.UnsignedInteger;

public class BinaryDecoder {

	DataInput input;

	public void setDataInput(DataInput input) {
		this.input = input;
	}

	public Object decodeValue() throws IOException {
		byte type = input.readByte();
		if (type >= BinaryEncoder.NULL) {
			return new Byte(type);
		} else if (type == BinaryEncoder.STRING) {
			return decodeString(type);
		} else if (type <= BinaryEncoder.UNSIGNED_3_BYTE_INT
				&& type >= BinaryEncoder.INT) {
			return new Integer(decodeInteger(type));
		} else if (type == BinaryEncoder.FLOAT) {
			return new Float(decodeFloat(type));
		} else if (type == BinaryEncoder.ARRAY) {
			return decodeArray(type);
		} else if (type == BinaryEncoder.MAP) {
			return decodeMap(type, true);
		} else if (type == BinaryEncoder.DOUBLE
				|| type == BinaryEncoder.DOUBLE_STR) {
			return new Double(decodeFloat(type));
		} else if (type == BinaryEncoder.BYTE) {
			return new Byte(decodeByte(type));
		} else if (type <= BinaryEncoder.UNSIGNED_BYTE
				&& type >= BinaryEncoder.SHORT) {
			return new Short(decodeShort(type));
		} else if (type <= BinaryEncoder.UNSIGNED_INT
				&& type >= BinaryEncoder.LONG) {
			return new Long(decodeLong(type));
		} else if (type <= BinaryEncoder.DATE_YEAR_ONLY && type >= BinaryEncoder.DATE_ALL ) {
			return decodeDate(type);
		} else if (type == BinaryEncoder.BIG_DECIMAL){
			return decodeBigDecimal(type);
		} else if (type == BinaryEncoder.BIG_INT){
			return decodeBigInteger(type);
		} else {
			return decodeObject(type);
		}
	}

	@SuppressWarnings("unchecked")
	public Object decodeObject(byte type) throws IOException {
		@SuppressWarnings("rawtypes")
		Map map = decodeMap(true);
		return Reflection.fromMap((Map<String, Object>)map);
	}

	public BigInteger decodeBigInteger() throws IOException {
		byte type = input.readByte();
		if (type == BinaryEncoder.BIG_INT) {
			return decodeBigInteger(type);
		} else {
			long value = decodeLong();
			return BigInteger.valueOf(value);
		}
	}
	
	public BigInteger decodeBigInteger(byte type) throws IOException {
		BigInteger bi = null;
		byte[] byteArray = decodeByteArray();
		bi = new BigInteger(byteArray);
		return bi;
	}

	public BigDecimal decodeBigDecimal(byte type) throws IOException {
		BigDecimal bd = null;
		BigInteger unscaledValue = null;
		int scale = 0;
		unscaledValue = decodeBigInteger();
		scale = decodeInteger();
		bd = new BigDecimal(unscaledValue, scale);
		return bd;
	}

	private Object decodeArray(byte type) throws IOException {
		byte componentType = input.readByte();
		if (componentType == BinaryEncoder.BYTE) {
			return decodeByteArray(type, componentType);
		} else if (componentType == BinaryEncoder.INT) {
			return decodeIntArray(type, componentType);
		}
		if (componentType == BinaryEncoder.LONG) {
			return decodeLongArray(type, componentType);
		}
		if (componentType == BinaryEncoder.SHORT) {
			return decodeShortArray(type, componentType);
		}
		if (componentType == BinaryEncoder.CHAR) {
			return decodeCharArray(type, componentType);
		} else {
			// TODO support dates, big decimal, big int, and strings
			return null;
		}
	}

	public byte decodeByte() throws IOException {

		byte type = input.readByte();
		return decodeByte(type);

	}

	public byte decodeByte(byte type) throws IOException {

		// if it is not these, we should warn.
		// if (type == BinaryEncoder.BYTE || type ==
		// BinaryEncoder.UNSIGNED_BYTE) {
		// }

		if (type >= -50) {
			return type;
		} else {
			return input.readByte();
		}
	}

	public boolean decodeBoolean() throws IOException {
		byte type = input.readByte();
		if (type == 1) {
			return true;
		} else if (type == 0) {
			return false;
		} else {
			throw new IOException("Expecting boolean but was " + type);
		}

	}

	public int decodeInteger() throws IOException {
		byte type = input.readByte();
		return decodeInteger(type);
	}

	private int decodeThreeByteInteger(byte type) throws IOException {

		if (type == BinaryEncoder.C_1_000_000) {
			return 1_000_000;
		} else if (type == BinaryEncoder.C_10_000_000) {
			return 10_000_000;
		} else {
			byte[] bytes = null;

			bytes = new byte[3];
			input.readFully(bytes);
			Unsigned3ByteInteger unsigned3ByteInteger = new Unsigned3ByteInteger(
					bytes[0], bytes[1], bytes[2]);
			return unsigned3ByteInteger.intValue();
		}

	}

	public int decodeInteger(byte type) throws IOException {
		byte[] bytes = null;

		if (type == BinaryEncoder.C_1_000_000) {
			return 1_000_000;
		} else if (type == BinaryEncoder.C_10_000_000) {
			return 10_000_000;
		} else if (type == BinaryEncoder.C_100_000_000) {
			return 100_000_000;
		} else if (type == BinaryEncoder.C_1_000_000_000) {
			return 1_000_000_000;
		} else if (type == BinaryEncoder.UNSIGNED_3_BYTE_INT) {
			return decodeThreeByteInteger(type);
		} else if (type == BinaryEncoder.NEGATIVE_3_BYTE_INT) {
			bytes = new byte[3];
			input.readFully(bytes);
			Unsigned3ByteInteger unsigned3ByteInteger = new Unsigned3ByteInteger(
					bytes[0], bytes[1], bytes[2]);
			unsigned3ByteInteger.setNegative(true);
			return unsigned3ByteInteger.intValue();
		} else if (type == BinaryEncoder.UNSIGNED_INT) {
			return input.readInt();
		} else if (type == BinaryEncoder.INT) {
			return input.readInt();
		} else if (type == BinaryEncoder.CHAR) {
			return input.readChar();
		} else if (type == BinaryEncoder.NEGATIVE_SHORT) {
			return input.readChar() * -1;
		} else {
			return decodeShort(type);
		}
	}

	public short decodeUnsignedByte() throws IOException {
		return decodeShort();
	}

	public short decodeShort() throws IOException {
		byte type = input.readByte();
		return decodeShort(type);
	}

	public short decodeShort(byte type) throws IOException {

		if (type == BinaryEncoder.C_128) {
			return 128;
		} else if (type == BinaryEncoder.C_NEG_100) {
			return -100;
		} else if (type == BinaryEncoder.C_256) {
			return 256;
		} else if (type == BinaryEncoder.C_512) {
			return 512;
		} else if (type == BinaryEncoder.C_1024) {
			return 1024;
		} else if (type == BinaryEncoder.C_2048) {
			return 2048;
		} else if (type == BinaryEncoder.C_4096) {
			return 4096;
		} else if (type == BinaryEncoder.C_8192) {
			return 8192;
		} else if (type == BinaryEncoder.C_1000) {
			return 1000;
		} else if (type == BinaryEncoder.CHAR) {
			return (short) input.readChar();
		} else if (type == BinaryEncoder.SHORT) {
			return input.readShort();
		} else if (type == BinaryEncoder.NEGATIVE_BYTE) {
			byte value = input.readByte();
			short s = UnsignedByte.unsignedByteToShort(value);
			return (short) (s * -1);
		} else if (type == BinaryEncoder.UNSIGNED_BYTE) {
			byte value = input.readByte();
			return UnsignedByte.unsignedByteToShort(value);
		} else {
			return decodeByte(type);
		}
	}

	public int decodeUnsignedShort() throws IOException {
		return decodeChar();
	}

	public char decodeChar() throws IOException {
		byte type = input.readByte();
		return decodeChar(type);
	}

	public char decodeChar(byte type) throws IOException {
		if (type == BinaryEncoder.CHAR) {
			return (char) input.readChar();
		} else {
			return (char) decodeShort(type);
		}
	}

	public long decodeLong() throws IOException {
		byte type = input.readByte();
		return decodeLong(type);
	}

	private long decode5ByteInt(byte type) throws IOException {

		if (type == BinaryEncoder.C_1_000_000_000_000) {
			return 1_000_000_000_000l;
		} else {
			byte[] bytes = new byte[5];
			input.readFully(bytes);
			Unsigned5ByteInteger unsigned5ByteInteger = new Unsigned5ByteInteger(
					bytes[0], bytes[1], bytes[2], bytes[3], bytes[4]);
			return unsigned5ByteInteger.longValue();
		}
	}

	public long decodeLong(byte type) throws IOException {
		byte[] bytes = null;

		if (type == BinaryEncoder.C_1_000_000) {
			return 1_000_000;
		} else if (type == BinaryEncoder.C_10_000_000) {
			return 10_000_000;
		} else if (type == BinaryEncoder.C_100_000_000) {
			return 100_000_000;
		} else if (type == BinaryEncoder.C_1_000_000_000) {
			return 1_000_000_000;
		} else if (type == BinaryEncoder.C_1_000_000_000_000) {
			return 1_000_000_000_000l;
		} else if (type == BinaryEncoder.UNSIGNED_5_BYTE_INT) {
			return decode5ByteInt(type);
		} else if (type == BinaryEncoder.LONG) {
			return input.readLong();
		} else if (type == BinaryEncoder.NEGATIVE_5_BYTE_INT) {
			bytes = new byte[5];
			input.readFully(bytes);
			Unsigned5ByteInteger unsigned5ByteInteger = new Unsigned5ByteInteger(
					bytes[0], bytes[1], bytes[2], bytes[3], bytes[4]);
			unsigned5ByteInteger.setNegative(true);
			return unsigned5ByteInteger.longValue();
		} else if (type == BinaryEncoder.UNSIGNED_6_BYTE_INT) {
			bytes = new byte[6];
			input.readFully(bytes);
			Unsigned6ByteInteger unsigned6ByteInteger = new Unsigned6ByteInteger(
					bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5]);
			return unsigned6ByteInteger.longValue();
		} else if (type == BinaryEncoder.NEGATIVE_6_BYTE_INT) {
			bytes = new byte[6];
			input.readFully(bytes);
			Unsigned6ByteInteger unsigned6ByteInteger = new Unsigned6ByteInteger(
					bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5]);
			unsigned6ByteInteger.setNegative(true);
			return unsigned6ByteInteger.longValue();
		} else if (type == BinaryEncoder.UNSIGNED_7_BYTE_INT) {
			bytes = new byte[7];
			input.readFully(bytes);
			Unsigned7ByteInteger unsigned7ByteInteger = new Unsigned7ByteInteger(
					bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5],
					bytes[6]);
			return unsigned7ByteInteger.longValue();
		} else if (type == BinaryEncoder.NEGATIVE_7_BYTE_INT) {
			bytes = new byte[7];
			input.readFully(bytes);
			Unsigned7ByteInteger unsigned7ByteInteger = new Unsigned7ByteInteger(
					bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5],
					bytes[6]);
			unsigned7ByteInteger.setNegative(true);
			return unsigned7ByteInteger.longValue();
		} else if (type == BinaryEncoder.UNSIGNED_INT) {
			int i = this.input.readInt();
			long l = UnsignedInteger.unsignedIntToLong(i);
			return l;
		} else if (type == BinaryEncoder.NEGATIVE_INT) {
			int i = this.input.readInt();
			return UnsignedInteger.unsignedIntToLong(i) * -1l;
		} else {
			return decodeInteger(type);
		}
	}

	public float decodeFloat() throws IOException {
		byte type = input.readByte();
		return decodeFloat(type);
	}

	public float decodeFloat(byte type) throws IOException {

		if (type == BinaryEncoder.C_0_0) {
			return 0.0f;
		} else if (type == BinaryEncoder.C_1_0) {
			return 1.0f;
		} else if (type == BinaryEncoder.C_10_0) {
			return 10.0f;
		} else if (type == BinaryEncoder.C_100_0) {
			return 100.0f;
		} else {
			if (type == BinaryEncoder.FLOAT) {
				return input.readFloat();
			} else {
				return decodeLong(type);
			}
		}
	}

	public double decodeDouble() throws IOException {
		byte type = input.readByte();

		if (type == BinaryEncoder.C_0_0) {
			return 0.0d;
		} else if (type == BinaryEncoder.C_1_0) {
			return 1.0d;
		} else if (type == BinaryEncoder.C_10_0) {
			return 10.0d;
		} else if (type == BinaryEncoder.C_100_0) {
			return 100.0d;
		} else if (type == BinaryEncoder.DOUBLE) {
			return input.readDouble();
		} else if (type == BinaryEncoder.DOUBLE_STR) {
			int size = decodeInteger();
			byte[] bytes = new byte[size];
			input.readFully(bytes);
			return Double.parseDouble(new String(bytes, "UTF-8"));
		} else {
			return decodeLong(type);
		}
	}

	public String decodeString() throws IOException {
		byte type = input.readByte();
		return decodeString(type);
	}

	public String decodeString(byte type) throws IOException {
		if (type == BinaryEncoder.STRING) {
			int size = decodeInteger();
			byte[] bytes = new byte[size];
			input.readFully(bytes);
			return new String(bytes, "UTF-8");
		} else {
			throw new IOException("Expecting String but was " + type);
		}
	}

	public double[] decodeDoubleArray() throws IOException {
		byte type = input.readByte();
		byte componentType = input.readByte();
		return decodeDoubleArray(type, componentType);
	}

	public double[] decodeDoubleArray(byte type, byte componentType)
			throws IOException {
		double[] array = null;

		if (type == BinaryEncoder.ARRAY
				&& ((componentType <= BinaryEncoder.BYTE) && (componentType >= BinaryEncoder.DOUBLE_STR))) {
			int size = decodeInteger();
			array = new double[size];
			for (int index = 0; index < array.length; index++) {
				array[index] = decodeDouble();
			}
			return array;
		} else {
			throw new IOException("Expecting type ARRAY DOUBLE but got " + type
					+ " " + componentType);
		}
	}

	public float[] decodeFloatArray() throws IOException {
		byte type = input.readByte();
		byte componentType = input.readByte();
		return decodeFloatArray(type, componentType);
	}

	public float[] decodeFloatArray(byte type, byte componentType)
			throws IOException {
		float[] array = null;

		if (type == BinaryEncoder.ARRAY
				&& ((componentType <= BinaryEncoder.BYTE) && (componentType >= BinaryEncoder.FLOAT))) {
			int size = decodeInteger();
			array = new float[size];
			for (int index = 0; index < array.length; index++) {
				array[index] = decodeFloat();
			}
			return array;
		} else {
			throw new IOException("Expecting type ARRAY FLOAT but got " + type
					+ " " + componentType);
		}
	}

	public short[] decodeShortArray() throws IOException {
		byte type = input.readByte();
		byte componentType = input.readByte();
		return decodeShortArray(type, componentType);
	}

	public short[] decodeShortArray(byte type, byte componentType)
			throws IOException {
		short[] array = null;

		if (type == BinaryEncoder.ARRAY
				&& ((componentType <= BinaryEncoder.BYTE) && (componentType >= BinaryEncoder.CHAR))) {
			int size = decodeInteger();
			array = new short[size];
			for (int index = 0; index < array.length; index++) {
				array[index] = decodeShort();
			}
			return array;
		} else {
			throw new IOException("Expecting type ARRAY SHORT but got " + type
					+ " " + componentType);
		}
	}

	public int[] decodeIntArray() throws IOException {
		byte type = input.readByte();
		byte componentType = input.readByte();
		return decodeIntArray(type, componentType);
	}

	public int[] decodeIntArray(byte type, byte componentType)
			throws IOException {
		int[] array = null;

		if (type == BinaryEncoder.ARRAY
				&& ((componentType <= BinaryEncoder.BYTE) && (componentType >= BinaryEncoder.NEGATIVE_INT))) {
			int size = decodeInteger();
			array = new int[size];
			for (int index = 0; index < array.length; index++) {
				array[index] = decodeInteger();
			}
			return array;
		} else {
			throw new IOException("Expecting type ARRAY SHORT but got " + type
					+ " " + componentType);
		}
	}

	public long[] decodeLongArray() throws IOException {
		byte type = input.readByte();
		byte componentType = input.readByte();
		return decodeLongArray(type, componentType);
	}

	public long[] decodeLongArray(byte type, byte componentType)
			throws IOException {
		long[] array = null;

		if (type == BinaryEncoder.ARRAY
				&& ((componentType <= BinaryEncoder.BYTE) && (componentType >= BinaryEncoder.LONG))) {
			int size = decodeInteger();
			array = new long[size];
			for (int index = 0; index < array.length; index++) {
				array[index] = decodeLong();
			}
			return array;
		} else {
			throw new IOException("Expecting type ARRAY LONG but got " + type
					+ " " + componentType);
		}
	}

	public char[] decodeCharArray() throws IOException {
		byte type = input.readByte();
		byte componentType = input.readByte();
		return decodeCharArray(type, componentType);
	}

	public char[] decodeCharArray(byte type, byte componentType)
			throws IOException {
		char[] array = null;

		if (type == BinaryEncoder.ARRAY
				&& ((componentType <= BinaryEncoder.BYTE) && (componentType >= BinaryEncoder.CHAR))) {
			int size = decodeInteger();
			array = new char[size];
			for (int index = 0; index < array.length; index++) {
				array[index] = decodeChar();
			}
			return array;
		} else {
			throw new IOException("Expecting type ARRAY CHAR but got " + type
					+ " " + componentType);
		}
	}

	public byte[] decodeByteArray() throws IOException {
		byte type = input.readByte();
		byte componentType = input.readByte();
		return decodeByteArray(type, componentType);
	}

	public byte[] decodeByteArray(byte type, byte componentType)
			throws IOException {

		byte[] array = null;

		if (type == BinaryEncoder.ARRAY
				&& (componentType <= BinaryEncoder.BYTE)) {
			int size = decodeInteger();
			array = new byte[size];
			for (int index = 0; index < array.length; index++) {
				array[index] = input.readByte();
			}
			return array;
		} else {
			throw new IOException("Expecting type ARRAY CHAR but got " + type
					+ " " + componentType);
		}
	}

	class MapMap {
		Map<java.lang.Number, Object> sequenceMap;
		Map<String, Object> map;
	}

	private List<MapMap> mappings = new ArrayList<>();

	@SuppressWarnings("unchecked")
	private <K, V> void registerMap(Map<java.lang.Number, Object> sequenceMap,
			Map<K, V> map) {
		MapMap mapMap = new MapMap();
		mapMap.sequenceMap = sequenceMap;
		mapMap.map = (Map<String, Object>) map;
		mappings.add(mapMap);
	}

	public <K, V> Map<K, V> decodeMap(boolean stringKeys) throws IOException {
		byte type = input.readByte();
		return decodeMap(type, stringKeys);
	}

	int depth = 0;
	
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> decodeMap(byte type, boolean stringKeys)
			throws IOException {
		Map<K, V> map = null;

		boolean root = false;
		if (depth==0) {
			root = true;
		}	
		depth++;

		if (type != BinaryEncoder.MAP) {
			throw new IOException("Expecting a MAP but got a " + type);
		}
		int size = decodeInteger();
		if (size >= 1) {
			Object key = decodeValue();
			if (key instanceof Number && stringKeys) {
				Map<java.lang.Number, Object> sequenceMap = decodeStringKeyMapWithNumberKey(
						Types.toLong(key), size);
				map = new HashMap<K, V>(size);
				registerMap(sequenceMap, map);
			} else {
				map = doDecodeMap((K) key, size);
			}
		} else {
			map = new HashMap<K, V>();
		}
		if (root) {
			byte typeMapKeys = input.readByte();
			if (typeMapKeys != BinaryEncoder.MAP_KEYS) {
				throw new RuntimeException("Expecting map keys, but got "
						+ typeMapKeys);
			}
			int sizeMapKeys = decodeInteger();

			Map<Long, String> keyToNum = new HashMap<Long, String>();

			for (int index = 0; index < sizeMapKeys; index++) {
				Long longKey = decodeLong();
				String actualKey = decodeString();
				keyToNum.put(longKey, actualKey);
			}

			for (MapMap mapMap : mappings) {
				for (Number number : mapMap.sequenceMap.keySet()) {
					String key = keyToNum.get(number);
					mapMap.map.put(key, mapMap.sequenceMap.get(number));
				}
			}
		}
		return map;

	}

	@SuppressWarnings("unchecked")
	private <K, V> Map<K, V> doDecodeMap(K key, int size) throws IOException {
		Map<K, V> map = new HashMap<K, V>(size);
		V value = (V) decodeValue();
		map.put(key, value);

		for (int index = 1; index < size; index++) {
			key = (K) decodeValue();
			value = (V) decodeValue();
			map.put(key, value);
		}
		return map;

	}

	@SuppressWarnings("unchecked")
	private <V> Map<Number, V> decodeStringKeyMapWithNumberKey(Number key,
			int size) throws IOException {
		Map<Number, V> map = new HashMap<Number, V>(size);
		V value = (V) decodeValue();
		map.put(key, value);

		for (int index = 1; index < size; index++) {
			key =  decodeLong();
			value = (V) decodeValue();
			map.put(key, value);
		}

		return map;
	}

	public Date decodeDate() throws IOException {
		byte type = input.readByte();
		return decodeDate(type);
	}		
	
	public Date decodeDate(byte type) throws IOException {
		int year = 0;
		int month = 0;
		int day = 0;
		int hour = 0;
		int minute = 0;
		int second = 0;
		int milisecond = 0;
		// int tz_seconds = offset / 1000;
		int tz_minutes = 0;

		switch (type) {
		case BinaryEncoder.DATE_YEAR_ONLY:
			year = input.readShort();
			break;
		case BinaryEncoder.DATE_YEAR_MONTH_ONLY:
			year = input.readShort();
			month = input.readByte();
			break;
		case BinaryEncoder.DATE_YEAR_MONTH_DAY_ONLY:
			year = input.readShort();
			month = input.readByte();
			day = input.readByte();
			break;
		case BinaryEncoder.DATE_YEAR_MONTH_DAY_HOUR_ONLY:
			year = input.readShort();
			month = input.readByte();
			day = input.readByte();
			hour = input.readByte();
			break;
		case BinaryEncoder.DATE_YEAR_MONTH_DAY_HOUR_MINUTE_ONLY:
			year = input.readShort();
			month = input.readByte();
			day = input.readByte();
			hour = input.readByte();
			minute = input.readByte();
			break;
		case BinaryEncoder.DATE_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_ONLY:
			year = input.readShort();
			month = input.readByte();
			day = input.readByte();
			hour = input.readByte();
			minute = input.readByte();
			second = input.readByte();
			break;
		case BinaryEncoder.DATE_ALL:
			year = input.readShort();
			month = input.readByte();
			day = input.readByte();
			hour = input.readByte();
			minute = input.readByte();
			second = input.readByte();
			milisecond = input.readShort();

			break;
		case BinaryEncoder.DATE_YEAR_MONTH_DAY_HOUR_TZ_ONLY:
			year = input.readShort();
			month = input.readByte();
			day = input.readByte();
			hour = input.readByte();
			tz_minutes = input.readShort();
			break;
		case BinaryEncoder.DATE_YEAR_MONTH_DAY_HOUR_MINUTE_TZ_ONLY:
			year = input.readShort();
			month = input.readByte();
			day = input.readByte();
			hour = input.readByte();
			minute = input.readByte();
			tz_minutes = input.readShort();
			break;
		case BinaryEncoder.DATE_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_TZ_ONLY:
			year = input.readShort();
			month = input.readByte();
			day = input.readByte();
			hour = input.readByte();
			minute = input.readByte();
			second = input.readByte();
			tz_minutes = input.readShort();
			break;
		case BinaryEncoder.DATE_ALL_TZ:
			year = input.readShort();
			month = input.readByte();
			day = input.readByte();
			hour = input.readByte();
			minute = input.readByte();
			second = input.readByte();
			milisecond = input.readShort();
			tz_minutes = input.readShort();
			break;
		}
		day = day + 1;

		TimeZone tz = null;

		if (tz_minutes != 0) {
			boolean negative = false;
			if (tz_minutes < 0) {
				negative = true;
			}

			int tz_hours = tz_minutes / 60;
			tz_minutes = tz_minutes % 60;
			tz = TimeZone.getTimeZone(String.format("GMT %s %s : %s",
					negative ? "-" : "+", java.lang.Math.abs(tz_hours),
					java.lang.Math.abs(tz_minutes)));
		}

		Calendar calendar = null;

		if (tz == null) {
			calendar = Calendar.getInstance();
		} else {
			calendar = Calendar.getInstance();
		}
		
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, milisecond);

		return calendar.getTime();
	}

}
