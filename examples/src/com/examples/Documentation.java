package com.examples;

import static org.facile.Facile.*;

import java.util.List;
import java.util.Map;

//Ignore
//http://warpedvisions.org/projects/markdown-cheat-sheet/ Markdown cheatsheet

//Rule
//Remove expect(out

@SuppressWarnings("unused")
//Ignore
public class Documentation {

	//Ignore to }
	public static void main(String [] args) {
		StringBuilder out = new StringBuilder();
		mapAndListLiterals(out);
		mapAndListLiterals2(out);
		listComprehensionAndMaps(out);
		functionalProgramming(out);

		print ("test output" + mul(20, "*"));
		print(out);
	}

	//Ignore
	private static void mapAndListLiterals(StringBuilder out) {

		
		/* doc
These example were pulled right from the [Python tutorial on dictionaries](http://docs.python.org/tutorial/datastructures.html#dictionaries).
Python, Groovy, Perl and Ruby are a source of many ideas for this projects.
		 */
		
		/* doc		
		 Since Java is strongly typed you will have to declare a variable.
		 Since Facile uses generics, the IDE can usually do this for you.
		 */
		Map<String, Integer> tel;
		
		/* doc
		 #### Java versus Python side by side comparison using Ease library.
		 */

		//tel = {'jack': 4098, 'sape': 4139} //Py
		tel = mp("jack", 4098, "sape", 4139);

		// tel['guido'] = 4127 //Py
		tel.put("guido", 4127);

		// print (tel) //Py
		print(tel);
		// {'sape': 4139, 'guido': 4127, 'jack': 4098} //Py Out
		// {sape=4139, jack=4098, guido=4127}  //Java Out
		expect(out, "Basic map creation", "{sape=4139, jack=4098, guido=4127} ", sprint(tel));
		

		// print (tel['jack']) 							//Py
		print(tel.get("jack"));
		// 4098 									//Py Out
		expect(out, "Indexing", "4098 ", sprint(tel.get("jack")));
		// 4098 //Java Out

		// del tel['sape']								//Py
		tel.remove("sape");

		// tel['irv'] = 4127							//Py
		tel.put("irv", 4127);

		// print(tel)									//Py
		print(tel);
		// {'guido': 4127, 'irv': 4127, 'jack': 4098} //Py Out
		expect(out, "Basic map operations remove sape, add irv", "{irv=4127, jack=4098, guido=4127} ", sprint(tel));
		// {irv=4127, jack=4098, guido=4127} //Java Out

		/* doc
		 We are not looking to make Java look like Python just for the sake of it so we are not going to
		 replace 
		 */
		// print(tel.keys()) //Py
		print(tel.keySet());
		// ['guido', 'irv', 'jack'] //Py Out
		// [irv, jack, guido] //Java Out
		expect(out, "Display keys", "[irv, jack, guido] ", sprint(tel.keySet()));


		// print('guido' in tel)  //Py 
		print(tel.containsKey("guido"));
		// True //Py Out
		// true //Java Out
		expect(out, "See if key is contained", "true ", sprint(tel.containsKey("guido")));

//Ignore
	}

	//Ignore to {
	@SuppressWarnings("unchecked") //This is needed for Java 6 but not Java 7
	private static void mapAndListLiterals2(StringBuilder out) {
		
		//Ignore
		Map<String, Integer> tel;

		
		// tel = dict([('sape', 4139), ('guido', 4127), ('jack', 4098)])
		tel = mp(kv("sape", 4139), kv("guido", 4127), kv("jack", 4098));
		
		// print (tel)
		print(tel);
		// Output {'sape': 4139, 'jack': 4098, 'guido': 4127}
		//expect(out, "Using dict", "{sape=4139, jack=4098, guido=4127} ", sprint(tel));
		// {sape=4139, jack=4098, guido=4127} //Java Out

//Ignore
	}


