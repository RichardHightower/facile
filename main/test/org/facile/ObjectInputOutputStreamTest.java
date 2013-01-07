package org.facile;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.facile.ObjectInputStream;
import org.facile.ObjectOutputStream;
import org.facile.lang.Unsigned3ByteInteger;
import org.facile.lang.Unsigned5ByteInteger;
import org.facile.lang.Unsigned6ByteInteger;
import org.facile.lang.Unsigned7ByteInteger;
import org.facile.lang.UnsignedByte;
import org.facile.lang.UnsignedInteger;
import org.junit.Before;
import org.junit.Test;

import static org.facile.Facile.*;

public class ObjectInputOutputStreamTest {
	private ByteArrayInputStream bis;
	private ByteArrayOutputStream bos;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private byte[] byteArray;
	
	private static final double DELTA = 1e-15;
	private static final double F_DELTA = 1e-6;
	
	@Before
	public void setup() {
		bos = new ByteArrayOutputStream();
		output = new ObjectOutputStream(bos);
	}
	
	private void prepareInput() {
		byteArray = bos.toByteArray();
		bis = new ByteArrayInputStream(byteArray);
		input = new ObjectInputStream(bis);
	}


	public void testEasyByte(byte b) throws IOException {
	
		this.setup();
		output.writeByte(b);
	
		
		prepareInput();
	
		assertEquals(1, byteArray.length);
		byte b2 = input.readByte();
		
		assertEquals(b, b2);
	}
	
	
	@Test
	public void testEasyByte() throws IOException {
		byte[] bytes = {0, 1, 2, 127};
		
		for (byte b: bytes) {
			testEasyByte(b);
		}
	}
	
	
	public void testHardByte(byte b) throws IOException {
		
		this.setup();
		output.writeByte(b);
		
		prepareInput();
		
		assertEquals(2, byteArray.length);
		
		
		byte b2 = input.readByte();
		
		assertEquals(b, b2);
	}



	@Test
	public void testHardByte() throws IOException {
		byte[] bytes = {-51,-60,-70};
		
		for (byte b: bytes) {
			testHardByte(b);
		}
	}
	
	
	public void testLong(long l, int expectedLength) throws IOException {
		
		this.setup();
		output.writeLong(l);
		
		prepareInput();
		
		assertEquals(expectedLength, byteArray.length);
		
		long l2 = input.readLong();
		
		assertEquals(l, l2);
	}

	public void testInt(int i, int expectedLength) throws IOException {
		
		this.setup();
		output.writeInt(i);
		
		prepareInput();
		
		assertEquals(expectedLength, byteArray.length);
		
		long i2 = input.readInt();
		
		assertEquals(i, i2);
	}

	@Test
	public void testWriteUnsignedByte() throws IOException {
		output.writeUnsignedByte((short)0xFF);
		prepareInput();
		byte b = input.readByte();
		
		int int1 = UnsignedByte.unsignedByteToInt(b);
		
		assertEquals(0xFF, int1);

		
		short short1 = UnsignedByte.unsignedByteToShort(b);		
		assertEquals((short)0xFF, short1);
		
		
		prepareInput();
		assertEquals((short)0xFF, input.readUnsignedByte());
		

	}
	

	@Test
	public void testByte() throws IOException {
		output.writeByte('a');
		prepareInput();
		char a = (char)input.readByte();
		assertEquals('a', a);

		setup();
		output.writeByte(-49);
		prepareInput();
		byte n50 = input.readByte();
		assertEquals(-49, n50);


	}


	@Test
	public void test1ByteLong() throws IOException {
		long[] one_byte_long = {
				1_000_000, 
				10_000_000, 100_000_000, 
				1_000_000_000, 1_000_000_000_000l,
				128, -100, 
				256, 512, 1024, 2048, 4096, 8192, 1000};
		
		for (long l: one_byte_long) {
			testLong(l, 1);
		}
	}


	@Test
	public void test1ByteInt() throws IOException {
		int[] one_byte_int = {
				1_000_000, 
				10_000_000, 100_000_000, 
				1_000_000_000,
				128, -100, 
				256, 512, 1024, 2048, 4096, 8192, 1000};
		
		for (int i: one_byte_int) {
			testInt(i, 1);
		}
	}


	@Test
	public void test2ByteLong() throws IOException {
		long[] two_byte_long = {-51, -128, UnsignedByte.MAX_NEGATIVE};
		
		for (long l: two_byte_long) {
			testLong(l, 2);
		}
	}

