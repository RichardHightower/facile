package org.facile;
import static org.facile.Facile.*;


import org.junit.Test;

public class Filter {
	
	dFilter under100d = new dFilter() {
		
		@Override
		public boolean filter(double x) {
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
		print(ls(items));
		expect("", 50.0, items[4]);
		expect("", 1.0, items[5]);
		
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
