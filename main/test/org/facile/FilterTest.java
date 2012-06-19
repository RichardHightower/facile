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


	@Test
	public void testD() {
		double[] items = filter(under100d, 10, 20, 30, 40, 50, 500, 1000, 1);
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
		long[] items = filter(under100l, 10l, 20l, 30l, 40l, 50l, 500l, 1000l, 1l);
		expect("", 50l, items[4]);
		expect("", 1l, items[5]);
		
	}


	@Test
	public void testF() {
		float[] items = filter(under100f, 10, 20, 30, 40, 50, 500, 1000, 1);
		print(ls(items));
		expect("", 50.0f, items[4]);
		expect("", 1.0f, items[5]);
		
	}


	@Test
	public void testI() {
		int[] items = filter(under100i, 10, 20, 30, 40, 50, 500, 1000, 1);
		print(ls(items));
		expect("", 50, items[4]);
		expect("", 1, items[5]);
		
	}


	@Test
	public void testB() {
		byte[] items = filter(under100b, (byte)10, (byte)20, (byte)30, (byte)40, (byte)50, (byte)101, (byte)102, (byte)1);
		print(ls(items));
		expect("", (byte)50, items[4]);
		expect("", (byte)1, items[5]);
		
	}
	@Test
	public void testS() {
		short[] items = filter(under100s, (short)10, (short)20, (short)30, (short)40, (short)50, (short)101, (short)102, (short)1);
		print(ls(items));
		expect("", (short)50, items[4]);
		expect("", (short)1, items[5]);
		
	}

}