	//Ignore
	private static void listComprehensionAndMaps(StringBuilder out) {
		
		/* doc
		 Another thing they showed towards the end of the Python dictionary tutorial was list comprehensions.
		 We don't support that yet. We might in the future via an expression language, but it is a ways off.
		 The closest we can get is to support basic functional programming.

		#### Example using list comprehension which we do not support.
		*/
		// print( dict([(x, x**2) for x in (2, 4, 6)]) ) # use a list comprehension //Py
		// Output {2: 4, 4: 16, 6: 36}
		/* doc
		 #### Example using Python map/fucntional programming, which we do support.
		 */
		//list = [2,4,6] //Py
		//def pow2 x: return x**2 //Py
		//print (dict( zip(list, map(pow2,list) ) ) //Py
		
		
		List<Integer> list;
		
		list = ls(2, 4, 6);
		class Pow2 { int f(int x) { return x* x;} }
		print ( mp(list, map(new Pow2(), list)) );
		
		expect(out, "power of 2 map problem", mp(2,4,4,16,6,36), mp(list, map(new Pow2(), list)));

		/* doc
		 The `ls` method is to allow you to simulate Python list literals since Java does no have literals for lists.
		 `ls` is a synomn for `list`. You can use either. Ease tends to have a short from and a long form of key methods.
		The `mp` is like Python dict function. We call it `mp` because Java does not have dictionaries, it has `java.util.Map`s.
		The `map` works just like Python's `map` function as described [here](http://docs.python.org/library/functions.html#map).
		The big difference is that Java does not have lamba or function object like Python so we just pass an instance of a class
		and the `map` function executes this method per iteration. 
		 */
	
	//Ignore
	}
	
	/* doc
	 Ease is a bit loose about what it allows as a function. You can use the `f` function to turn an object into a function
	 as long as that object implements a method called f (it can be static or not).
	 You can use the `fn` function to turn a method into a function. Let's seem some examples using the
	 `enumerate` method as follows:
	 */
	
	static void printItem(int i, String v) {
		print("static", i, v);
	}
	
	enum ops {printItem};
	
	static Class <Documentation> clz = Documentation.class;

	
	//Ignore
	private static void functionalProgramming(StringBuilder out) {

		/* doc
		 The `enumerate` method takes a function object as the first argument.
		 The `fn` creates a function object, the second argument to `fn` can be 
		 a String or any other object as toString is called on it.
		 This makes an enum a perfect second argument as you get code completion, and you only
		 have to spell the method name correctly once, the compiler and your IDE will take of the rest.
		 The `fn` will use the first method it finds (static or not), and it only looks at methods
		 declared by the current class (no super classes).
		 */
		print("Uses static method printItem"); //Ignore
		enumerate(fn(clz, "printItem"), list("tic", "tac", "toe"));
		print("You can use enums for method names instead of strings"); //Ignore
		enumerate(fn(clz, ops.printItem), list("tic", "tac", "toe"));
		print("You can skip the fn funciton too"); //Ignore
		enumerate(clz, ops.printItem, list("tic", "tac", "toe")); 
		

		/* doc
		 You could use an inner class as follows as the `fn` works with classes and instances of classes equally.
		 */
		class Foo { void printItem(int i, String v) { print("inner1", i, v); }}
		enumerate(fn(new Foo(), ops.printItem), list("tic", "tac", "toe"));

		/* doc
		 * You can also use the `f` instead of the `fn`, but in this case the function that get created
		 * is always based on the first method it finds (static or not) named f.
		 */
		class PrintItems { void f(int i, String v) { print("inner2", i, v); }}
		enumerate(f(new PrintItems()), list("tic", "tac", "toe"));
		
		/* doc
		 * You can also skip `f` and `fn` altogether as follows:
		 */
		enumerate(new Foo(), ops.printItem, list("tic", "tac", "toe"));
		enumerate(new PrintItems(), list("tic", "tac", "toe"));
		
		/* doc
		 * You can also skip this whole reflection business all together as follows:
		 */
		enumerate(new Enumerate<String>(){ public void visit(int index, String item) {
				print (index, item);
			}}, list("tic", "tac", "toe"));
		
		/* doc
		 * Let's break that last one down a bit.
		 * Define an enumerator
		 */
		Enumerate<String> printItems = new Enumerate<String> () {
			@Override
			public void visit(int index, String item) {
				print (index, item);
			}
			
		};
		/* doc
		 * Create a list.
		 */
		List<String> list = list("tic", "tac", "toe");
		/* doc
		 * Enumerate over the list.
		 */
		enumerate(printItems, list);
	
	//Ignore
	}

/*
The Employee class is a sample object. We will use it to demonstrate some concepts.
*/
	static Employee emp(String firstName, String lastName, double salary) {
		return new Employee(firstName, lastName, salary);
	}

	public static class Employee {
		private String firstName;
		private String lastName;
		private boolean current = false;
		private double salary;

		public boolean isCurrent() {
			return current;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setSalary(double salary) {
			this.salary = salary;
		}


		public Employee(String firstName, String lastName, double salary) {
			super();
			this.firstName = firstName;
			this.lastName = lastName;
			this.salary = salary;
		}

		//Ignore to }
		@Override
		public String toString() {
			return "Employee [firstName=" + firstName + ", lastName="
					+ lastName + ", salary=" + salary + "]";
		}

	}

}
