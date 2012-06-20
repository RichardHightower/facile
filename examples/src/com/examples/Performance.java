package com.examples;

import static org.facile.Facile.*;

@SuppressWarnings("unused")
public class Performance {
	static long start;
	static long stop;
	
	static double [] items = new double [2000000];
	static Double [] itemsD = new Double [2000000];
	static double expected = 1.71428200006E11; //2000000
	//static double expected = 1.7141914290000002E9; //at 200000
	//static double expected = 171349.5; //2000
	static Object obj = new f() {
		boolean f(double x){return !(Math.floor(x) % 7==0);}
	};
	
	@SuppressWarnings("rawtypes")
	static Function myFunc;
	
	static {
		
		myFunc = f(obj);

		double begin = 0.1;
		for (int index = 0; index < items.length; index++) {
			itemsD[index]=items[index] = begin * index;
		}
	}
	
	public static boolean myfilter(double x) {
		return !(Math.floor(x) % 7==0);

	}
	public static final double forEach1(double [] items) {
		double total = 0d;
		
		for (double i : items) {			
			if (myfilter(i)) {
				total+=i;
			} 
		}
		return total;
	}
	
	public static final double forEach2(double [] items) {
		double total = 0d;
		
		for (double i : items) {			
			if (!(Math.floor(i) % 7==0)) {
				total+=i;
			} 
		}
		return total;
	}

	public static final double forEach3(double [] items) {
		double total = 0d;
		double i;
		for (int index=0; index<items.length; index++) {
			i = items[index];
			if (!(Math.floor(i) % 7==0)) {
				total+=i;
			} 
		}
		return total;
	}

	public static final double forEach4() {
		double total = 0d;
		for (int index=0; index<items.length; index++) {
			double i = items[index];
			if (!(Math.floor(i) % 7==0)) {
				total+=i;
			} 
		}
		return total;
	}

	public static final double forEach5(Double [] items) {
		double total = 0d;
		double i;
		for (int index=0; index<items.length; index++) {
			i = items[index];
			if (!(Math.floor(i) % 7==0)) {
				total+=i;
			} 
		}
		return total;
	}

	public static final double forEach6(Double [] items) {
		Double total = 0d;
		Double i;
		for (int index=0; index<items.length; index++) {
			i = items[index];
			if (!(Math.floor(i) % 7==0)) {
				total+=i;
			} 
		}
		return total;
	}

	
	public static double easeFilter1(double[] items) {
		dFilter f = new dFilter() {
			public boolean filter(double x) {
				return !(Math.floor(x) % 7==0);
			}
		};
		
		return sum(dfilter(f,items));
	}

	public static double easeFilter2(double[] items) {
		dFilter f = new dFilter() {
			public boolean filter(double x) {
				return !(Math.floor(x) % 7==0);
			}
		};
		
		double[] filtered = dfilter(f,items);
		
		double sum = 0.0;
		
		for (int index =0; index < filtered.length; index++) {
			sum+=filtered[index];
		}
		
		return sum;
	}

	static dFilter f = new dFilter() {
		public boolean filter(double x) {
			return !(Math.floor(x) % 7==0);
		}
	};

	public static double easeFilter3(double[] items) {
		
		double[] filtered = dfilter(f,items);
		
		double sum = 0.0;
		
		for (int index =0; index < filtered.length; index++) {
			sum+=filtered[index];
		}
		
		return sum;
	}

	public static double easeFuncReflection(double[] items) {
		
		return sum(dfilter(new f(){ boolean f(double x){return !(Math.floor(x) % 7==0);}},items));
	}
	

	
	public static double easeSavedFunc(double[] items) {
		
		return sum(dfilter(myFunc,items));
	}


	public static void main (String [] args) {
		
		for (int i = 0; i < 3; i++) {
		print(mul(40, "*"));
		double result;
		
		start = System.nanoTime();
		result = forEach1(items);
		stop = System.nanoTime();
		expect("", expected, result);
		display("Simple forEach 1");

		start = System.nanoTime();
		result = forEach2(items);
		stop = System.nanoTime();
		expect("", expected, result);
		display("Simple forEach 2");

		start = System.nanoTime();
		result = forEach3(items);
		stop = System.nanoTime();
		expect("", expected, result);
		display("Simple forEach 3");

		start = System.nanoTime();
		result = forEach4();
		stop = System.nanoTime();
		expect("", expected, result);
		display("Simple forEach 4");

		start = System.nanoTime();
		result = forEach5(itemsD);
		stop = System.nanoTime();
		expect("", expected, result);
		display("Simple forEach 5");

		
		start = System.nanoTime();
		result = forEach6(itemsD);
		stop = System.nanoTime();
		expect("", expected, result);
		display("Simple forEach 6");

		
		start = System.nanoTime();
		result = easeFilter1(items);
		stop = System.nanoTime();
		expect("", expected, result);	
		display("Simple filter with sum");

		start = System.nanoTime();
		result = easeFilter2(items);
		stop = System.nanoTime();
		expect("", expected, result);	
		display("Simple filter no sum, tight loop");
		
		start = System.nanoTime();
		result = easeFilter3(items);
		stop = System.nanoTime();
		expect("", expected, result);	
		display("Simple filter no sum, tighter loop");


		start = System.nanoTime();
		result = easeFuncReflection(items);
		stop = System.nanoTime();
		expect("", expected, result);	
		display("Simple filter reflection inline");
		
		start = System.nanoTime();
		result = easeSavedFunc(items);
		stop = System.nanoTime();
		expect("", expected, result);	
		display("Simple filter reflection ahead of time");
		}


	}

	private static void display(String string) {
		print (string, (stop-start)/(1000.0 * 1000.0 * 1000.0) );
	}
}
