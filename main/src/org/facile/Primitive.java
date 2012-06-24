package org.facile;

import java.util.Collection;
import java.util.List;

import org.facile.Facile.Func;
import static org.facile.Facile.fn;
import static org.facile.Facile.f;
import org.facile.Reflection.FuncImpl;


public class Primitive {
	public static interface dEnumerate {
		void visit(int index, double x);
	}
	public static interface fEnumerate {
		void visit(int index, float x);
	}
	public static interface lEnumerate {
		void visit(int index, long x);
	}
	public static interface iEnumerate {
		void visit(int index, int x);
	}
	public static interface sEnumerate {
		void visit(int index, short x);
	}
	public static interface bEnumerate {
		void visit(int index, byte x);
	}
	public static interface cEnumerate {
		void visit(int index, char x);
	}

	private static class dEnumerateInvoker implements dEnumerate {
		Func<?> f;

		dEnumerateInvoker (Func<?> f) {
			this.f = f;
		}
		@Override
		public void visit(int index, double x) {
			f.execute(index, x);
		}
	}
	private static class fEnumerateInvoker implements fEnumerate {
		Func<?> f;

		fEnumerateInvoker (Func<?> f) {
			this.f = f;
		}
		@Override
		public void visit(int index, float x) {
			f.execute(index, x);
		}
	}
	private static class lEnumerateInvoker implements lEnumerate {
		Func<?> f;

		lEnumerateInvoker (Func<?> f) {
			this.f = f;
		}
		@Override
		public void visit(int index, long x) {
			f.execute(index, x);
		}
	}
	private static class iEnumerateInvoker implements iEnumerate {
		Func<?> f;

		iEnumerateInvoker (Func<?> f) {
			this.f = f;
		}
		@Override
		public void visit(int index, int x) {
			f.execute(index, x);
		}
	}

	private static class sEnumerateInvoker implements sEnumerate {
		Func<?> f;

		sEnumerateInvoker (Func<?> f) {
			this.f = f;
		}
		@Override
		public void visit(int index, short x) {
			f.execute(index, x);
		}
	}
	private static class bEnumerateInvoker implements bEnumerate {
		Func<?> f;

		bEnumerateInvoker (Func<?> f) {
			this.f = f;
		}
		@Override
		public void visit(int index, byte x) {
			f.execute(index, x);
		}
	}
	private static class cEnumerateInvoker implements cEnumerate {
		Func<?> f;

		cEnumerateInvoker (Func<?> f) {
			this.f = f;
		}
		@Override
		public void visit(int index, char x) {
			f.execute(index, x);
		}
	}

	private static class sFilterInvoker implements sFilter {
		Func<?> f;

		private sFilterInvoker(FuncImpl<?> f) {
			this.f = f;
		}

		@Override
		public boolean filter(short x) {
			return (Boolean) f.execute(x);
		}
	}

	private static class bFilterInvoker implements bFilter {
		Func<?> f;

		private bFilterInvoker(FuncImpl<?> f) {
			this.f = f;
		}

		@Override
		public boolean filter(byte x) {
			return (Boolean) f.execute(x);
		}
	}

	private static class cFilterInvoker implements cFilter {
		Func<?> f;

		private cFilterInvoker(FuncImpl<?> f) {
			this.f = f;
		}

		@Override
		public boolean filter(char x) {
			return (Boolean) f.execute(x);
		}
	}

	public static double[] dfilter(Func<?> f, double... array) {
		return dfilter(new dFilterInvoker((FuncImpl<?>) f), array);
	}

	public static float[] ffilter(Func<?> f, float... array) {
		return ffilter(new fFilterInvoker((FuncImpl<?>) f), array);
	}

	public static long[] lfilter(Func<?> f, long... array) {
		return lfilter(new lFilterInvoker((FuncImpl<?>) f), array);
	}

	public static int[] ifilter(Func<?> f, int... array) {
		return ifilter(new iFilterInvoker((FuncImpl<?>) f), array);
	}

	public static short[] sfilter(Func<?> f, short... array) {
		return sfilter(new sFilterInvoker((FuncImpl<?>) f), array);
	}

	public static byte[] bfilter(Func<?> f, byte... array) {
		return bfilter(new bFilterInvoker((FuncImpl<?>) f), array);
	}

