package org.facile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
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
import org.facile.IO;
import org.facile.IO.FileObject;

import static org.facile.Templating.*;

public class Facile {
	private static final Logger log = Logger.getLogger(Facile.class.getName());
	private static final Logger appLog = Logger.getLogger(sprop(
			pkey(Facile.class, "appLog"), "genericLog"));

	public static final Class<String> string = String.class;
	public static final Class<Integer> integer = Integer.class;
	public static final Class<Float> flt = Float.class;
	public static final Class<Double> dbl = Double.class;
	public static final Class<Date> date = Date.class;
	public static final boolean debug;
	public static final PrintStream OUT = System.out;
	public static final PrintStream ERR = System.err;

	Class<Facile> easy = Facile.class;

	static {
		debug = sbprop(pkey(Facile.class, "debug"));
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
		return pkey(Facile.class, key);
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

	public static class PrintEnumerate implements Enumerate<Object> {

		@Override
		public void visit(int index, Object object) {
			print(object);
		}

	}

	public static PrintEnumerate printEnum = new PrintEnumerate();

	public static String sprint(Object... items) {
		StringBuilder builder = new StringBuilder(256);
		for (Object item : items) {
			builder.append(item);
			builder.append(' ');
		}
		return builder.toString();
	}

	public static void debug(Object... items) {
		db(items);
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
		System.out.printf(fmt + "\n", args);
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

	public static void fprintf(StringBuilder builder, String fmt,
			Object... args) {
		builder.append((String.format(fmt, args)));
	}

	public static void fprintf(PrintStream out, String fmt, Object... args) {
		out.append((String.format(fmt, args)));
	}

	public static void fprintf(Appendable out, String fmt, Object... args) {
		try {
			out.append((String.format(fmt, args)));
		} catch (IOException e) {
			warning(log, e, "problem with fprintf");
		}
	}

	public static void fprintln(PrintStream out, String fmt, Object... args) {
		out.append((String.format(fmt + "\n", args)));
	}

	public static void fprintln(StringBuilder builder, String fmt,
			Object... args) {
		builder.append((String.format(fmt, args)));
		builder.append("\n");
	}

	public static void fprintln(Appendable out, String fmt, Object... args) {
		try {
			out.append((String.format(fmt, args)));
			out.append("\n");
		} catch (IOException e) {
			warning(log, e, "problem with fprintf");
		}
	}

	// Logging
	// Logging
	// Logging
	public static void warning(Logger log, Throwable ex, String fmt,
			Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		log.log(Level.WARNING, String.format(fmt, args), ex);
	}

	public static void error(Logger log, Throwable ex, String fmt,
			Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		log.log(Level.SEVERE, String.format(fmt, args), ex);
	}

	public static void info(Logger log, Throwable ex, String fmt,
			Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		log.log(Level.INFO, String.format(fmt, args), ex);

	}

	public static void debug(Logger log, Throwable ex, String fmt,
			Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		log.log(Level.FINE, String.format(fmt, args), ex);

	}

	public static void trace(Logger log, Throwable ex, String fmt,
			Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		log.log(Level.FINEST, String.format(fmt, args), ex);
	}

	public static void warning(Logger log, String fmt, Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		log.warning(String.format(fmt, args));
	}

	public static void error(Logger log, String fmt, Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		log.warning(String.format(fmt, args));
	}

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

	public static void error(String fmt, Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		appLog.severe(String.format(fmt, args));
	}

	public static void warning(String fmt, Object... args) {
		if (debug) {
			printf(fmt, args);
			return;
		}
		appLog.warning(String.format(fmt, args));
	}

	public static void info(String fmt, Object... args) {
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

	public static List<Double> dlist(final double... array) {
		List<Double> list = new ArrayList<Double>();
		for (double d : array) {
			list.add(d);
		}
		return list;
	}

	public static List<Long> flist(final long... array) {
		List<Long> list = new ArrayList<Long>();
		for (long d : array) {
			list.add(d);
		}
		return list;
	}

	public static List<Float> llist(final float... array) {
		List<Float> list = new ArrayList<Float>();
		for (float x : array) {
			list.add(x);
		}
		return list;
	}

	public static List<Integer> ilist(final int... array) {
		List<Integer> list = new ArrayList<Integer>();
		for (int x : array) {
			list.add(x);
		}
		return list;
	}

	public static List<Byte> blist(final byte... array) {
		List<Byte> list = new ArrayList<Byte>();
		for (byte x : array) {
			list.add(x);
		}
		return list;
	}

	public static List<Short> slist(final short... array) {
		List<Short> list = new ArrayList<Short>();
		for (short x : array) {
			list.add(x);
		}
		return list;
	}

	public static <V> List<V> lsRange(int start, int finish, final V... array) {
		List<V> list = new ArrayList<V>(finish - start);
		for (; start < finish; start++) {
			list.add(array[start]);
		}
		return list;
	}

	public static List<Integer> noZeroList(int[] array) {
		List<Integer> arrayList = new ArrayList<Integer>(array.length);
		for (int i : array) {
			arrayList.add(i);
		}
		return arrayList;
	}

	public static boolean isEqual(List<?> left, List<?> right) {
		if (left.size() != right.size()) {
			return false;
		}
		int index = 0;
		for (Object it : left) {
			if (!right.get(index).equals(it)) {
				return false;
			}
			index++;
		}
		return true;
	}

	public static boolean debugEqual(List<?> left, List<?> right) {
		print(left, right);
		if (left.size() != right.size()) {
			print("size not equal");
			return false;
		}
		int index = 0;
		for (Object it : left) {
			if (!right.get(index).equals(it)) {
				print("item not equal at index", index, "#" + it + "#", "%"
						+ right.get(index) + "%", it.getClass(),
						right.get(index).getClass());
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
		if (list.size() > 0) {
			print();
			Object newInstance = Array.newInstance(list.get(0).getClass(),
					list.size());
			return (V[]) list.toArray((V[]) newInstance);
		} else {
			complain("array(list): The list has to have at least one item in it");
			return null;
		}
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

	public static <K, V> Map<K, V> mp(Entry<K, V>... entries) {
		HashMap<K, V> map = new HashMap<K, V>(entries.length);
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

	public static <T> Function<T> f(Class<T> returnType, Object that) {
		try {
			return fn(returnType, that, "f");
		} catch (Exception e) {
			throw new ReflectionException(e);
		}
	}

	public static Function<?> f(Object that) {
		try {
			return fn(that, "f");
		} catch (Exception e) {
			throw new ReflectionException(e);
		}
	}

	public static Function<?> fn(Object that, Object name) {
		return fn(Object.class, that, name);
	}

	public static <T> Function<T> fn(Class<T> returnType, Object that,
			Object name) {
		try {
			Method[] methods = clazz(that).getDeclaredMethods();
			if (that instanceof Class) {
				Constructor<?> constructor = ((Class<?>) that)
						.getDeclaredConstructors()[0];
				constructor.setAccessible(true);
				that = constructor.newInstance((Object[]) null);
			}
			if (methods.length == 1) {
				methods[0].setAccessible(true);
				if (Modifier.isStatic(methods[0].getModifiers())) {
					return new FunctionImpl<T>(returnType, methods[0], null);
				} else {
					return new FunctionImpl<T>(returnType, methods[0], that);
				}
			}
			for (Method m : methods) {
				if (m.getName().equals(name.toString())) {
					m.setAccessible(true);
					if (Modifier.isStatic(m.getModifiers())) {
						return new FunctionImpl<T>(returnType, m, null);
					} else {
						return new FunctionImpl<T>(returnType, m, that);
					}
				}
			}
		} catch (Exception ex) {
			log.log(Level.SEVERE, String.format(
					"Unable to find function that=%s, name=%s", that, name), ex);
		}
		die("Unable to find function that=%s, name=%s", that, name);
		return null;
	}

	public static interface func {

	}

	public static interface f {

	}

	public static interface Enumerate<T> {
		void visit(int index, T t);
	}

	public static <T> void enumerate(Enumerate<T> e, Collection<T> c) {
		int index = 0;
		for (T t : c) {
			e.visit(index, t);
			index++;
		}

	}

	public static <T> void enumerate(Enumerate<T> e, T[] c) {
		int index = 0;
		for (T t : c) {
			e.visit(index, t);
			index++;
		}
	}

	public static void enumerate(Object func, Object methodName, Collection<?> c) {
		enumerate(fn(func, methodName), c);
	}

	public static void enumerate(Object func, Object[] c) {
		enumerate(f(func), c);
	}

	public static void enumerate(Object func, Object methodName, Object[] c) {
		enumerate(fn(func, methodName), c);
	}

	public static void enumerate(Object func, Collection<?> c) {
		enumerate(f(func), c);
	}

	public static void enumerate(Function<?> f, Collection<?> c) {
		int index = 0;
		for (Object o : c) {
			f.execute(index, o);
			index++;
		}
	}

	public static void enumerate(Function<?> f, Object[] c) {
		int index = 0;
		for (Object t : c) {
			f.execute(index, t);
			index++;
		}
	}

	public interface Filter<T> {
		boolean filter(T x);
	}

	public interface dFilter {
		boolean filter(double x);
	}

	public interface fFilter {
		boolean filter(float x);
	}

	public interface lFilter {
		boolean filter(long x);
	}

	public interface iFilter {
		boolean filter(int x);
	}

	public interface bFilter {
		boolean filter(byte x);
	}

	public interface sFilter {
		boolean filter(short x);
	}

	public interface cFilter {
		boolean filter(char x);
	}

	private static class dFilterInvoker implements dFilter {
		Function<?> f;

		private dFilterInvoker(FunctionImpl<?> f) {
			this.f = f;
		}

		@Override
		public boolean filter(double x) {
			return (Boolean) f.execute(x);
		}
	}

	private static class fFilterInvoker implements fFilter {
		Function<?> f;

		private fFilterInvoker(FunctionImpl<?> f) {
			this.f = f;
		}

		@Override
		public boolean filter(float x) {
			return (Boolean) f.execute(x);
		}
	}

	private static class lFilterInvoker implements lFilter {
		Function<?> f;

		private lFilterInvoker(FunctionImpl<?> f) {
			this.f = f;
		}

		@Override
		public boolean filter(long x) {
			return (Boolean) f.execute(x);
		}
	}

	private static class iFilterInvoker implements iFilter {
		Function<?> f;

		private iFilterInvoker(FunctionImpl<?> f) {
			this.f = f;
		}

		@Override
		public boolean filter(int x) {
			return (Boolean) f.execute(x);
		}
	}

	private static class sFilterInvoker implements sFilter {
		Function<?> f;

		private sFilterInvoker(FunctionImpl<?> f) {
			this.f = f;
		}

		@Override
		public boolean filter(short x) {
			return (Boolean) f.execute(x);
		}
	}

	private static class bFilterInvoker implements bFilter {
		Function<?> f;

		private bFilterInvoker(FunctionImpl<?> f) {
			this.f = f;
		}

		@Override
		public boolean filter(byte x) {
			return (Boolean) f.execute(x);
		}
	}

	private static class cFilterInvoker implements cFilter {
		Function<?> f;

		private cFilterInvoker(FunctionImpl<?> f) {
			this.f = f;
		}

		@Override
		public boolean filter(char x) {
			return (Boolean) f.execute(x);
		}
	}

	public static double[] dfilter(Function<?> f, double... array) {
		return dfilter(new dFilterInvoker((FunctionImpl<?>) f), array);
	}

	public static float[] ffilter(Function<?> f, float... array) {
		return ffilter(new fFilterInvoker((FunctionImpl<?>) f), array);
	}

	public static long[] lfilter(Function<?> f, long... array) {
		return lfilter(new lFilterInvoker((FunctionImpl<?>) f), array);
	}

	public static int[] ifilter(Function<?> f, int... array) {
		return ifilter(new iFilterInvoker((FunctionImpl<?>) f), array);
	}

	public static short[] sfilter(Function<?> f, short... array) {
		return sfilter(new sFilterInvoker((FunctionImpl<?>) f), array);
	}

	public static byte[] bfilter(Function<?> f, byte... array) {
		return bfilter(new bFilterInvoker((FunctionImpl<?>) f), array);
	}

	public static char[] cfilter(Function<?> f, char... array) {
		return cfilter(new cFilterInvoker((FunctionImpl<?>) f), array);
	}

	public static double[] dfilter(Object func, String name, double... array) {
		return dfilter(fn(func, name), array);
	}

	public static float[] ffilter(Object func, String name, float... array) {
		return ffilter(fn(func, name), array);
	}

	public static long[] lfilter(Object func, String name, long... array) {
		return lfilter(fn(func, name), array);
	}

	public static int[] ifilter(Object func, String name, int... array) {
		return ifilter(fn(func, name), array);
	}

	public static short[] sfilter(Object func, String name, short... array) {
		return sfilter(fn(func, name), array);
	}

	public static byte[] bfilter(Object func, String name, byte... array) {
		return bfilter(fn(func, name), array);
	}

	public static char[] cfilter(Object func, String name, char... array) {
		return cfilter(fn(func, name), array);
	}

	public static double[] dfilter(Object func, double... array) {
		return dfilter(f(func), array);
	}

	public static float[] ffilter(Object func, float... array) {
		return ffilter(f(func), array);
	}

	public static long[] lfilter(Object func, long... array) {
		return lfilter(f(func), array);
	}

	public static int[] ifilter(Object func, int... array) {
		return ifilter(f(func), array);
	}

	public static short[] sfilter(Object func, short... array) {
		return sfilter(f(func), array);
	}

	public static byte[] bfilter(Object func, byte... array) {
		return bfilter(f(func), array);
	}

	public static char[] cfilter(Object func, char... array) {
		return cfilter(f(func), array);
	}

	public static double[] dfilter(dFilter f, double... array) {
		double[] items = new double[array.length];
		double value;

		int newIndex = 0;
		for (int index = 0; index < array.length; index++) {
			value = array[index];
			if (f.filter(value)) {
				items[newIndex] = value;
				newIndex++;
			}
		}

		double[] results = new double[newIndex];
		System.arraycopy(items, 0, results, 0, newIndex); // Is arraycopy faster
															// than a good for
															// loop, good
															// benchmark test
		return results;

	}
	


	public static long[] lfilter(lFilter f, long... array) {
		long[] items = new long[array.length];
		int newIndex = 0;
		for (int index = 0; index < array.length; index++) {
			long value = array[index];
			if (f.filter(value)) {
				items[newIndex] = value;
				newIndex++;
			}
		}

		long[] results = new long[newIndex];
		System.arraycopy(items, 0, results, 0, newIndex); // Is arraycopy faster
															// than a good for
															// loop, good
															// benchmark test
		return results;

	}

	public static float[] ffilter(fFilter f, float... array) {
		float[] items = new float[array.length];
		int newIndex = 0;
		for (int index = 0; index < array.length; index++) {
			float value = array[index];
			if (f.filter(value)) {
				items[newIndex] = value;
				newIndex++;
			}
		}

		float[] results = new float[newIndex];
		System.arraycopy(items, 0, results, 0, newIndex); // Is arraycopy faster
															// than a good for
															// loop, good
															// benchmark test
		return results;

	}

	public static int[] ifilter(iFilter f, int... array) {
		int[] items = new int[array.length];
		int newIndex = 0;
		for (int index = 0; index < array.length; index++) {
			int value = array[index];
			if (f.filter(value)) {
				items[newIndex] = value;
				newIndex++;
			}
		}

		int[] results = new int[newIndex];
		System.arraycopy(items, 0, results, 0, newIndex); // Is arraycopy faster
															// than a good for
															// loop, good
															// benchmark test
		return results;

	}

	public static short[] sfilter(sFilter f, short... array) {
		short[] items = new short[array.length];
		int newIndex = 0;
		for (short index = 0; index < array.length; index++) {
			short value = array[index];
			if (f.filter(value)) {
				items[newIndex] = value;
				newIndex++;
			}
		}

		short[] results = new short[newIndex];
		System.arraycopy(items, 0, results, 0, newIndex); // Is arraycopy faster
															// than a good for
															// loop, good
															// benchmark test
		return results;

	}

	public static byte[] bfilter(bFilter f, byte... array) {
		byte[] items = new byte[array.length];
		int newIndex = 0;
		for (short index = 0; index < array.length; index++) {
			byte value = array[index];
			if (f.filter(value)) {
				items[newIndex] = value;
				newIndex++;
			}
		}

		byte[] results = new byte[newIndex];
		System.arraycopy(items, 0, results, 0, newIndex); // Is arraycopy faster
															// than a good for
															// loop, good
															// benchmark test
		return results;

	}

	public static char[] cfilter(cFilter f, char... array) {
		char[] items = new char[array.length];
		int newIndex = 0;
		for (short index = 0; index < array.length; index++) {
			char value = array[index];
			if (f.filter(value)) {
				items[newIndex] = value;
				newIndex++;
			}
		}

		char[] results = new char[newIndex];
		System.arraycopy(items, 0, results, 0, newIndex); // Is arraycopy faster
															// than a good for
															// loop, good
															// benchmark test
		return results;

	}

	public static Collection<?> filter(Function<?> f, Collection<?> c) {
		return gfilter(f, c);
	}

	public static Collection<?> filter(Function<?> f, Object[] c) {
		return gfilter(f, c);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Collection<?> filter(Filter f, Collection c) {
		return gfilter(f, c);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object[] filter(Filter f, Object... c) {
		return gfilter(f, c);
	}

	public static Collection<?> filter(Object f, Object[] in) {
		return doGfilter(f(f), list(in));
	}

	public static Collection<?> filter(Object f, Object name, Collection<?> in) {
		return doGfilter(fn(f, name), list(in));
	}

	private static <T> Collection<T> doGfilter(Function<?> f, List<T> in) {
		ListIterator<T> listIterator = in.listIterator();
		while (listIterator.hasNext()) {
			Boolean b = (Boolean) f.execute(listIterator.next());
			if (!b) {
				listIterator.remove();
			}
		}
		return in;
	}

	private static <T> List<T> doGfilter(Filter<T> f, List<T> in) {
		ListIterator<T> listIterator = in.listIterator();
		while (listIterator.hasNext()) {
			boolean b = f.filter(listIterator.next());
			if (!b) {
				listIterator.remove();
			}
		}
		return in;
	}

	private static <T> T[] doGfilterAsArray(Filter<T> f, List<T> in) {
		List<T> list = doGfilter(f, in);
		return array(list);
	}

	public static <T> Collection<T> gfilter(Object f, T[] in) {
		return doGfilter(f(f), list(in));
	}

	public static <T> Collection<T> gfilter(Object f, Object name,
			Collection<T> in) {
		return doGfilter(fn(f, name), list(in));
	}

	public static <T> Collection<T> gfilter(Function<?> f, T[] in) {
		return doGfilter(f, list(in));
	}

	public static <T> Collection<T> gfilter(Function<?> f, Collection<T> in) {
		return doGfilter(f, list(in));
	}

	public static <T> T[] gfilter(Filter<T> f, T... in) {
		return doGfilterAsArray(f, list(in));
	}

	public static <T> Collection<T> gfilter(Filter<T> f, Collection<T> in) {
		return doGfilter(f, list(in));
	}

	public interface Converter<TO, FROM> {
		TO convert(FROM from);
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

	public static <TO, FROM> List<TO> map(Converter<TO, FROM> converter,
			List<FROM> fromList) {

		ArrayList<TO> toList = new ArrayList<TO>(fromList.size());

		for (FROM from : fromList) {
			toList.add(converter.convert(from));
		}

		return toList;
	}

	public static List<?> map(Function<?> f, Collection<?> c) {
		return gmap(f, c);
	}

	public static List<?> map(Function<?> f, Object[] array) {
		return gmap(f, list(array));
	}

	public static List<?> map(Object that, String methodName, Collection<?> c) {
		return gmap(fn(that, methodName), c);
	}

	public static List<?> map(Object that, String methodName, Object[] array) {
		return gmap(fn(that, methodName), list(array));
	}

	public static List<?> map(Object that, Collection<?> c) {
		return gmap(f(that), c);
	}

	public static List<?> map(Object that, Object[] array) {
		return gmap(f(that), list(array));
	}

	@SuppressWarnings("unchecked")
	public static <IN, OUT> List<OUT> gmap(Class<OUT> returnType, Object that,
			Object name, IN[] c) {
		return gmap((Function<OUT>) fn(that, name), list(c));
	}

	@SuppressWarnings("unchecked")
	public static <IN, OUT> List<OUT> gmap(Class<OUT> returnType, Object that,
			IN[] c) {
		return gmap((Function<OUT>) f(that), list(c));
	}

	public static <IN, OUT> List<OUT> gmap(Function<OUT> f, IN[] c) {
		return gmap(f, c);
	}

	@SuppressWarnings("unchecked")
	public static <IN, OUT> List<OUT> gmap(Class<OUT> returnType, Object that,
			Object name, Collection<IN> c) {
		return gmap((Function<OUT>) fn(that, name), c);
	}

	@SuppressWarnings("unchecked")
	public static <IN, OUT> List<OUT> gmap(Class<OUT> returnType, Object that,
			Collection<IN> c) {
		return gmap((Function<OUT>) f(that), c);
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
	public static <T> T greduce(Function<?> f, T array) {
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

		if (length == 0) {
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

	public static <T> void expect(Appendable out, T ex, T v) {
		if (ex == null && v == null) {
			return;
		}

		if (ex == null && v != null) {
			fprintf(out, "expected was null, but value was %s", v);
		}

		if (!ex.equals(v)) {
			fprintf(out, "expected was %s, but value was %s", ex, v);
		}

	}

	public static <T> void expect(String msg, T ex, T v) {
		if (ex == null && v == null) {
			return;
		}

		if (ex == null && v != null) {
			throw new AssertionException(sprintf(
					"%s | expected \n null, but value was \n #%s#", msg, v));
		}

		if (!ex.equals(v)) {
			throw new AssertionException(sprintf(
					"%s | expected \n#%s#, but value was \n#%s#", msg, ex, v));
		}

	}

	public static <T> void expect(Appendable out, String msg, T ex, T v) {
		if (ex == null && v == null) {
			return;
		}

		if (ex == null && v != null) {
			fprintf(out, "%s | expected null, but value was #%s#", msg, v);
		}

		if (!ex.equals(v)) {
			fprintf(out, "%s | expected \n#%s#, but value was \n#%s#", msg, ex,
					v);
		}

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

	public static char[] range(char start, char end) {
		StringBuilder builder = new StringBuilder();
		for (char c = start; c <= end; c++) {
			builder.append(c);
		}
		return builder.toString().toCharArray();
	}

	public static String[] split(String str, String splitBy) {
		char[] array = str.toCharArray();
		char[] split = splitBy.toCharArray();
		String[] strings = split(array,split);
		return strings;
	}
	
	public static String[] split(String str, char c) {
		char[] array = str.toCharArray();
		String[] strings = split(array,c);
		return strings;
	}


	public static String repr(char c) {
		return "'" + c + "'";
	}

	public static String[] toLines(final char[] buffer) {
		List<String> list = new ArrayList<String>(100);
		StringBuilder builder = new StringBuilder(256);
		String str = null;

		for (int index = 0; index < buffer.length; index++) {
			char c = buffer[index];
			if (c == '\r') {
				if (index < buffer.length) {
					index++;
					c = buffer[index];
					if (c != '\n') {
						index--;
					} else {
						c = buffer[index];
					}
				}
				str = builder.toString();
				builder.setLength(0);
				list.add(str);
				continue;
			} else if (c == '\n') {
				str = builder.toString();
				builder.setLength(0);
				list.add(str);
				continue;
			} else {
				builder.append(c);
			}
		}

		return list.toArray(new String[list.size()]);

	}
	
	public static String[] split(final char[] buffer, final char split) {
		List<String> list = new ArrayList<String>(100);
		StringBuilder builder = new StringBuilder(256);
		String str = null;
		for (int index = 0; index < buffer.length; index++) {
			char c = buffer[index];
			if (c == split) {
				str = builder.toString();
				builder.setLength(0);
				list.add(str);
				continue;
			} else {
				builder.append(c);
			}
		}
		return list.toArray(new String[list.size()]);
	}

	public static String[] split(final char[] buffer, final char[] split) {
		List<String> list = new ArrayList<String>(100);
		StringBuilder builder = new StringBuilder(256);
		String str = null;
		for (int index = 0; index < buffer.length; index++) {
			char c = buffer[index];
			if (isIn(c,split)) {
				str = builder.toString();
				builder.setLength(0);
				list.add(str);
				while(isIn(c, split) && index < buffer.length) {
					index++;
				}
				continue;
			} else {
				builder.append(c);
			}
		}
		return list.toArray(new String[list.size()]);
	}

	public static String[] toLines(String str) {
		return toLines(str.toCharArray());
	}

	public static String[] toLines(StringBuilder b) {
		char[] buf = new char[b.length()];
		b.getChars(0, buf.length, buf, 0);
		return toLines(buf);
	}

	public static String[] toLines(CharSequence cs) {
		return toLines(cs.toString());
	}

	public static String str(char... chars) {
		return string(chars);
	}

	public static char[] chars(String str) {
		if (str != null) {
			return str.toCharArray();
		} else {
			return new char[0];
		}
	}

	public static char[] chars(char... chars) {
		return str(chars).toCharArray();
	}

	public static String string(char... chars) {
		if (chars != null) {
			return String.valueOf(chars);
		} else {
			return "";
		}

	}

	public static String string(int start, int stop, char... chars) {
		if (chars != null) {
			return String.valueOf(chars, start, stop);
		} else {
			return "";
		}

	}

	public static String str(Object obj) {
		return string(obj);
	}

	public static String string(Object obj) {
		return obj.toString();
	}

	public static boolean isIn(char c, char... array) {
		for (int index = 0; index < array.length; index++) {
			if (c == array[index]) {
				return true;
			}
		}
		return false;

	}

	public static List<Character> list(char... chars) {
		return ls(chars);
	}

	public static char[] add(char[] chars, char... chars2) {
		List<Character> list = ls(chars);
		list.addAll(ls(chars2));
		char[] chars3 = new char[chars.length + chars2.length];
		int index = 0;
		for (char c : list) {
			chars3[index] = c;
			index++;
		}
		return chars3;
	}

	public static List<Character> ls(char... chars) {
		List<Character> ls = new ArrayList<Character>();
		for (char c : chars) {
			ls.add(c);
		}
		return ls;
	}

	public static <T> void checkArgumentsForNulls(T... objects) {
		if (objects == null) {
			throw new IllegalArgumentException(
					" nulls arguments are not allowed ");

		}
		for (Object obj : objects) {
			if (obj == null) {
				throw new IllegalArgumentException(
						" nulls arguments are not allowed ");
			}
		}
	}

	public static void notSupported() {
		throw new UnsupportedOperationException();
	}

	public static void complain(String msg) {
		throw new UnsupportedOperationException(msg);
	}

	public static FileObject<String> open(File file) {
		return IO.open(file);
	}

	public static FileObject<String> openString(String str) {
		return IO.openString(str);
	}

	public static FileObject<String> open(char[] buffer) {
		return IO.open(buffer);
	}

	public static String[] readLines(File file) {
		return IO.open(file).readLines();
	}

	public static String[] readAll(File file) {
		return IO.open(file).readLines();
	}

	public static String[] readLinesFromFile(String file) {
		return IO.open(new File(file)).readLines();
	}

	public static String[] readLinesAllFromFile(String file) {
		return IO.open(new File(file)).readLines();
	}

	public static FileObject<String> open(InputStream inputStream) {
		return IO.open(inputStream);
	}

	public static FileObject<String> open(Class<?> clz, String resource) {
		return IO.open(clz, resource);
	}

	public static FileObject<String> open(URL url) {
		return IO.open(url);
	}

	public static FileObject<String> open(String suri) {
		return IO.open(suri);
	}
	
	public static double sum(double...items) {
		double total = 0d;
		for (int index=0 ; index < items.length; index++){
			total += items[index];
		}
		return total;
	}
}