	@Test
	public void test3ByteLong() throws IOException {
		//Character.MAX_VALUE
		long[] byte_long = {Character.MAX_VALUE, Character.MAX_VALUE * -1l, Short.MIN_VALUE, Short.MIN_VALUE+1};
		
		for (long l: byte_long) {
			testLong(l, 3);
		}
	}


	@Test
	public void test4ByteLong() throws IOException {
		long[] byte_long = {Unsigned3ByteInteger.MAX_VALUE, Unsigned3ByteInteger.MAX_NEGATIVE, 
								Unsigned3ByteInteger.MAX_NEGATIVE+1, Unsigned3ByteInteger.MAX_VALUE-1,
								Character.MAX_VALUE + 1, (Character.MAX_VALUE * -1l) -1};
		
		for (long l: byte_long) {
			testLong(l, 4);
		}
	}


	@Test
	public void test5ByteLong() throws IOException {
		long[] byte_long = {Integer.MIN_VALUE, Integer.MIN_VALUE+1, 
							Integer.MAX_VALUE, Integer.MAX_VALUE-1, UnsignedInteger.MAX_NEGATIVE,
							Unsigned3ByteInteger.MAX_NEGATIVE-1, Unsigned3ByteInteger.MAX_VALUE+1};

		for (long l: byte_long) {
			testLong(l, 5);
		}
	}

	@Test
	public void test6ByteLong() throws IOException {
		long[] byte_long = {Unsigned5ByteInteger.MAX_NEGATIVE+1, Unsigned5ByteInteger.MAX_VALUE-1};
		
		for (long l: byte_long) {
			testLong(l, 6);
		}
	}

	@Test
	public void test7ByteLong() throws IOException {
		long[] byte_long = {Unsigned6ByteInteger.MAX_NEGATIVE+1, Unsigned6ByteInteger.MAX_VALUE-1};
		
		for (long l: byte_long) {
			testLong(l, 7);
		}
	}

	

	@Test
	public void test8ByteLong() throws IOException {
		long[] byte_long = {Unsigned7ByteInteger.MAX_NEGATIVE+1, Unsigned7ByteInteger.MAX_VALUE-1};
		
		for (long l: byte_long) {
			testLong(l, 8);
		}
	}


	@Test
	public void test9ByteLong() throws IOException {
		long[] byte_long = {Long.MAX_VALUE, Long.MIN_VALUE};
		
		for (long l: byte_long) {
			testLong(l, 9);
		}
	}
	
	@Test
	public void testWriteString() throws IOException {
		
		String str = "Hello Cruel World";
		this.output.writeUTF(str);
		
		prepareInput();
		String str2 = input.readUTF();
		
		assertEquals(str, str2);
	}

	
	@Test
	public void testWriteBoolean() throws IOException {
		
		boolean bool =  true;
		output.writeBoolean(bool);
		prepareInput();
		assertEquals(1, byteArray.length);
		boolean b1 = input.readBoolean();
		
		assertEquals(bool, b1);
		
		setup();
		
		bool =  false;
		output.writeBoolean(bool);
		prepareInput();
		assertEquals(1, byteArray.length);
		b1 = input.readBoolean();
		
		assertEquals(bool, b1);
		
		
		try {
			setup();
			output.writeByte(10);
			prepareInput();
			b1 = input.readBoolean();		
		} catch (IOException ex) {
			assertTrue(true);
			return;
		}
		assertTrue(false);


	}

	@Test
	public void testWriteShort() throws IOException {
		
		short sh = 77;
		output.writeShort(sh);
		prepareInput();
		assertEquals(1, byteArray.length);
		short s1 = input.readShort();
		
		assertEquals(sh, s1);
	}
	
	
	@Test
	public void testWriteByte() throws IOException {
		
		byte b = 77;
		output.writeByte(b);
		prepareInput();
		assertEquals(1, byteArray.length);
		byte b1 = input.readByte();
		
		assertEquals(b, b1);
	}
	
	
	
	@Test
	public void testWriteDoubleArray() throws IOException {
		double [] array = {1.7d, 2d, 3d, 1.1d, 1.123456789d, Float.MAX_VALUE + 10.0d, 1.0d + Long.MAX_VALUE, 12345678910.12345d, 0.0, 1.0, 10.0, 100.0};
		output.writeArray(array);
		
		prepareInput();
		
		double [] array2 = input.readDoubleArray();
		
		int index = 0;
		for (double v : array) {
			double v2 = array2[index];
			assertEquals(v, v2, DELTA);
			index++;
		}
	}

