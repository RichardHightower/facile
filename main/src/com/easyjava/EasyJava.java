package com.easyjava;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

public class EasyJava {
	private static final Logger log = Logger
			.getLogger(EasyJava.class.getName());
	private static final Logger appLog = Logger.getLogger(sprop(
			pkey(EasyJava.class, "appLog"), "genericLog"));

	public static final Class<String> string = String.class;
	public static final Class<Integer> integer = Integer.class;
	public static final Class<Float> flt = Float.class;
	public static final Class<Double> dbl = Double.class;
	public static final Class<Date> date = Date.class;
	public static final boolean debug;
	public static final PrintStream OUT = System.out;
	public static final PrintStream ERR = System.err;
	
	Class<EasyJava> easy = EasyJava.class;


	static {
		debug = sbprop(pkey(EasyJava.class, "debug"));
	}

	// HELPER
	// HELPER

	// HELPER
	// HELPER
	private static Class<?> clazz(Object that) {
		if (that instanceof Class) {
			return (Class<?>) that;
		} else {
			return that.getClass();
		}
	}

	public static String mykey(Class<?> cls, final String key) {
		return pkey(EasyJava.class, key);
	}

	public static String pkey(Class<?> cls, String key) {
		return cls.getName() + "." + key;
	}

	public static String sprop(String key) {
		return System.getProperty(key);
	}

	public static String sprop(String key, String defaultValue) {
		return System.getProperty(key, defaultValue);
	}

	public static boolean sbprop(String key) {
		return Boolean.getBoolean(key);
	}

	public static void print(Object... items) {
		StringBuilder builder = new StringBuilder(256);
		for (Object item : items) {
			builder.append(item);
			builder.append(' ');
		}
		System.out.println(builder);
	}
	
	public static void db(Object... items) {
		StringBuilder builder = new StringBuilder(256);
		for (Object item : items) {
			builder.append(item);
			builder.append(' ');
		}
		fprintln(ERR, str(builder));
	}


	public static void printf(String fmt, Object... args) {
		System.out.printf(fmt, args);
	}
	
	public static void println(String fmt, Object... args) {
		System.out.printf(fmt+"\n", args);
	}


	public static String sprintf(String fmt, Object... args) {
		return String.format(fmt, args);
	}

	public static void fprintf(Logger log, String fmt, Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		log.info(String.format(fmt, args));
	}
	

	public static void fprintf(StringBuilder builder, String fmt, Object... args) {
		builder.append((String.format(fmt, args)));
	}

	public static void fprintf(PrintStream out, String fmt, Object... args) {
		out.append((String.format(fmt, args)));
	}

	public static void fprintln(PrintStream out, String fmt, Object... args) {
		out.append((String.format(fmt + "\n", args)));
	}


	public static void fprintln(StringBuilder builder, String fmt, Object... args) {
		builder.append((String.format(fmt, args)));
		builder.append("\n");
	}


	// Logging
	// Logging
	// Logging
	public static void info(Logger log, String fmt, Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		log.info(String.format(fmt, args));
	}

	public static void debug(Logger log, String fmt, Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		log.fine(String.format(fmt, args));
	}

	public static void trace(Logger log, String fmt, Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		log.finest(String.format(fmt, args));
	}

	public static void info(String fmt, Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		appLog.info(String.format(fmt, args));
	}

	public static void fprintf(String fmt, Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		appLog.info(String.format(fmt, args));
	}

	public static void debug(String fmt, Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		appLog.fine(String.format(fmt, args));
	}

	public static void trace(String fmt, Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		appLog.finest(String.format(fmt, args));
	}

	@SuppressWarnings("serial")
	public static class AssertionException extends RuntimeException {

		public AssertionException() {
			super();
		}

		public AssertionException(String message, Throwable cause) {
			super(message, cause);
		}

		public AssertionException(String message) {
			super(message);
		}

		public AssertionException(Throwable cause) {
			super(cause);
		}
	}

	@SuppressWarnings("serial")
	public static class ReflectionException extends RuntimeException {

		public ReflectionException() {
			super();
		}

		public ReflectionException(String message, Throwable cause) {
			super(message, cause);
		}

		public ReflectionException(String message) {
			super(message);
		}

		public ReflectionException(Throwable cause) {
			super(cause);
		}
	}

	public static void die(boolean condition, String message) {
		if (condition) {
			throw new AssertionException(message);
		}
	}

	public static void die(String message) {
		throw new AssertionException(message);
	}

	public static void die(String message, Object... args) {
		throw new AssertionException(String.format(message, args));
	}

	// CREATIONAL
	// CREATIONAL

	// CREATIONAL
	// CREATIONAL
	public static interface Entry<K, V> {
		K key();

		V value();
	}

	private static class EntryImpl<K, V> implements Entry<K, V> {
		EntryImpl(K k, V v) {
			this.k = k;
			this.v = v;
		}

		K k;
		V v;

		@Override
		public K key() {
			return k;
		}

		@Override
		public V value() {
			return v;
		}
	}

	public static <K, V> Entry<K, V> kv(final K k, final V v) {
		return new EntryImpl<K, V>(k, v);
	}

