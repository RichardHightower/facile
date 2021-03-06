package org.facile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import static org.facile.Facile.*;

public class Types {
	static Class<Types> types = Types.class;

	private static final Logger log = log(types);

	public static int toInt(byte[] bytes, int offset) {

		ByteArrayInputStream bis = new ByteArrayInputStream(bytes, offset,
				bytes.length);
		DataInputStream instream = new DataInputStream(bis);
		try {
			return instream.readInt();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public static int toInt(byte[] bytes) {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		DataInputStream instream = new DataInputStream(bis);
		try {
			return instream.readInt();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static int toInt(Object obj) {
		try {
			if (obj instanceof Number) {
				return ((Number) obj).intValue();
			} else if (obj instanceof CharSequence) {
				try {
					return Integer.parseInt(((CharSequence) obj).toString());
				} catch (Exception ex) {
					char[] chars = chars(obj);
					Appendable buf = buf(chars.length);
					boolean found = false;
					for (char c : chars) {
						if (Character.isDigit(c) && !found) {
							found = true;
							add(buf, c);
						} else if (Character.isDigit(c) && found) {
							add(buf, c);
						} else if (!Character.isDigit(c) && found) {
						}
					}
					try {
						if (len(buf) > 0) {
							return Integer.parseInt(str(buf));
						}
					} catch (Exception ex2) {
						warning(log,
								ex,
								"unable to convert to int and there was an exception %s",
								ex.getMessage());
					}
				}
			} else {
			}
		} catch (Exception ex) {
			warning(log, ex,
					"unable to convert to int and there was an exception %s",
					ex.getMessage());
		}
		die("Unable to convert %s to a int", obj.getClass());
		return -666; // die throws an exception

	}

	public static byte toByte(Object obj) {
		try {
			if (obj instanceof Number) {
				return ((Number) obj).byteValue();
			} else if (obj instanceof CharSequence) {
				try {
					return Byte.parseByte(((CharSequence) obj).toString());
				} catch (Exception ex) {
					char[] chars = chars(obj);
					Appendable buf = buf(chars.length);
					boolean found = false;
					for (char c : chars) {
						if (Character.isDigit(c) && !found) {
							found = true;
							add(buf, c);
						} else if (Character.isDigit(c) && found) {
							add(buf, c);
						} else if (!Character.isDigit(c) && found) {
						}
					}
					try {
						if (len(buf) > 0) {
							return Byte.parseByte(str(buf));
						}
					} catch (Exception ex2) {
						warning(log,
								ex,
								"unable to convert to byte and there was an exception %s",
								ex.getMessage());
					}
				}
			} else {
			}
		} catch (Exception ex) {
			warning(log, ex,
					"unable to convert to byte and there was an exception %s",
					ex.getMessage());
		}
		die("Unable to convert %s to a byte", obj.getClass());
		return -66; // die throws an exception

	}

	public static short toShort(Object obj) {
		try {
			if (obj instanceof Number) {
				return ((Number) obj).shortValue();
			} else if (obj instanceof CharSequence) {
				try {
					return Short.parseShort(((CharSequence) obj).toString());
				} catch (Exception ex) {
					char[] chars = chars(obj);
					Appendable buf = buf(chars.length);
					boolean found = false;
					for (char c : chars) {
						if (Character.isDigit(c) && !found) {
							found = true;
							add(buf, c);
						} else if (Character.isDigit(c) && found) {
							add(buf, c);
						} else if (!Character.isDigit(c) && found) {
						}
					}
					try {
						if (len(buf) > 0) {
							return Short.parseShort(str(buf));
						}
					} catch (Exception ex2) {
						warning(log,
								ex,
								"unable to convert to short and there was an exception %s",
								ex.getMessage());
					}
				}
			} else {
			}
		} catch (Exception ex) {
			warning(log, ex,
					"unable to convert to byte and there was an exception %s",
					ex.getMessage());
		}
		die("Unable to convert %s to a short", obj.getClass());
		return -66; // die throws an exception

	}

	public static char toChar(Object obj) {
		try {
			if (obj instanceof Character) {
				return ((Character) obj).charValue();
			} else if (obj instanceof CharSequence) {
				obj.toString().charAt(0);
			} else {
			}
		} catch (Exception ex) {
			warning(log, ex,
					"unable to convert to byte and there was an exception %s",
					ex.getMessage());
		}
		die("Unable to convert %s to a byte", obj.getClass());
		return 'Z'; // die throws an exception

	}

	public static long toLong(Object obj) {
		try {
			if (obj instanceof Number) {
				return ((Number) obj).longValue();
			} else if (obj instanceof CharSequence) {
				try {
					return Long.parseLong(((CharSequence) obj).toString());
				} catch (Exception ex) {
					char[] chars = chars(obj);
					Appendable buf = buf(chars.length);
					boolean found = false;
					for (char c : chars) {
						if (Character.isDigit(c) && !found) {
							found = true;
							add(buf, c);
						} else if (Character.isDigit(c) && found) {
							add(buf, c);
						} else if (!Character.isDigit(c) && found) {
						}
					}
					try {
						if (len(buf) > 0) {
							return Long.parseLong(str(buf));
						}
					} catch (Exception ex2) {
						warning(log,
								ex,
								"unable to convert to long and there was an exception %s",
								ex.getMessage());

					}
				}
			} else {
			}
		} catch (Exception ex) {
			warning(log, ex,
					"unable to convert to long and there was an exception %s",
					ex.getMessage());

		}

		die("Unable to convert %s %s to a long", obj, obj.getClass());
		return -666; // die throws an exception

	}

	public static boolean toBoolean(Object obj) {

		Set<String> trueSet = set("t", "true", "True", "y", "yes", "1", "aye",
				"ofcourse", "T", "TRUE", "ok");
		if (obj instanceof String || obj instanceof CharSequence
				|| obj.getClass() == char[].class) {
			String str = str(obj);
			if (str.length() == 0) {
				return false;
			} else {
				return isIn(str, trueSet);
			}
		} else if (obj instanceof Boolean) {
			return ((Boolean) obj).booleanValue();
		} else if (isArray(obj) || obj instanceof Collection) {
			return len(obj) > 0;
		} else {
			return toBoolean(str(obj));
		}
	}

	public static double toDouble(Object obj) {
		try {
			if (obj instanceof Double) {
				return (Double) obj;
			} else if (obj instanceof Number) {
				return ((Number) obj).doubleValue();
			} else if (obj instanceof CharSequence) {
				try {
					return Double.parseDouble(((CharSequence) obj).toString());
				} catch (Exception ex) {
					String svalue = str(obj);
					Matcher re = Regex.re(
							"[-+]?[0-9]+\\.?[0-9]+([eE][-+]?[0-9]+)?", svalue);
					if (re.find()) {
						svalue = re.group(0);
						return Double.parseDouble(svalue);
					}
					warning(log, "unable to convert to double after regex");
					return Double.NaN;
				}
			} else {
			}
		} catch (Exception ex) {
			warning(log,
					ex,
					"unable to convert to double and there was an exception %s",
					ex.getMessage());
		}

		die("Unable to convert %s to a double", obj.getClass());
		return -666d; // die throws an exception

	}

	public static float toFloat(Object obj) {
		try {
			if (obj instanceof Float) {
				return (Float) obj;
			} else if (obj instanceof Number) {
				return ((Number) obj).floatValue();
			} else if (obj instanceof CharSequence) {
				try {
					return Float.parseFloat(((CharSequence) obj).toString());
				} catch (Exception ex) {
					String svalue = str(obj);
					Matcher re = Regex.re(
							"[-+]?[0-9]+\\.?[0-9]+([eE][-+]?[0-9]+)?", svalue);
					if (re.find()) {
						svalue = re.group(0);
						return Float.parseFloat(svalue);
					}
					warning(log, "unable to convert to float after regex");
					return Float.NaN;
				}
			} else {
			}
		} catch (Exception ex) {
			warning(log,
					"unable to convert to float and there was an exception %s",
					ex.getMessage());
		}

		die("Unable to convert %s to a float", obj.getClass());
		return -666f; // die throws an exception

	}

	@SuppressWarnings("unchecked")
	public static <T> T coerce(Class<T> clz, Object value) {
		if (clz == integer || clz == pint) {
			Integer i = toInt(value);
			return (T) i;
		} else if (clz == lng || clz == plong) {
			Long l = toLong(value);
			return (T) l;
		} else if (clz == dbl || clz == pdouble) {
			Double i = toDouble(value);
			return (T) i;
		} else if (clz == flt || clz == pfloat) {
			Float i = toFloat(value);
			return (T) i;
		} else if (clz == sarray) {
			return (T) toStringArray(value);
		} else if (clz == bool || clz == pboolean) {
			Boolean b = toBoolean(value);
			return (T) b;
		} else if (clz == fileT) {
			return (T) toFile(value);
		} else if (isMap(clz)) {
			if (value instanceof Map) {
				return (T) value;
			}
			return (T) toMap(value);
		} else if (clz.isArray()) {
			return (T) toArray(clz, value);
		} else if (isCollection(clz)) {
			return toCollection(clz, value);
		}
		else if (clz!= null && clz.getPackage() != null && !clz.getPackage().getName().startsWith("java")
				&& isMap(value.getClass()) && isKeyTypeString(value)) {
			return (T) fromMap((Map<String, Object>) value);
		} 
		else {
			return (T) value;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T toArray(Class<T> clz, Object value) {
		if (clz == intA) {
			return (T) iarray(value);
		} else if (clz == byteA) {
			return (T) barray(value);			
		} else if (clz == charA) {
			return (T) carray(value);					
		} else if (clz == shortA) {
			return (T) sarray(value);					
		} else if (clz == longA) {
			return (T) larray(value);					
		} else if (clz == floatA) {
			return (T) farray(value);					
		} else if (clz == doubleA) {
			return (T) darray(value);					
		} else if (value.getClass() == clz) {
			return (T) value;
		} else {
			int index = 0;
			Object newInstance = Array.newInstance(clz.getComponentType(), len(value));
			Iterator<Object> iterator = iterator(object, value);
			while(iterator.hasNext()) {
				idx(newInstance, index, iterator.next());
				index++;
			}
			return (T) newInstance;
		}
	}


	public static double[] darray(Object value) {
		//You could handle shorts, bytes, longs and chars more efficiently
		if (value.getClass() == shortA) {
			return (double[]) value;
		}
		double[] values = new double[len(value)];
		int index = 0;
		Iterator<Object> iterator = iterator(Object.class, value);
		while (iterator.hasNext()) {
			values[index] = toFloat(iterator.next());
			index++;
		}
		return values;
	}

	public static float[] farray(Object value) {
		//You could handle shorts, bytes, longs and chars more efficiently
		if (value.getClass() == floatA) {
			return (float[]) value;
		}
		float[] values = new float[len(value)];
		int index = 0;
		Iterator<Object> iterator = iterator(Object.class, value);
		while (iterator.hasNext()) {
			values[index] = toFloat(iterator.next());
			index++;
		}
		return values;
	}

	public static long[] larray(Object value) {
		//You could handle shorts, bytes, longs and chars more efficiently
		if (value.getClass() == shortA) {
			return (long[]) value;
		}
		long[] values = new long[len(value)];
		int index = 0;
		Iterator<Object> iterator = iterator(Object.class, value);
		while (iterator.hasNext()) {
			values[index] = toLong(iterator.next());
			index++;
		}
		return values;
	}

	public static short[] sarray(Object value) {
		//You could handle shorts, bytes, longs and chars more efficiently
		if (value.getClass() == shortA) {
			return (short[]) value;
		}
		short[] values = new short[len(value)];
		int index = 0;
		Iterator<Object> iterator = iterator(Object.class, value);
		while (iterator.hasNext()) {
			values[index] = toShort(iterator.next());
			index++;
		}
		return values;
	}

	public static int[] iarray(Object value) {
		//You could handle shorts, bytes, longs and chars more efficiently
		if (value.getClass() == intA) {
			return (int[]) value;
		}
		int[] values = new int[len(value)];
		int index = 0;
		Iterator<Object> iterator = iterator(Object.class, value);
		while (iterator.hasNext()) {
			values[index] = toInt(iterator.next());
			index++;
		}
		return values;
	}

	public static byte[] barray(Object value) {
		//You could handle shorts, ints, longs and chars more efficiently
		if (value.getClass() == byteA) {
			return (byte[]) value;
		}
		byte[] values = new byte[len(value)];
		int index = 0;
		Iterator<Object> iterator = iterator(Object.class, value);
		while (iterator.hasNext()) {
			values[index] = toByte(iterator.next());
			index++;
		}
		return values;
	}
	public static char[] carray(Object value) {
		//You could handle shorts, ints, longs and chars more efficiently
		if (value.getClass() == charA) {
			return (char[]) value;
		}
		char[] values = new char[len(value)];
		int index = 0;
		Iterator<Object> iterator = iterator(object, value);
		while (iterator.hasNext()) {
			values[index] = toChar(iterator.next());
			index++;
		}
		return values;
	}

	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> iterator(Class<T> class1, final Object value) {


		if (isArray(value)) {
			final int length = Facile.arrayLength(value);

			return new Iterator<T>() {
				int i = 0;

				@Override
				public boolean hasNext() {
					return i < length;
				}

				@Override
				public T next() {
					T next = (T) Reflection.idx(value, i);
					i++;
					return next;
				}

				@Override
				public void remove() {
				}
			};
		} else if (isCollection(value.getClass())) {
			return ((Collection<T>) value).iterator();
		} else if (isMap(value.getClass())) {
			Iterator<T> iterator = ((Map<String, T>) value).values().iterator();
			return iterator;
		} else {
			return (Iterator<T>) Collections.singleton(value).iterator();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T toCollection(Class<T> clz, Object value) {
		if (isList(clz)) {
			return (T) toList(value);
		} else if (isSortedSet(clz)) {
			return (T)  toSortedSet(value);
		} else if (isSet(clz)) {
			return  (T) toSet(value);			
		} else {
			return  (T) toList(value);			
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(Object value) {
		if (value instanceof List) {
			return (List)value;
		} else if (value instanceof Collection) {
			return new ArrayList((Collection) value);			
		} else {
			ArrayList list = new ArrayList(len(value));
			Iterator<Object> iterator = iterator(object, value);
			while(iterator.hasNext()) {
				list.add(iterator.next());
			}
			return list;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Set toSet(Object value) {
		if (value instanceof Set) {
			return (Set)value;
		} else if (value instanceof Collection) {
			return new HashSet((Collection) value);			
		} else {
			HashSet set = new HashSet(len(value));
			Iterator<Object> iterator = iterator(object, value);
			while(iterator.hasNext()) {
				set.add(iterator.next());
			}
			return set;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static SortedSet toSortedSet(Object value) {
		if (value instanceof Set) {
			return (SortedSet)value;
		} else if (value instanceof Collection) {
			return new TreeSet((Collection) value);			
		} else {
			TreeSet set = new TreeSet();
			Iterator<Object> iterator = iterator(object, value);
			while(iterator.hasNext()) {
				set.add(iterator.next());
			}
			return set;
		}
	}


	public static boolean isKeyTypeString(Object value) {
		return getKeyType((Map<?, ?>) value) == string;
	}

	public static Map<String, Object> toMap(Object value) {
		return Reflection.toMap(value);
	}

	public static File toFile(Object value) {
		if (value instanceof File) {
			return (File) value;
		} else if (value instanceof CharSequence) {
			return file(str(value));
		} else {
			return toFile(value.toString());
		}
	}

	public static String[] toStringArray(Object value) {
		if (value instanceof CharSequence) {
			String str = toString(value);

			List<Character> delims = ls(',', '\t', ' ', '|', ':', ';');
			char[] chars = chars(str);
			for (char c : delims) {
				if (isIn(c, chars)) {
					return split(chars, c);
				}
			}

		} else if (value instanceof List) {
			// complete this
		}

		return null;
	}

	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		}
		if (obj instanceof String) {
			return (String) obj;
		} else {
			return obj.toString();
		}
	}

	public static Number toWrapper(long l) {
		if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE) {
			return toWrapper((int) l);
		} else {
			return Long.valueOf(l);
		}
	}

	public static Number toWrapper(int i) {
		if (i >= Byte.MIN_VALUE && i <= Byte.MAX_VALUE) {
			return Byte.valueOf((byte) i);
		} else if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE) {
			return Short.valueOf((short) i);
		} else {
			return Integer.valueOf(i);
		}
	}

	public static boolean isBasicType(Object value) {
		return (value instanceof Number || value instanceof CharSequence
				|| value instanceof Date || value instanceof Calendar);
	}

	public static boolean isBasicType(Class<?> theClass) {
		return (number.isAssignableFrom(theClass)
				|| chars.isAssignableFrom(theClass)
				|| date.isAssignableFrom(theClass)
				|| calendar.isAssignableFrom(theClass) || theClass
					.isPrimitive());
	}

	public static boolean isMap(Class<?> thisType) {
		return isType(thisType, Map.class);
	}

	public static boolean isCharSequence(Class<?> thisType) {
		return isType(thisType, CharSequence.class);
	}

	public static boolean isCollection(Class<?> thisType) {
		return isType(thisType, Collection.class);
	}

	public static boolean isList(Class<?> thisType) {
		return isType(thisType, List.class);
	}

	public static boolean isSet(Class<?> thisType) {
		return isType(thisType, Set.class);
	}

	public static boolean isSortedSet(Class<?> thisType) {
		return isType(thisType, SortedSet.class);
	}

	public static boolean isType(Class<?> thisType, Class<?> isThisType) {
		if (thisType == isThisType) {
			return true;
		}
		Class<?>[] interfaces = thisType.getInterfaces();
		for (Class<?> i : interfaces) {
			if (i == isThisType) {
				return true;
			}
		}

		if (!thisType.isInterface() && thisType.getSuperclass() != object) {
			return isType(thisType.getSuperclass(), isThisType);
		}

		return false;
	}

	public static boolean isModifiableCollection(Collection<Object> value) {
		try {
			value.clear();
		} catch (Exception ex) {
			return false;
		}

		@SuppressWarnings("rawtypes")
		Class<? extends Collection> clazz = value.getClass();

		if (clazz == HashSet.class || clazz == TreeSet.class
				|| clazz == ArrayList.class || clazz == LinkedList.class) {
			return true;
		} else {
			return false;
		}
	}

	public static Class<?> getKeyType(Map<?, ?> value) {
		if (value.size() > 0) {
			return value.keySet().iterator().next().getClass();
		} else {
			return null;
		}
	}
	
	public static Object toArray(Collection<?> value) {
		Class<?> componentType = Reflection.getComponentType(value);
		Object array = Array.newInstance(componentType, value.size());
		@SuppressWarnings("unchecked")
		Iterator<Object> iterator = (Iterator<Object>) value.iterator();
		int index=0;
		while (iterator.hasNext()) {
			idx(array, index, iterator.next());
			index++;
		}
		return array;
	}


}