	@Test
	public void testWriteFloatArray() throws IOException {
		
		
		float [] array = {3f, 5f, 1.1f, 1.9f, 2f, 3f, 1.3f, Float.MAX_VALUE, 1.0f, 0.0f, 10.0f, 100.0f};
		output.writeArray(array);
		
		prepareInput();
		
		float [] array2 = input.readFloatArray();
		
		int index = 0;
		for (float v : array) {
			float v2 = array2[index];
			assertEquals(v, v2, F_DELTA);
			index++;
		}
	}


	@Test
	public void testWriteShortArray() throws IOException {
		
		
		short [] array = {1, 2, 3, 4, 5, 6, (short)Character.MAX_VALUE, Short.MAX_VALUE, Short.MIN_VALUE, UnsignedByte.MAX_VALUE};
		output.writeArray(array);
		
		prepareInput();
		
		short [] array2 = input.readShortArray();
		
		int index = 0;
		for (short v : array) {
			short v2 = array2[index];
			assertEquals(v, v2, F_DELTA);
			index++;
		}
	}

	@Test
	public void testWriteIntArray() throws IOException {
		
		
		int [] array = {1, 2, 3, 4, 5, 6, (short)Character.MAX_VALUE, Short.MAX_VALUE, Short.MIN_VALUE};
		output.writeArray(array);
		
		prepareInput();
		
		int [] array2 = input.readIntArray();
		
		int index = 0;
		for (int v : array) {
			int v2 = array2[index];
			assertEquals(v, v2);
			index++;
		}
	}

	@Test
	public void testWriteLongArray() throws IOException {
		
		
		long [] array = {1, 2, 3, 4, 5, 6, Long.MAX_VALUE, Long.MIN_VALUE};
		output.writeArray(array);
		
		prepareInput();
		
		long [] array2 = input.readLongArray();
		
		int index = 0;
		for (long v : array) {
			long v2 = array2[index];
			assertEquals(v, v2);
			index++;
		}
	}
	

	@Test
	public void testWriteCharArray() throws IOException {
		
		
		char [] array = {'a', 'b', 'c', Character.MAX_VALUE, Character.MIN_VALUE, Byte.MAX_VALUE};
		output.writeArray(array);
		
		prepareInput();
		
		char [] array2 = input.readCharArray();
		
		int index = 0;
		for (long v : array) {
			long v2 = array2[index];
			assertEquals(v, v2);
			index++;
		}
	}


	@Test
	public void testWriteByteArray() throws IOException {
		
		
		byte [] array = {'a', 'b', 'c'};
		output.writeArray(array);
		
		prepareInput();
		
		byte [] array2 = input.readByteArray();
		
		int index = 0;
		for (long v : array) {
			long v2 = array2[index];
			assertEquals(v, v2);
			index++;
		}
	}

	
	@Test
	public void unsignedByteTest() throws IOException {
		output.writeUnsignedByte(UnsignedByte.MAX_VALUE);
		prepareInput();
		int c = input.readInt();
		assertEquals(UnsignedByte.MAX_VALUE, c);
	}
	
	@Test
	public void testRandom1() throws IOException {
		
		output.writeChar('a');
		output.writeInt(99);
		prepareInput();
		char a = input.readChar();
		assertEquals('a', a);
		int b = input.readInt();
		assertEquals(99, b);
	}

	@Test
	public void testRandom2() throws IOException {
		output.write(1);
		output.writeFloat(1.9f);
		output.writeDouble(1.9d);
		prepareInput();


		byte a = input.readByte();
		assertEquals(1, a);
		float e = input.readFloat();
		assertEquals(1.9f, e, F_DELTA);
		double f = input.readDouble();
		assertEquals(1.9d, f, DELTA);
	}
	
	@Test
	public void testRandom3() throws IOException {
		output.writeUnsignedInt(UnsignedInteger.MAX_VALUE);
		prepareInput();
		long d = input.readLong();
		assertEquals(UnsignedInteger.MAX_VALUE, d);
		
	}

	
	@Test
	public void testRandomStuff() throws IOException {
		output.writeBytes("a");
		output.writeChars("b");
		output.writeChars("c");
		output.write(new byte[]{1,2,3});
		output.flush();
		output.close();
		prepareInput();
		String sa = input.readUTF();
		assertEquals("a", sa);
		String sb = input.readUTF();
		assertEquals("b", sb);
		String sc = input.readUTF();
		assertEquals("c", sc);
		byte[] bytes = input.readByteArray();
		assertEquals(1, bytes[0]);
		assertEquals(2, bytes[1]);
		assertEquals(3, bytes[2]);
		assertEquals(3, bytes.length);
		


	}