	public static <K, V> Entry<K, V> entry(final K k, final V v) {
		return new EntryImpl<K, V>(k, v);
	}

	public static <V> List<V> ls(final V... array) {
		return list(array);
	}
	
	public static <V> List<V> lsRange(int start, int finish, final V... array) {
		List <V> list = new ArrayList<V>(finish-start);
		for (;start<finish; start++) {
			list.add(array[start]);
		}
		return list;
	}

	
	public static List<Integer> noZeroList(int [] array) {
		List<Integer> arrayList = new ArrayList<Integer>(array.length);
		for (int i : array) {
			arrayList.add(i);
		}
		return arrayList;
	}

	
	public static boolean isEqual(List<?> left, List<?> right) {
		if (left.size()!=right.size()) {
			return false;
		}
		int index = 0;
		for (Object it : left) {
			if(!right.get(index).equals(it)) {
				return false;
			}
			index++;
		}
		return true;
	}

	public static boolean debugEqual(List<?> left, List<?> right) {
		print (left, right);
		if (left.size()!=right.size()) {
			print ("size not equal");
			return false;
		}
		int index = 0;
		for (Object it : left) {
			if(!right.get(index).equals(it)) {
				print ("item not equal at index", index, "#"+it +"#", "%"+right.get(index)+"%", it.getClass(), right.get(index).getClass());
				return false;
			}
			index++;
		}
		return true;
	}

	public static <V> V[] array(final V... array) {
		return array;
	}

	public static Object[] oarray(final Object... array) {
		return array;
	}

	public static Object[] oar(final Object... array) {
		return array;
	}

	public static <V> V[] ary(final V... array) {
		return array;
	}

	public static char[] ary(String str) {
		return str.toCharArray();
	}

	public static char[] array(String str) {
		return str.toCharArray();
	}

	@SuppressWarnings("unchecked")
	public static <V> V[] array(List<V> list) {
		return (V[]) list.toArray(new Object[list.size()]);
	}

	public static <V> V[] ary(List<V> list) {
		return array(list);
	}

	public static <V> List<V> list(final V... array) {
		ArrayList<V> list = new ArrayList<V>(array.length);
		for (V o : array) {
			list.add(o);
		}
		return list;
	}

	public static <V> List<V> ls(final Collection<V> col) {
		return list(col);
	}

	public static <V> List<V> list(final Collection<V> col) {
		return new ArrayList<V>(col);
	}

	public static <V> List<V> ls(Collection<V> lst,
			final Collection<?>... others) {
		return list(lst, others);
	}

	@SuppressWarnings("unchecked")
	public static <V> List<V> list(Collection<V> lst,
			final Collection<?>... others) {
		int total = lst.size();
		for (Collection<?> c : others) {
			total += c.size();
		}
		ArrayList<Object> list = new ArrayList<Object>(total);
		list.addAll(lst);
		for (Collection<?> c : others) {
			list.addAll(c);
		}
		return (List<V>) list;
	}

	public static <K, V> Map<K, V> copy(final Map<K, V> map) {
		return new HashMap<K, V>(map);
	}

	public static <V> List<V> copy(final List<V> col) {
		return new ArrayList<V>(col);
	}

	public static <V> Set<V> copy(final Set<V> col) {
		return new HashSet<V>(col);
	}

	public static <V> SortedSet<V> copy(final SortedSet<V> col) {
		return new TreeSet<V>(col);
	}

	public static String copy(final String str) {
		return new String(str);
	}

	public static StringBuilder copy(final StringBuilder str) {
		return new StringBuilder(str);
	}

	public static CharSequence copy(final CharSequence str) {
		return str.toString();
	}

	public static <V> List<V> mul(int x, Collection<V> lst) {
		ArrayList<V> list = new ArrayList<V>(x * lst.size());
		for (int index = 0; index < x; index++) {
			for (V element : lst) {
				list.add(element);
			}
		}
		return list;
	}

	public static String mul(int x, CharSequence seq) {
		StringBuilder builder = new StringBuilder(x * seq.length());
		for (int index = 0; index < x; index++) {
			builder.append(seq);
		}
		return builder.toString();
	}

	public static <V> Set<V> set(final V... array) {
		return new HashSet<V>(list(array));
	}

	public static List<Integer> range(int stop) {
		return range(0, stop);
	}

	public static List<Integer> range(int start, int stop) {
		ArrayList<Integer> range = new ArrayList<Integer>(stop - start);
		for (int index = start; index < stop; index++) {
			range.add(index);
		}
		return range;
	}

	public static List<Integer> range(int start, int stop, int inc) {
		ArrayList<Integer> range = new ArrayList<Integer>(stop - start);
		for (int index = start; index < stop; index += inc) {
			range.add(index);
		}
		return range;
	}

	public static <K, V> Map<K, V> mp(K k0, V v0) {
		HashMap<K, V> map = new HashMap<K, V>(10);
		map.put(k0, v0);
		return map;
	}