	public static char[] cfilter(Func<?> f, char... array) {
		return cfilter(new cFilterInvoker((FuncImpl<?>) f), array);
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

	public static void enumerate(dEnumerate e, Collection<Double> c) {
		int index = 0;
		for (double i : c) {
			e.visit(index, i);
			index++;
		}
	}
	public static void enumerate(fEnumerate e, Collection<Float> c) {
		int index = 0;
		for (float i : c) {
			e.visit(index, i);
			index++;
		}
	}
	public static void enumerate(lEnumerate e, Collection<Long> c) {
		int index = 0;
		for (long i : c) {
			e.visit(index, i);
			index++;
		}
	}
	public static void enumerate(iEnumerate e, Collection<Integer> c) {
		int index = 0;
		for (int i : c) {
			e.visit(index, i);
			index++;
		}
	}
	public static void enumerate(sEnumerate e, Collection<Short> c) {
		int index = 0;
		for (short i : c) {
			e.visit(index, i);
			index++;
		}
	}
	public static void enumerate(bEnumerate e, Collection<Byte> c) {
		int index = 0;
		for (byte i : c) {
			e.visit(index, i);
			index++;
		}
	}
	public static void enumerate(cEnumerate e, Collection<Character> c) {
		int index = 0;
		for (char i : c) {
			e.visit(index, i);
			index++;
		}
	}

	public static void enumerate(dEnumerate e, List<Double> l) {
		 enumerate(e, darray(l));
	}
	public static void enumerate(fEnumerate e, List<Float> l) {
		 enumerate(e, farray(l));
	}
	public static void enumerate(lEnumerate e, List<Long> l) {
		 enumerate(e, larray(l));
	}
	public static void enumerate(iEnumerate e, List<Integer> l) {
		 enumerate(e, iarray(l));
	}
	public static void enumerate(sEnumerate e, List<Short> l) {
		 enumerate(e, sarray(l));
	}
	public static void enumerate(bEnumerate e, List<Byte> l) {
		 enumerate(e, barray(l));
	}
	public static void enumerate(cEnumerate e, List<Character> l) {
		 enumerate(e, carray(l));
	}

	public static void enumerate(dEnumerate e, double...array) {
		for (int index=0; index<array.length; index++) {
			e.visit(index, array[index]);
		}
	}
	public static void enumerate(fEnumerate e, float...array) {
		for (int index=0; index<array.length; index++) {
			e.visit(index, array[index]);
		}
	}
	public static void enumerate(lEnumerate e, long...array) {
		for (int index=0; index<array.length; index++) {
			e.visit(index, array[index]);
		}
	}
	public static void enumerate(iEnumerate e, int...array) {
		for (int index=0; index<array.length; index++) {
			e.visit(index, array[index]);
		}
	}
	public static void enumerate(sEnumerate e, short...array) {
		for (int index=0; index<array.length; index++) {
			e.visit(index, array[index]);
		}
	}
	public static void enumerate(bEnumerate e, byte...array) {
		for (int index=0; index<array.length; index++) {
			e.visit(index, array[index]);
		}
	}
	public static void enumerate(cEnumerate e, char...array) {
		for (int index=0; index<array.length; index++) {
			e.visit(index, array[index]);
		}
	}

	public static void enumerate(Object func, double...array) {
		dEnumerateInvoker dEnumerateInvoker = new dEnumerateInvoker(f(func));
		enumerate(dEnumerateInvoker, array);
	}
	public static void enumerate(Object func, float...array) {
		fEnumerateInvoker fEnumerateInvoker = new fEnumerateInvoker(f(func));
		enumerate(fEnumerateInvoker, array);
	}
	public static void enumerate(Object func, long...array) {
		lEnumerateInvoker lEnumerateInvoker = new lEnumerateInvoker(f(func));
		enumerate(lEnumerateInvoker, array);
	}
	public static void enumerate(Object func, int...array) {
		iEnumerateInvoker iEnumerateInvoker = new iEnumerateInvoker(f(func));
		enumerate(iEnumerateInvoker, array);
	}
	public static void enumerate(Object func, short...array) {
		sEnumerateInvoker inv = new sEnumerateInvoker(f(func));
		enumerate(inv, array);
	}
	public static void enumerate(Object func, byte...array) {
		bEnumerateInvoker inv = new bEnumerateInvoker(f(func));
		enumerate(inv, array);
	}
	public static void enumerate(Object func, char...array) {
		cEnumerateInvoker inv = new cEnumerateInvoker(f(func));
		enumerate(inv, array);
	}

	public static void enumerate(Object func, String funcName, double...array) {
		dEnumerateInvoker inv = new dEnumerateInvoker(fn(func, funcName));
		enumerate(inv, array);
	}
	public static void enumerate(Object func, String funcName, float...array) {
		dEnumerateInvoker inv = new dEnumerateInvoker(fn(func, funcName));
		enumerate(inv, array);
	}
	public static void enumerate(Object func, String funcName, long...array) {
		lEnumerateInvoker inv = new lEnumerateInvoker(fn(func, funcName));
		enumerate(inv, array);
	}
	public static void enumerate(Object func, String funcName, int...array) {
		iEnumerateInvoker inv = new iEnumerateInvoker(fn(func, funcName));
		enumerate(inv, array);
	}
	public static void enumerate(Object func, String funcName, short...array) {
		sEnumerateInvoker inv = new sEnumerateInvoker(fn(func, funcName));
		enumerate(inv, array);
	}
	public static void enumerate(Object func, String funcName, byte...array) {
		bEnumerateInvoker inv = new bEnumerateInvoker(fn(func, funcName));
		enumerate(inv, array);
	}
	public static void enumerate(Object func, String funcName, char...array) {
		cEnumerateInvoker inv = new cEnumerateInvoker(fn(func, funcName));
		enumerate(inv, array);
	}

	private static class fFilterInvoker implements fFilter {
		Func<?> f;

		private fFilterInvoker(FuncImpl<?> f) {
			this.f = f;
		}

		@Override
		public boolean filter(float x) {
			return (Boolean) f.execute(x);
		}
	}

	private static class lFilterInvoker implements lFilter {
		Func<?> f;

		private lFilterInvoker(FuncImpl<?> f) {
			this.f = f;
		}

		@Override
		public boolean filter(long x) {
			return (Boolean) f.execute(x);
		}
	}

	private static class iFilterInvoker implements iFilter {
		Func<?> f;

		private iFilterInvoker(FuncImpl<?> f) {
			this.f = f;
		}

		@Override
		public boolean filter(int x) {
			return (Boolean) f.execute(x);
		}
	}

	private static class dFilterInvoker implements dFilter {
		Func<?> f;

		private dFilterInvoker(FuncImpl<?> f) {
			this.f = f;
		}

		@Override
		public boolean filter(double x) {
			return (Boolean) f.execute(x);
		}
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

	
	public static double[] darray(List<Double> list) {
		double [] array = new double[list.size()];
		Double [] org = list.toArray(new Double[list.size()]);
		for (int index=0;index<array.length;index++) {
			array[index] = org[index];
		}
		return array;
	}
	public static float[] farray(List<Float> list) {
		float [] array = new float[list.size()];
		Float [] org = list.toArray(new Float[list.size()]);
		for (int index=0;index<array.length;index++) {
			array[index] = org[index];
		}
		return array;
	}

	public static long[] larray(List<Long> list) {
		long [] array = new long[list.size()];
		Long [] org = list.toArray(new Long[list.size()]);
		for (int index=0;index<array.length;index++) {
			array[index] = org[index];
		}
		return array;
	}
	public static int[] iarray(List<Integer> list) {
		int [] array = new int[list.size()];
		Integer [] org = list.toArray(new Integer[list.size()]);
		for (int index=0;index<array.length;index++) {
			array[index] = org[index];
		}
		return array;
	}
	public static short[] sarray(List<Short> list) {
		short [] array = new short[list.size()];
		Short [] org = list.toArray(new Short[list.size()]);
		for (int index=0;index<array.length;index++) {
			array[index] = org[index];
		}
		return array;
	}
	public static byte[] barray(List<Byte> list) {
		byte [] array = new byte[list.size()];
		Byte [] org = list.toArray(new Byte[list.size()]);
		for (int index=0;index<array.length;index++) {
			array[index] = org[index];
		}
		return array;
	}
	public static char[] carray(List<Character> list) {
		char [] array = new char[list.size()];
		Character [] org = list.toArray(new Character[list.size()]);
		for (int index=0;index<array.length;index++) {
			array[index] = org[index];
		}
		return array;
	}

}