	@Test
	public void testBasicMap() throws IOException {
		Map<String, ? extends Object> mp = mp("k1", "a", "k2", "b");
		output.writeMap(mp);
		
		prepareInput();
		Map<String, ? extends Object> mp2 = input.readMap();
		
		assertEquals("a", mp2.get("k1"));
		assertEquals("b", mp2.get("k2"));

	}
	

	@Test
	public void testDifferentMap() throws IOException {
		Map<Number, ? extends Object> mp = mp(toWrapper(1), "value1", toWrapper(2), "value2");
		output.writeMap(mp);
		
		prepareInput();
		Map<Number, ? extends Object> mp2 = input.readMap(false);
		
		assertEquals(mp.get(toWrapper(1)), mp2.get(toWrapper(1)));
		assertEquals(mp.get(toWrapper(2)), mp2.get(toWrapper(2)));

	}
	
	
	@Test
	public void testNestedMap() throws IOException {
		Map<String, ? extends Object> mp = mp("key1", mp("key1_1", "value1_1"));
		output.writeMap(mp);
		
		prepareInput();
		Map<String, ? extends Object> mp2 = input.readMap();
		
		assertEquals(mp.get("key1"), mp2.get("key1"));
		
		assertEquals(get(string, mp.get("key1"), "key1_1"), get(string, mp2.get("key1"), "key1_1"));

	}

	@Test
	public void testNestedReadObjectMap() throws IOException {
		Map<String, ? extends Object> mp = mp("key1", mp("key1_1", "value1_1", "ivalue", 1, "lvalue", 2L, "dvalue", new Date()));
		output.writeMap(mp);
		
		prepareInput();
		Map<String, ? extends Object> mp2 = input.readMap();
		
		//assertEquals(mp.get("key1"), mp2.get("key1"));
		
		//assertEquals(get(string, mp.get("key1"), "key1_1"), get(string, mp2.get("key1"), "key1_1"));
		
		int ivalue = get(integer, mp.get("key1"), "ivalue");
		int ivalue2 = get(integer, mp2.get("key1"), "ivalue");
		
		assertEquals(ivalue, ivalue2);
		
		long lvalue = get(lng, mp.get("key1"), "lvalue");
		long lvalue2 = get(lng, mp2.get("key1"), "lvalue");

		assertEquals(lvalue, lvalue2);

		Date dvalue = get(date, mp.get("key1"), "dvalue");
		Date dvalue2 = get(date, mp2.get("key1"), "dvalue");

		assertEquals(dvalue, dvalue2);

	}

	@Test
	public void testBugWithMapAndDate() throws IOException {
		Date d1 = new Date();
		Map<String, ? extends Object> mp = mp("k1", d1);
		output.writeMap(mp);
		
		prepareInput();
		Map<String, ? extends Object> mp2 = input.readMap();
		
		@SuppressWarnings("rawtypes")
		Date d2 = get(date, (Map)mp2, "k1");

		assertEquals(d1, d2);

	}

	@Test
	public void testDate() throws IOException {
		Date date = new Date();
		output.writeDate(date, true);
		prepareInput();
		Date date2 = input.readDate();
		
		assertEquals(date.getTime(), date2.getTime());
		
		assertEquals(date, date2);

	}
	
	@Test
	public void testDate2() throws IOException, ClassNotFoundException {
		Date date = new Date();
		output.writeObject(date);
		prepareInput();
		Date date2 = (Date) input.readObject();
		
		assertEquals(date.getTime(), date2.getTime());
		
		assertEquals(date, date2);

	}

	
	@Test
	public void testDateNoTZ() throws IOException {
		Date date = new Date();
		output.writeDate(date);
		prepareInput();
		Date date2 = input.readDate();
		
		assertEquals(date.getTime(), date2.getTime());
		
		assertEquals(date, date2);

	}

	
	@Test
	public void testDateNoMilis() throws IOException {
		Calendar calendar = Calendar.getInstance();
		
		Date date = new Date();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		date = calendar.getTime();
		output.writeDate(date);
		prepareInput();
		Date date2 = input.readDate();
		
		assertEquals(date.getTime(), date2.getTime());
		
		assertEquals(date, date2);

	}