	public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1) {
		HashMap<K, V> map = new HashMap<K, V>(10);
		map.put(k0, v0);
		map.put(k1, v1);
		return map;
	}

	public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2) {
		HashMap<K, V> map = new HashMap<K, V>(10);
		map.put(k0, v0);
		map.put(k1, v1);
		map.put(k2, v2);
		return map;
	}

	public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
			V v3) {
		HashMap<K, V> map = new HashMap<K, V>(10);
		map.put(k0, v0);
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		return map;
	}

	public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
			V v3, K k4, V v4) {
		HashMap<K, V> map = new HashMap<K, V>(10);
		map.put(k0, v0);
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		return map;
	}

	public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
			V v3, K k4, V v4, K k5, V v5) {
		HashMap<K, V> map = new HashMap<K, V>(10);
		map.put(k0, v0);
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		map.put(k5, v5);
		return map;
	}

	public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
			V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
		HashMap<K, V> map = new HashMap<K, V>(10);
		map.put(k0, v0);
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		map.put(k5, v5);
		map.put(k6, v6);
		return map;
	}

	public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
			V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
		HashMap<K, V> map = new HashMap<K, V>(10);
		map.put(k0, v0);
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		map.put(k5, v5);
		map.put(k6, v6);
		map.put(k7, v7);
		return map;
	}

	public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
			V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
		HashMap<K, V> map = new HashMap<K, V>(10);
		map.put(k0, v0);
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		map.put(k5, v5);
		map.put(k6, v6);
		map.put(k7, v7);
		map.put(k8, v8);
		return map;
	}

	public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
			V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
			K k9, V v9) {
		HashMap<K, V> map = new HashMap<K, V>(10);
		map.put(k0, v0);
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		map.put(k5, v5);
		map.put(k6, v6);
		map.put(k7, v7);
		map.put(k8, v8);
		map.put(k9, v9);
		return map;
	}

	public static <K, V> Map<K, V> mp(K k0, V v0, K k1, V v1, K k2, V v2, K k3,
			V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
			K k9, V v9, Entry<K, V>... entries) {
		HashMap<K, V> map = new HashMap<K, V>(10 + entries.length);
		map.put(k0, v0);
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		map.put(k5, v5);
		map.put(k6, v6);
		map.put(k7, v7);
		map.put(k8, v8);
		map.put(k9, v9);
		for (Entry<K, V> entry : entries) {
			map.put(entry.key(), entry.value());
		}
		return map;
	}

	public static <K, V> Map<K, V> mp(Collection<K> keys, Collection<V> values) {
		HashMap<K, V> map = new HashMap<K, V>(10 + keys.size());
		Iterator<V> iterator = values.iterator();
		for (K k : keys) {
			if (iterator.hasNext()) {
				V v = iterator.next();
				map.put(k, v);
			} else {
				map.put(k, null);
			}
		}
		return map;
	}

	public static <K, V> Map<K, V> mp(K[] keys, V[] values) {
		HashMap<K, V> map = new HashMap<K, V>(10 + keys.length);
		int index = 0;
		for (K k : keys) {
			if (index < keys.length) {
				V v = values[index];
				map.put(k, v);
			} else {
				map.put(k, null);
			}
			index++;
		}
		return map;
	}

	public static <K, V> Map<K, V> dict(Entry<K, V>... entries) {
		HashMap<K, V> map = new HashMap<K, V>(5);
		for (Entry<K, V> entry : entries) {
			map.put(entry.key(), entry.value());
		}
		return map;
	}

	//
	// Functional
	//

	//
	// Functional
	//
	public static interface Function<T> {
		T execute(Object... params);
	}

	private static class FunctionImpl<T> implements Function<T> {
		Method method;
		Object that;

		FunctionImpl(Class<T> clazz, Method method, Object that) {
			this.method = method;
			this.that = that;
		}

		@SuppressWarnings("unchecked")
		public T execute(Object... params) {

			T ret = null;
			try {
				ret = (T) method.invoke(that, params);
			} catch (Exception ex) {
				throw new ReflectionException("unable to execute function "
						+ method.getName(), ex);
			}
			return ret;
		}

	}

	public static <T> Function<T> f(Class<T> returnType, Object that,
			Object... args) {
		try {
			return fn(returnType, that, "f", args);
		} catch (Exception e) {
			throw new ReflectionException(e);
		}
	}

	public static Function<?> f(Object that, Object... args) {
		try {
			return fn(that, "f", args);
		} catch (Exception e) {
			throw new ReflectionException(e);
		}
	}

	public static Function<?> fn(Object that, Object name, Object... args) {
		return fn(Object.class, that, name, args);
	}

	public static <T> Function<T> fn(Class<T> returnType, Object that,
			Object name, Object... args) {
		try {
			Method[] methods = clazz(that).getDeclaredMethods();
			for (Method m : methods) {
				if (args.length <= m.getParameterTypes().length) {
					if (m.getName().equals(name.toString())) {
						m.setAccessible(true);
						if (Modifier.isStatic(m.getModifiers())) {
							return new FunctionImpl<T>(returnType, m, null);
						} else {
							return new FunctionImpl<T>(returnType, m, that);
						}
					}
				}
			}
		} catch (Exception ex) {
			log.log(Level.SEVERE, String.format(
					"Unable to find function that=%s, name=%s, args=%s", that,
					name, list(args)), ex);
		}
		die("Unable to find function that=%s, name=%s, args=%s", that, name,
				list(args));
		return null;
	}

	public static void enumerate(Function<?> f, Collection<?> c) {
		int index = 0;
		for (Object o : c) {
			f.execute(index, o);
			index++;
		}
	}

	public static Collection<?> filter(Function<?> f, List<?> l) {
		return gfilter(f, l);
	}

	public static Collection<?> filter(Function<?> f, Collection<?> c) {
		return gfilter(f, c);
	}

	public static <T> Collection<T> gfilter(Function<?> f, List<T> l) {
		ListIterator<?> listIterator = l.listIterator();
		while (listIterator.hasNext()) {
			Boolean b = (Boolean) f.execute(listIterator.next());
			if (!b) {
				listIterator.remove();
			}
		}
		return l;
	}

	public static <T> Collection<T> gfilter(Function<?> f, Collection<T> in) {
		ArrayList<Object> removeList = new ArrayList<Object>(in.size());
		ArrayList<T> c = new ArrayList<T>(in);

		for (Object o : in) {
			Boolean b = (Boolean) f.execute(0);
			if (!b) {
				removeList.add(o);
			}
		}
		for (Object o : removeList) {
			c.remove(o);
		}
		return c;
	}

	public static List<?> map(Function<?> f, List<?>... cols) {
		return gmap(f, cols);
	}

	public static <OUT> List<OUT> gmap(Function<OUT> f, List<?>... cols) {

		int max = Integer.MIN_VALUE;

		for (int index = 0; index < cols.length; index++) {
			Collection<?> c = cols[index];
			if (c.size() > max) {
				max = c.size();
			}
		}

		ArrayList<OUT> mapList = new ArrayList<OUT>(max);
		for (int row = 0; row < max; row++) {
			ArrayList<Object> args = new ArrayList<Object>(cols.length);

			for (int index = 0; index < cols.length; index++) {
				List<?> c = cols[index];
				if (row < c.size()) {
					args.add(c.get(row));
				} else {
					args.add(null);
				}
			}
			OUT object = f.execute(args.toArray(new Object[cols.length]));
			mapList.add(object);

		}

		return mapList;
	}

	public static List<?> map(Function<?> f, Collection<?> c) {
		return gmap(f, c);
	}

	public static <IN, OUT> List<OUT> gmap(Function<OUT> f, Collection<IN> c) {
		ArrayList<OUT> mapList = new ArrayList<OUT>(c.size());
		for (Object o : c) {
			mapList.add(f.execute(o));
		}
		return mapList;
	}

	@SuppressWarnings("rawtypes")
	public static Object reduce(Function f, Collection<?> c) {
		return greduce(f, c);
	}

	@SuppressWarnings("rawtypes")
	public static Object reduce(Function f, Object[] array) {
		return greduce(f, list(array));
	}

	@SuppressWarnings("unchecked")
	public static <T> T greduce(Function<?> f, Collection<T> c) {
		T accumulo = null;
		int index = 0;
		for (T o : c) {
			if (index == 0) {
				accumulo = o;
			} else {
				accumulo = (T) f.execute(accumulo, o);
			}
			index++;
		}
		return accumulo;
	}

	// File
	// File
	public static interface File {
		String read(String path);

		String[] readLines(String path);
	}

	// String
	// String

	public static String lns(String... lines) {
		return lines(lines);
	}

	public static String lines(String... lines) {
		return join("\n", (Object[]) lines);
	}

	public static String join(String delim, Object... args) {
		StringBuilder builder = new StringBuilder(256);
		for (Object arg : args) {
			builder.append(arg.toString());
			builder.append(delim);
		}
		return builder.toString();
	}

	public static String join(char delim, Object... args) {
		StringBuilder builder = new StringBuilder(256);
		for (Object arg : args) {
			builder.append(arg.toString());
			builder.append(delim);
		}
		return builder.toString();
	}

	public static String join(Object... args) {
		StringBuilder builder = new StringBuilder(256);
		for (Object arg : args) {
			builder.append(arg.toString());
		}
		return builder.toString();
	}

	public static String slc(CharSequence str, int start, int end) {
		return slice(str, start, end);
	}

	public static String slice(CharSequence str, int start, int end) {
		final int length = str.length();

		// Adjust
		if (start < 0) {
			start = length + start;
		}
		if (end < 0) {
			end = length + end;
		}

		// Bound check
		if (start < 0) {
			start = 0;
		}
		if (start > length) {
			start = length;
		}
		if (end > str.length()) {
			end = str.length();
		}
		if (end < 0) {
			end = 0;
		}

		// Bound check
		if (start > end) {
			return "";
		}

		return str.subSequence(start, end).toString();
	}

	public static String slc(CharSequence str, int start) {
		return slice(str, start);
	}

	public static String slice(CharSequence str, int start) {
		return slice(str, start, str.length());
	}

	public static char index(CharSequence str, int index) {
		return idx(str, index);
	}

	public static char idx(CharSequence str, int index) {
		final int length = str.length();

		// Adjust
		if (index < 0) {
			index = length + index;
		}

		// Bound check
		if (index < 0) {
			index = 0;
		}
		if (index > length) {
			index = length;
		}

		return str.charAt(index);

	}

	public static <T> List<T> slc(List<T> lst, int start, int end) {
		return slice(lst, start, end);
	}

	public static <T> List<T> slice(List<T> lst, int start, int end) {
		final int length = lst.size();
		// Adjust
		if (start < 0) {
			start = length + start;
		}
		if (end < 0) {
			end = length + end;
		}

		// Bound check
		if (start < 0) {
			start = 0;
		}
		if (start > length) {
			start = length;
		}
		if (end > length) {
			end = length;
		}
		if (end < 0) {
			end = 0;
		}

		// Bound check
		if (start > end) {
			return lst.subList(0, 0);
		}
		return lst.subList(start, end);
	}

	public static <T> void rpl(List<T> lst, int start, int end,
			List<T> replacement) {
		replace(lst, start, end, replacement);
	}

	public static <T> void rpl(List<T> lst, int start, List<T> replacement) {
		rpl(lst, start, replacement);
	}

	public static <T> void replace(List<T> lst, int start, List<T> replacement) {
		replace(lst, start, lst.size(), replacement);
	}

	public static <T> void replace(List<T> lst, int start, int end,
			List<T> replacement) {
		final int length = lst.size();
		// Adjust
		if (start < 0) {
			start = length + start;
		}
		if (end < 0) {
			end = length + end;
		}

		// Bound check
		if (start < 0) {
			start = 0;
		}
		if (start > length) {
			start = length;
		}
		if (end > length) {
			end = length;
		}
		if (end < 0) {
			end = 0;
		}

		if (start == end) {
			List<T> copy = copy(replacement);
			Collections.reverse(copy);
			for (T t : copy) {
				lst.add(start, t);
			}
			return;
		}

		int slicesize = end - start;
		int current = start;
		for (int index = 0; index < replacement.size(); index++, current++) {
			T t = replacement.get(index);
			if (index < slicesize) {
				lst.set(current, t);
			} else {
				lst.add(current, t);
			}
		}
		int stop = current;
		while (current < end) {
			lst.remove(stop);
			current++;
		}

	}

	public static <T> List<T> slc(List<T> lst, int start) {
		return slice(lst, start);
	}

	public static <T> List<T> slice(List<T> lst, int start) {
		return slice(lst, start, lst.size());
	}

	public static <T> T idx(List<T> lst, int index) {
		return index(lst, index);
	}

	public static <T> T index(List<T> lst, int index) {
		final int length = lst.size();
		
		if (length==0) {
			return null;
		}

		// Adjust
		if (index < 0) {
			index = length + index;
		}

		// Bound check
		if (index < 0) {
			index = 0;
		}
		if (index > length) {
			index = length;
		}

		return lst.get(index);

	}

	public static <T> void expect(T ex, T v) {
		if (ex == null && v == null) {
			return;
		}

		if (ex == null && v != null) {
			throw new AssertionException(sprintf(
					"expected was null, but value was %s", v));
		}

		if (!ex.equals(v)) {
			throw new AssertionException(sprintf(
					"expected was %s, but value was %s", ex, v));
		}

	}

	public static String mfmt(String str, Map<?, ?> map) {
		return mapFormat(str, map);
	}


	private static int _lookForFormatRules(_context cx, int i, char c) {
		cx.formatRules = false;
		for (; i < cx.ca.length && c != '}'; i++) {
			c = cx.ca[i];
			if (c == '[' || c == '!' || c == ':' || c == '.') {
				cx.formatRules = true;
			}
			if (c != '}') {
				if (!cx.formatRules) {
					cx.contents.append(c);
				} else {
					cx.formatRulesContents.append(c);
				}
			}
		}
		return i;
	}

	@SuppressWarnings("unchecked")
	private static int _handleLoop(int index, _context cx) {
		char c = '#';
		StringBuilder builder = new StringBuilder(256);
		String endloop = "{@endloop}";
		

		for (; index < cx.ca.length && c != '}'; index++) {
			c = cx.ca[index];
			if (c != '}')
				builder.append(c);
		}

		String expression = builder.toString();
		builder.setLength(0);

		char [] ca = cx.ca;
		for (; index < cx.ca.length; index++) {
			c = cx.ca[index];
			if (c == '{' && index + endloop.length() < ca.length) {
				int x = index;
				if (ca[x + 1] == '@' && ca[x + 2] == 'e' && ca[x + 3] == 'n'
						&& ca[x + 4] == 'd' && ca[x + 5] == 'l'
						&& ca[x + 6] == 'o' && ca[x + 7] == 'o'
						&& ca[x + 8] == 'p' && ca[x + 9] == '}') {
					index += endloop.length();
					break;
				}
			}
			builder.append(c);
		}

		String body = builder.toString();
		
		Object lobj = lookup(cx.namespace, expression);
		
		if (lobj instanceof List) {
			List <Object> list = (List<Object>) lobj;
			for (Object obj : list){
				((Map<String, Object>)cx.namespace).put("iter", obj);
				cx.builder.append(mfmt(body, cx.namespace));
			}
		}

		return index;
	}
	
	private static int _handleIf(int index, _context cx) {
		char c = '#';
		StringBuilder builder = new StringBuilder(256);
		String endloop = "{@endif}";
		

		for (; index < cx.ca.length && c != '}'; index++) {
			c = cx.ca[index];
			if (c != '}')
				builder.append(c);
		}

		String expression = builder.toString();
		builder.setLength(0);

		char [] ca = cx.ca;
		for (; index < cx.ca.length; index++) {
			c = cx.ca[index];
			if (c == '{' && index + endloop.length() < ca.length) {
				int x = index;
				if (ca[x + 1] == '@' && ca[x + 2] == 'e' && ca[x + 3] == 'n'
						&& ca[x + 4] == 'd' && ca[x + 5] == 'i'
						&& ca[x + 6] == 'f' && ca[x + 7] == '}') {
					index += endloop.length();
					break;
				}
			}
			builder.append(c);
		}

		String body = builder.toString();
		
		Boolean bool = (Boolean)lookup(cx.namespace, expression);
		if (bool) {
			cx.builder.append(mfmt(body, cx.namespace));
		}
		return index;
	}


	public static String fmt(String str, Object... args) {
		return format(str, args);
	}

	public static String format(String str, Object... args) {

		if (args.length == 1) {
			if (args[0] instanceof char[]) {
				return cformat(str, (char[]) args[0]);
			} else if (args[0] instanceof Map) {
				return mapFormat(str, (Map<?, ?>) args[0]);
			} else {
				return arrayFormat(str, args);
			}
		} else {
			return arrayFormat(str, args);
		}
	}

	public static String arfmt(String str, Object... args) {
		return format(str, args);
	}

	private interface NamespaceResolver<KEY> {
		Object lookup(KEY key);
	}

	@SuppressWarnings("unused")
	private class ObjectArrayResolver implements NamespaceResolver<Integer> {

		Object[] args;

		ObjectArrayResolver(Object[] args) {
			this.args = args;
		}

		public Object lookup(Integer index) {
			return args[index];
		}

	}

	private static class _context {
		_context(Map<?, ?> map, String str) {
			this(str);
			this.namespace = copy(map);
		}

		_context(String str, Object... args) {
			this.str = str;
			this.args = args;
			charArray = this.str.toCharArray();
			ca = charArray;
			builder = new StringBuilder(str.length());
			contents = new StringBuilder(32);
			formatRulesContents = new StringBuilder(32);
			this.namespace = new HashMap<String, Object>();
		}

		Map<?, ?> namespace;
		String str;
		StringBuilder builder;
		StringBuilder contents;
		StringBuilder formatRulesContents;
		int argIndexKeeper = 0;
		int argIndex = -666;
		Object[] args;
		char charArray[];
		char ca[];
		boolean formatRules = false;
		final String loop = "@loop:";
		final String ifCmd = "@if:";
		int i;

	}
	
	
	public static String mapFormat(String astr, Map<?, ?> amap) {

		_context cx = new _context(amap, astr);

		for (; cx.i < cx.ca.length; cx.i++) {
			char c = cx.ca[cx.i];
			if (c == '{') {
				cx.contents.setLength(0);
				cx.formatRulesContents.setLength(0);
				cx.i++;

				if (_ifHandledLoopCmd(cx, cx.i)) {
					//
				} else if (_ifHandledIfCmd(cx, cx.i)) {
					
				}
				else {

					cx.i = _lookForFormatRules(cx, cx.i, c);

					if (!cx.formatRules) {
						cx.builder.append(cx.namespace.get(cx.contents
								.toString()));
					} else {
						cx.builder.append(_formatRule(
								cx.namespace.get(cx.contents.toString()),
								cx.formatRulesContents.toString()));
					}

					cx.i--;
				}
			} else {
				cx.builder.append(c);
			}
		}
		return cx.builder.toString();
	}


	public static String arrayFormat(String astr, Object... aargs) {
		_context cx = new _context(astr, aargs);

		for (; cx.i < cx.charArray.length; cx.i++) {
			char c = cx.charArray[cx.i];
			if (c == '{') {
				cx.contents.setLength(0);
				cx.formatRulesContents.setLength(0);
				cx.i++;

				if (_ifHandledLoopCmd(cx, cx.i)) {
					//
				} else {

					cx.i = _lookForFormatRules(cx, cx.i, c);
					_addTemplateValueToStringBuilder(cx);
					cx.i--;
				}
			} else {
				cx.builder.append(c);
			}
		}
		return cx.builder.toString();
	}

	private static boolean _ifHandledLoopCmd(_context cx, int i) {
		if( i + cx.loop.length() < cx.ca.length && cx.ca[i] == '@'
				&& cx.ca[i + 1] == 'l' && cx.ca[i + 2] == 'o'
				&& cx.ca[i + 3] == 'o' && cx.ca[i + 4] == 'p'
				&& cx.ca[i + 5] == ':') {
			cx.i = _handleLoop(i + cx.loop.length(), cx);
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean _ifHandledIfCmd(_context cx, int i) {
		if( i + cx.loop.length() < cx.ca.length && cx.ca[i] == '@'
				&& cx.ca[i + 1] == 'i' && cx.ca[i + 2] == 'f'
				&& cx.ca[i + 3] == ':') {
			cx.i = _handleIf(i + cx.ifCmd.length(), cx);
			return true;
		} else {
			return false;
		}
	}


	private static void _addTemplateValueToStringBuilder(_context cx) {
		String sindex = cx.contents.toString().trim();
		if (sindex.isEmpty()) {
			cx.argIndex = cx.argIndexKeeper;
			addTemplateValueToStringBuilder(cx);
			cx.argIndexKeeper++;
		} else {
			cx.argIndex = Integer.parseInt(sindex);
			addTemplateValueToStringBuilder(cx);
		}
	}

	private static void addTemplateValueToStringBuilder(_context cx) {
		Object obj = cx.args[cx.argIndex];
		if (obj==null) {
			obj = "NULL";
		}
		if (!cx.formatRules) {
			cx.builder.append(obj.toString());
		} else {
			cx.builder.append(_formatRule(obj,
					cx.formatRulesContents.toString()));
		}
	}

	private static String _formatRule(Object object, String rule) {
		StringBuilder format = new StringBuilder(16);
		StringBuilder idx = new StringBuilder(16);
		List<Object> idxs = new ArrayList<Object>(16);
		char[] charArray = rule.toCharArray();

		boolean hasFormat = false;
		boolean hasIndex = false;
		boolean hasDotIndex = false;

		for (int index = 0; index < charArray.length; index++) {
			char c = charArray[index];

			if (hasFormat) {
				format.append(c);
			} else if (hasIndex) {
				if (c == ']') {
					idxs.add(Integer.parseInt(idx.toString()));
					hasIndex = false;
				} else {
					idx.append(c);
				}
			} else if (hasDotIndex) {
				if (c == '[' || c == '.' || c == ':') {
					idxs.add(idx.toString());
					hasDotIndex = false;
				} else {
					idx.append(c);
					if (index + 1 == charArray.length) {
						idxs.add(idx.toString());
						hasDotIndex = false;
					}
				}
			}

			if (c == ':') {
				hasFormat = true;
				format.setLength(0);
			} else if (c == '[') {
				hasIndex = true;
				idx.setLength(0);
			} else if (c == '.') {
				hasDotIndex = true;
				idx.setLength(0);
			}

		}
		if (hasFormat) {
			format.insert(0, '%');
		}

		for (Object oindex : idxs) {
			if (oindex instanceof Integer) {
				object = _indexLookup(object, oindex);
			} else {
				String key = (String) oindex;
				if (object instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<String, ?> map = (Map<String, ?>) object;
					object = map.get(key);
				} else {
					object = _propLookup(object, key);
				}
			}
		}
		if (object == null) {
			return "";
		} else if (hasFormat) {
			return String.format(format.toString(), object);
		} else {
			return object.toString();
		}

	}

	public static Object lookup(Object object, String expression) {
		StringBuilder idx = new StringBuilder(16);
		StringBuilder op = new StringBuilder(16);
		List<Object> idxs = new ArrayList<Object>(16);
		
		List<Object> leftIdxs = null;

		char[] charArray = expression.toCharArray();
		
		Object root = object;

		boolean hasIndex = false;
		boolean hasDotIndex = false;
		
		if (charArray[0]=='[') {
			hasIndex = true;
		} else {
			hasDotIndex = true;
		}

		for (int index = 0; index < charArray.length; index++) {
			char c = charArray[index];

			if (hasIndex) {
				if (c == ']') {
					idxs.add(Integer.parseInt(idx.toString()));
					hasIndex = false;
				} else {
					idx.append(c);
				}
			} else if (hasDotIndex) {
				if (c == '[' || c == '.' || c == ':' || c=='>' || c=='<' || c=='=' || c==' ' || c=='!')  {
					idxs.add(idx.toString());
					hasDotIndex = false;
				} else {
					idx.append(c);
					if (index + 1 == charArray.length) {
						idxs.add(idx.toString());
						hasDotIndex = false;
					}
				}
			}

			if (c == '[') {
				hasIndex = true;
				idx.setLength(0);
			} else if (c == '.') {
				hasDotIndex = true;
				idx.setLength(0);
			} else if (c=='>' || c=='<' || c=='=' || c==' ' || c=='!') {
				op.setLength(0);
				op.append(c);
				
				inner:
				for (int x = 1; x < 5; x++) {
					char ch = '#';
					if (index+x <  charArray.length) {
						ch = charArray[index+x];
						if (ch=='>' || ch=='<' || ch=='=' || ch=='!' || ch==' ') {
							op.append(charArray[index+x]);						
						} else {
							leftIdxs = idxs;
							idxs = new ArrayList<Object>(16);
							if (ch=='[') {
								hasIndex = true;
							} else {
								hasDotIndex = true;
							}
							idx.setLength(0);
							index = index+x-1;
							break inner;
						}
					}
				}
			}

		}
		print("here", "left", leftIdxs, "right", idxs, "operator",op.toString());
		
		if (leftIdxs==null) {
			object = findByPath(object, idxs);
			return object;
		} else {
			print("left", leftIdxs, "right", idxs, "operator",op.toString());
			Object left =  findByPath(object, leftIdxs);
			Object right = findByPath(root, idxs);
			print("left", left, "op", op.toString(), "right", right);
			return left; //later evaluate
		}

	}

	private static Object findByPath(Object object, List<Object> idxs) {
		for (Object oindex : idxs) {
			if (oindex instanceof Integer) {
				object = _indexLookup(object, oindex);
			} else {
				String key = (String) oindex;
				if (object instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<String, ?> map = (Map<String, ?>) object;
					object = map.get(key);
				} else {
					object = _propLookup(object, key);
				}
			}
		}
		return object;
	}

	private static Object _indexLookup(Object object, Object oindex) {
		int index = (Integer) oindex;
		if (object.getClass().isArray()) {
			object = Array.get(object, index);
		} else if (object instanceof List) {
			object = index(((List<?>) object), index);
		} else {
			object = null;
		}
		return object;
	}

	private static Object _propLookup(Object object, final String key) {
		if (object == null) {
			return null;
		}
		object.getClass().getDeclaredMethods();
		Class<? extends Object> clz = object.getClass();
		outer: while (clz != Object.class) {
			Method[] methods = clz.getDeclaredMethods();
			for (Method method : methods) {
				method.setAccessible(true);
				if (method.getParameterTypes().length == 0
						&& method.getName().toLowerCase()
								.endsWith(key.toLowerCase())
						&& (method.getName().startsWith("is")
								|| method.getName().startsWith("get") || method
								.getName().length() == key.length())) {
					try {
						object = method.invoke(object, (Object[]) null);
						break outer;
					} catch (Exception ex) {
						continue;
					}
				}
			}
			Field[] declaredFields = clz.getDeclaredFields();
			for (Field field : declaredFields) {
				field.setAccessible(true);
				if (field.getName().equals(key)) {
					try {
						object = field.get(object);
						break outer;
					} catch (Exception ex) {
						break;
					}
				}
			}

			clz = clz.getSuperclass();
		}
		return object;

	}

	public static String cfmt(String str, char... args) {
		return cformat(str, args);
	}

	public static String cformat(String str, char... args) {
		StringBuilder builder = new StringBuilder(str.length());
		StringBuilder contents = new StringBuilder(32);

		int argIndexKeeper = 0;
		int argIndex = -666;

		char[] charArray = str.toCharArray();
		for (int index = 0; index < charArray.length; index++) {
			char c = charArray[index];
			if (c == '{') {
				contents.setLength(0);
				index++;

				for (; index < charArray.length && c != '}'; index++) {
					c = charArray[index];
					if (c != '}') {
						contents.append(c);
					}
				}

				String sindex = contents.toString().trim();
				if (sindex.isEmpty()) {
					argIndex = argIndexKeeper;
					builder.append(args[argIndex]);
					argIndexKeeper++;
				} else {
					argIndex = Integer.parseInt(sindex);
					builder.append(args[argIndex]);
				}
				index--;
			} else {
				builder.append(c);
			}
		}
		return builder.toString();
	}
	
	public static char[] range(char start, char end) {
		StringBuilder builder = new StringBuilder();
		for (char c = start; c <=end; c++) {
			builder.append(c);
		}
		return builder.toString().toCharArray();
	}
	
	
	public static String[] split(String str, String splitBy) {
		return str.split(splitBy);
	}
	
	public static String repr(char c) {
		return "'" + c + "'";
	}
	
	

	public static String[] toLines(String str) {
		return str.split("[\r\n]");
	}
	public static String str (char... chars) {
		return string(chars);
	}
	
	public static char[] chars(String str) {
		if (str!=null) {
			return str.toCharArray();
		} else {
			return new char[0];
		}
	}
	public static char[] chars(char... chars) {
		return str(chars).toCharArray();
	}

	public static String string(char... chars) {
		if (chars!=null) {
			return String.valueOf(chars);			
		}else {
			return "";
		}

	}
	
	public static String string(int start, int stop, char... chars) {
		if (chars!=null) {
			return String.valueOf(chars, start, stop);	
		}else {
			return "";
		}

	}

	public static String str(Object obj) {
		return string(obj);
	}
	public static String string(Object obj) {
		return obj.toString();
	}
	
	public static boolean isIn (char c, char... array) {
		for (int index = 0; index < array.length; index++) {
			if (c==array[index]) {
				return true;
			}
		}
		return false;
		
	}

	
	public static List<Character> list(char... chars) {
		return ls(chars);
	}
	
	public static char[] add(char [] chars, char...chars2) {
		List<Character> list = ls(chars);
		list.addAll(ls(chars2));
		char [] chars3 = new char[chars.length + chars2.length];
		int index=0;
		for (char c : list){
			chars3[index] = c;
			index++;
		}
		return chars3;
	}

	public static List<Character> ls(char... chars) {
		List<Character> ls = new ArrayList<Character>();
		for (char c: chars) {
			ls.add(c);
		}
		return ls;
	}
	
	public static <T> void checkArgumentsForNulls(T... objects) {
		if (objects==null) {
			throw new IllegalArgumentException(" nulls arguments are not allowed ");
			
		}
		for (Object obj : objects) {
			if (obj==null) {
				throw new IllegalArgumentException(" nulls arguments are not allowed ");
			}
		}
	}
}
