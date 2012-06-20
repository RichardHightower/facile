package org.facile;
import static org.facile.Facile.*;

import java.util.Collection;


import org.junit.Test;
@SuppressWarnings("unused")
public class FilterTest {
	
	Filter<Double> under100D = new Filter<Double>()  {

		@Override
		public boolean filter(Double x) {
			return x<100;
		}
		
	};
	Filter<Long> under100L = new Filter<Long>()  {

		@Override
		public boolean filter(Long x) {
			return x<100;
		}
		
	};
	Filter<Float> under100F = new Filter<Float>()  {

		@Override
		public boolean filter(Float x) {
			return x<100;
		}
		
	};
	Filter<Integer> under100I = new Filter<Integer>()  {

		@Override
		public boolean filter(Integer x) {
			return x<100;
		}
		
	};
	Filter<Byte> under100B = new Filter<Byte>()  {

		@Override
		public boolean filter(Byte x) {
			return x<100;
		}
		
	};
	Filter<Character> under100C = new Filter<Character>()  {

		@Override
		public boolean filter(Character x) {
			return x<'a';
		}
		
	};


	dFilter under100d = new dFilter() {
		
		@Override
		public boolean filter(double x) {
			return x<100;
		}
	};
	
	lFilter under100l = new lFilter() {
		
		@Override
		public boolean filter(long x) {
			return x<100;
		}
	};

	
	fFilter under100f = new fFilter() {
		
		@Override
		public boolean filter(float x) {
			return x<100;
		}
	};

	
	iFilter under100i = new iFilter() {
		
		@Override
		public boolean filter(int x) {
			return x<100;
		}
	};

	sFilter under100s = new sFilter() {
		
		@Override
		public boolean filter(short x) {
			return x<100;
		}
	};
	
	bFilter under100b = new bFilter() {
		
		@Override
		public boolean filter(byte x) {
			return x<100;
		}
	};
	cFilter under100c = new cFilter()  {

		@Override
		public boolean filter(char x) {
			return x<'a';
		}
		
	};



	@Test
	public void testD() {
		double[] items = dfilter(under100d, 10, 20, 30, 40, 50, 500, 1000, 1);
		expect("", 50.0, items[4]);
		expect("", 1.0, items[5]);
		
	}

	@Test
	public void testBigD() {
		Double[] items = gfilter(under100D, 10d, 20d, 30d, 40d, 50d, 500d, 1000d, 1d);	
		expect("", 50d, items[4]);
		expect("", 1d, items[5]);

	}

	@Test
	public void testD2() {
		double[] items = dfilter(new f() { boolean f(double d){ return d<100;}}, 10, 20, 30, 40, 50, 500, 1000, 1);
		expect("", 50.0, items[4]);
		expect("", 1.0, items[5]);
		
	}
	
	@Test
	public void testD3() {
		class Under100 {
			boolean under100(double d) {
				return d < 100;
			}
		}
		double[] items = dfilter(new Under100(), "under100", 10, 20, 30, 40, 50, 500, 1000, 1);
		expect("", 50.0, items[4]);
		expect("", 1.0, items[5]);
		
	}


	@Test
	public void testL() {
		long[] items = lfilter(under100l, 10l, 20l, 30l, 40l, 50l, 500l, 1000l, 1l);
		expect("", 50l, items[4]);
		expect("", 1l, items[5]);
		
	}
	
	@Test
	public void testBigL() {
		Long[] items = gfilter(under100L, 10l, 20l, 30l, 40l, 50l, 500l, 1000l, 1l);	
		expect("", 50l, items[4]);
		expect("", 1l, items[5]);

	}

	@Test
	public void testL2() {
		long[] items = lfilter(new f() { boolean f(long d){ return d<100;}}, 10l, 20l, 30l, 40l, 50l, 500l, 1000l, 1l);
		expect("", 50l, items[4]);
		expect("", 1l, items[5]);
		
	}
	
	@Test
	public void testL3() {
		class Under100 {
			boolean under100(long d) {
				return d < 100;
			}
		}
		long[] items = lfilter(new Under100(), "under100", 10l, 20l, 30l, 40l, 50l, 500l, 1000l, 1l);
		expect("", 50l, items[4]);
		expect("", 1l, items[5]);
		
	}



	@Test
	public void testF() {
		float[] items = ffilter(under100f, 10, 20, 30, 40, 50, 500, 1000, 1);
		expect("", 50.0f, items[4]);
		expect("", 1.0f, items[5]);
		
	}
	
	@Test
	public void testBigF() {
		Float[] items = gfilter(under100F, 10f, 20f, 30f, 40f, 50f, 500f, 1000f, 1f);	
		expect("", 50f, items[4]);
		expect("", 1f, items[5]);

	}

	@Test
	public void testF2() {
		float[] items = ffilter(new f() { boolean f(float d){ return d<100;}}, 10f, 20f, 30f, 40f, 50f, 500f, 1000f, 1f);
		expect("", 50f, items[4]);
		expect("", 1f, items[5]);
		
	}
	
	@Test
	public void testF3() {
		class Under100 {
			boolean under100(float d) {
				return d < 100;
			}
		}
		float[] items = ffilter(new Under100(), "under100", 10f, 20f, 30f, 40f, 50f, 500f, 1000f, 1f);
		expect("", 50f, items[4]);
		expect("", 1f, items[5]);
		
	}



	@Test
	public void testI() {
		int[] items = ifilter(under100i, 10, 20, 30, 40, 50, 500, 1000, 1);
		expect("", 50, items[4]);
		expect("", 1, items[5]);
		
	}

	@Test
	public void testBigI() {
		Integer[] items = gfilter(under100I, 10, 20, 30, 40, 50, 500, 1000, 1);	
		expect("", 50, items[4]);
		expect("", 1, items[5]);

	}

	@Test
	public void testI2() {
		int[] items = ifilter(new f() { boolean f(int d){ return d<100;}}, 10, 20, 30, 40, 50, 500, 1000, 1);
		expect("", 50, items[4]);
		expect("", 1, items[5]);
		
	}
	
	@Test
	public void testI3() {
		class Under100 {
			boolean under100(int d) {
				return d < 100;
			}
		}
		int[] items = ifilter(new Under100(), "under100", 10, 20, 30, 40, 50, 500, 1000, 1);
		expect("", 50, items[4]);
		expect("", 1, items[5]);
		
	}

	
	@Test
	public void testC() {
		char[] items = cfilter(under100c, 'A', 'a', 'B', 'b');
		expect("", 'A', items[0]);
		expect("", 'B', items[1]);

	}
	
	@Test
	public void testBigC() {
		Character[] items = gfilter(under100C, 'A', 'a', 'B', 'b');
		expect("", 'A', items[0]);
		expect("", 'B', items[1]);

	}


	@Test
	public void testB() {
		byte[] items = bfilter(under100b, (byte)10, (byte)20, (byte)30, (byte)40, (byte)50, (byte)101, (byte)102, (byte)1);
		expect("", (byte)50, items[4]);
		expect("", (byte)1, items[5]);
		
	}
	@Test
	public void testS() {
		short[] items = sfilter(under100s, (short)10, (short)20, (short)30, (short)40, (short)50, (short)101, (short)102, (short)1);
		expect("", (short)50, items[4]);
		expect("", (short)1, items[5]);
		
	}

}