	@Test
	public void testDateNoSeconds() throws IOException {
		Calendar calendar = Calendar.getInstance();
		
		Date date = new Date();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);

		date = calendar.getTime();
		output.writeDate(date);
		prepareInput();
		Date date2 = input.readDate();
		
		assertEquals(date.getTime(), date2.getTime());
		
		assertEquals(date, date2);

	}

	@Test
	public void testDateNoMinutes() throws IOException {
		Calendar calendar = Calendar.getInstance();
		
		Date date = new Date();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);

		date = calendar.getTime();
		output.writeDate(date);
		prepareInput();
		Date date2 = input.readDate();
		
		assertEquals(date.getTime(), date2.getTime());
		
		assertEquals(date, date2);

	}


	@Test
	public void testDateNoHours() throws IOException {
		Calendar calendar = Calendar.getInstance();
		
		Date date = new Date();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);

		date = calendar.getTime();
		output.writeDate(date);
		prepareInput();
		Date date2 = input.readDate();
		
		assertEquals(date.getTime(), date2.getTime());
		
		assertEquals(date, date2);

	}

	@Test
	public void testDateNoDay() throws IOException {
		Calendar calendar = Calendar.getInstance();
		
		Date date = new Date();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		date = calendar.getTime();
		output.writeDate(date);
		prepareInput();
		Date date2 = input.readDate();
		
		assertEquals(date.getTime(), date2.getTime());
		
		assertEquals(date, date2);

	}

	@Test
	public void testDateNoMonth() throws IOException {
		Calendar calendar = Calendar.getInstance();
		
		Date date = new Date();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 0);

		date = calendar.getTime();
		output.writeDate(date);
		prepareInput();
		Date date2 = input.readDate();
		
		assertEquals(date.getTime(), date2.getTime());
		
		assertEquals(date, date2);

	}

	
	
	@Test
	public void testDateNoMilisTZ() throws IOException {
		Calendar calendar = Calendar.getInstance();
		
		Date date = new Date();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		date = calendar.getTime();
		output.writeDate(date, true);
		prepareInput();
		Date date2 = input.readDate();
		
		assertEquals(date.getTime(), date2.getTime());
		
		assertEquals(date, date2);

	}


	@Test
	public void testDateNoSecondsTZ() throws IOException {
		Calendar calendar = Calendar.getInstance();
		
		Date date = new Date();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);

		date = calendar.getTime();
		output.writeDate(date, true);
		prepareInput();
		Date date2 = input.readDate();
		
		assertEquals(date.getTime(), date2.getTime());
		
		assertEquals(date, date2);

	}

	@Test
	public void testDateNoMinutesTZ() throws IOException {
		Calendar calendar = Calendar.getInstance();
		
		Date date = new Date();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);

		date = calendar.getTime();
		output.writeDate(date, true);
		prepareInput();
		Date date2 = input.readDate();
		
		assertEquals(date.getTime(), date2.getTime());
		
		assertEquals(date, date2);

	}


	@Test
	public void testDateNoHoursTZ() throws IOException {
		Calendar calendar = Calendar.getInstance();
		
		Date date = new Date();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);

		date = calendar.getTime();
		output.writeDate(date, true);
		prepareInput();
		Date date2 = input.readDate();
		
		assertEquals(date.getTime(), date2.getTime());
		
		assertEquals(date, date2);

	}

	@Test
	public void testDateNoDayTZ() throws IOException {
		Calendar calendar = Calendar.getInstance();
		
		Date date = new Date();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		date = calendar.getTime();
		output.writeDate(date, true);
		prepareInput();
		Date date2 = input.readDate();
		
		assertEquals(date.getTime(), date2.getTime());
		
		assertEquals(date, date2);

	}

	@Test
	public void testDateNoMonthTZ() throws IOException {
		Calendar calendar = Calendar.getInstance();
		
		Date date = new Date();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 0);

		date = calendar.getTime();
		output.writeDate(date, true);
		prepareInput();
		Date date2 = input.readDate();
		
		assertEquals(date.getTime(), date2.getTime());
		
		assertEquals(date, date2);

	}
	
	@Test
	public void testBigDecimal() throws IOException, ClassNotFoundException {
		BigDecimal bd1 = new BigDecimal("9223372036854775807.99999");
		BigDecimal bd2 = new BigDecimal("1");

		output.writeObject(bd1);
		output.writeObject(bd2);
		prepareInput();
		BigDecimal test1 = (BigDecimal) input.readObject();
		Number test2 = (Number) input.readObject();
		
		assertEquals(bd1, test1);
		assertEquals(bd2.longValue(), test2.longValue());

	}

	
	@Test
	public void testBigInt() throws IOException, ClassNotFoundException {
		BigInteger bi1 = new BigInteger("922337203685477580799999");
		BigInteger bi2 = new BigInteger("1");

		output.writeObject(bi1);
		output.writeObject(bi2);
		prepareInput();
		BigInteger test1 = (BigInteger) input.readObject();
		Number test2 = (Number) input.readObject();
		
		assertEquals(bi1, test1);
		assertEquals(bi2.longValue(), test2.longValue());

	}
	
	
	@Test
	public void testStringBuilder() throws IOException, ClassNotFoundException {
		
		StringBuilder builder = new StringBuilder();
		builder.append("hello");

		output.writeObject(builder);
		prepareInput();
		String str = (String) input.readObject();
		assertEquals("hello", str);

	}

	@Test
	public void testCalendar() throws IOException, ClassNotFoundException {
		Calendar cal = Calendar.getInstance();
		
		output.writeObject(cal);
		prepareInput();
		Date date = (Date) input.readObject();
		assertEquals(cal.getTime(), date);

	}

	public static class Person  {
		@SuppressWarnings("unused")
		private String name = "Human";

	}
	
	public static class Employee extends Person {
		private String name = "Rick";
		private int age = 100;
		private Employee boss;
		private List<Employee> emps;

		int [] nums = {1,2,3,4};
		Integer [] nums2 = {1,2,3,4};
		List <Integer> nums3 = new ArrayList<>();
		{
			for (Integer num : nums2) {
				nums3.add(num);
			}
		}
		@Override
		public String toString() {
			return "Employee [name=" + name + ", age=" + age + ", boss=" + boss
					+ ", emps=" + emps + ", nums=" + Arrays.toString(nums)
					+ ", nums2=" + Arrays.toString(nums2) + ", nums3=" + nums3
					+ "]";
		}
		
		

	}
	
	public static class Class1  {
		private int num = 0;
		private Class1 parent = null;
		private List<Class1> children;

	}
	
	//Todo write a test for writing an object array.
	//Todo notices that shorts were getting written out and not getting resized to byte
	
	@Test
	public void smallTest() throws IOException, ClassNotFoundException {
		Class1 cls1 = new Class1();
		cls1.parent = new Class1();
		cls1.children = ls(new Class1());
		cls1.children.get(0).num=33;
		cls1.num=77;
		cls1.parent.num=99;
		output.writeObject(cls1);
		prepareInput();
		Class1 class1 = (Class1) input.readObject();

		assertEquals(class1.num, 77);
		assertEquals(class1.parent.num, 99);
		assertEquals(class1.children.get(0).num, 33);

	}

	@Test
	public void testInstance() throws IOException, ClassNotFoundException {
		
		Map<String, Object> mp = mp(
				"name", "Sam", 
				"nums", ilist(9,10,11),
				"nums3", new int[] {1, 2, 3, 4, 5},
				"nums2", new Integer[]{12,13,14},
				"class", "org.facile.ObjectInputOutputStreamTest$Employee",
				"age", (short)26,
				"boss", new Employee(),
				"emps", ls(new Employee(), new Employee(), new Employee())
				);
		Employee employeeOrginal = fromMap(mp, Employee.class);

		output.writeObject(employeeOrginal);
		prepareInput();
		Employee employee = (Employee) input.readObject();

		System.out.println(employee);
		assertEquals("Sam", employee.name);
		assertEquals(3, employee.nums.length);
		assertEquals(9, employee.nums[0]);
		assertEquals(11, employee.nums[2]);
		assertEquals(13, employee.nums2[1].intValue());
		assertEquals(26, employee.age);
		assertEquals("Rick", employee.boss.name); 
		assertEquals(100, employee.boss.age); 

		assertEquals("Rick", employee.emps.get(0).name);
		assertEquals(5, employee.nums3.size());
		assertEquals(5, idx(employee.nums3, 4).intValue());

	}

	@Test
	public void test() throws IOException {
		System.out.println("UI M    " + UnsignedInteger.MAX_VALUE);
		System.out.println("UI 3B M " + Unsigned3ByteInteger.MAX_VALUE);
		System.out.println("UI 5B M " + Unsigned5ByteInteger.MAX_VALUE);
		
	}
	
}
