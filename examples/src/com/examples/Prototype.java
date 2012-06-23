//package com.examples;
//
//import static org.facile.Facile.*;
//
//import java.util.*;
//
//public class Prototype {
//
//	public static void main(String[] args) {
//
//		String st = fmt(
//				lns("This is a test of my sanity",
//					"This is a test of my sanity",
//					"{@if:test.emps[0].current==foo.bar.baz} ",
//					"optional stuff in if",
//					"{@endif}",
//					"This text is outside of the loop"
//						)
//				,
//				mp("test",
//						mp("emps",
//								ls(emp("Rick", "Hightower", 1000.0),
//										emp("Bob", "Hightower", 7000.0)))));
//		print("fmt19", st);
//
//		
////		String st = fmt(
////				lns("This is a test of my sanity",
////					"This is a test of my sanity",
////					"{@loop:test.emps} ",
////					"This is the body of the loop",
////					"firstName = {iter.firstName}",
////					"{@endloop}",
////					"This text is outside of the loop"
////						)
////				,
////				mp("test",
////						mp("emps",
////								ls(emp("Rick", "Hightower", 1000.0),
////										emp("Bob", "Hightower", 7000.0)))));
////		print("fmt18", st);
//		
//		
////		st = fmt(
////				"{emps}, fn={emps[0].firstName}, salary={emps[0].salary:2.2f}",
////				mp("emps",
////						ls(emp("Rick", "Hightower", 1000.0),
////								emp("Bob", "Hightower", 7000.0))));
////		print("fmt16", st);
////		
////		st = fmt("{2}, {1}, {0}", "a", "b", "c");
////		print("fmt4", st);
//
//
//	}
//	
//	@SuppressWarnings({ "unchecked", "unused" })
//	public static void oldMain(String[] args) {
//	
//		Map<String, Integer> tel;
//
//		// tel = {'jack': 4098, 'sape': 4139}
//		tel = mp("jack", 4098, "sape", 4139);
//
//		// tel['guido'] = 4127
//		tel.put("guido", 4127);
//
//		// print (tel)
//		print(tel);
//		// Output {'sape': 4139, 'guido': 4127, 'jack': 4098}
//
//		// print (tel['jack'])
//		print(tel.get("jack"));
//		// Output 4098
//
//		// del tel['sape']
//		tel.remove("sape");
//
//		// tel['irv'] = 4127
//		tel.put("irv", 4127);
//
//		// print(tel)
//		print(tel);
//		// Output {'guido': 4127, 'irv': 4127, 'jack': 4098}
//
//		// print(tel.keys())
//		print(tel.keySet());
//		// Output ['guido', 'irv', 'jack']
//
//		// 'guido' in tel
//		print(tel.containsKey("guido"));
//		// Output True
//
//		// tel = dict([('sape', 4139), ('guido', 4127), ('jack', 4098)])
//		tel = mp(kv("sape", 4139), kv("guido", 4127), kv("jack", 4098)); // get
//																			// unchecked
//																			// exception
//																			// for
//																			// Java
//																			// 6
//																			// but
//																			// not
//																			// Java
//																			// 7
//		// print (tel)
//		print(tel);
//		// Output {'sape': 4139, 'jack': 4098, 'guido': 4127}
//
//		// List Comprehension
//		Class<?> c = Prototype.class;
//
//		// print( dict([(x, x**2) for x in (2, 4, 6)]) ) # use a list
//		// comprehension
//		// Output {2: 4, 4: 16, 6: 36}
//		print(map(fn(c, "pow2"), list(2, 4, 6)));
//
//		// >>> for i, v in enumerate(['tic', 'tac', 'toe']):
//		// ... print i, v
//		// ...
//		// 0 tic
//		// 1 tac
//		// 2 toe
//		enumerate(fn(c, s.printItem), list("tic", "tac", "toe"));
//
//		// You can also declare functions on the fly like so
//		class Foo {
//			void printItem(int i, String v) {
//				print("foo", i, v);
//			}
//		}
//		enumerate(fn(new Foo(), s.printItem), list("tic", "tac", "toe"));
//
//		// The list function creates a list from an array or a varg array
//		// The list function has an alias ls
//		print("See ls", ls("tic", "tac", "toe"));
//
//		// >>> def f(x): return x % 2 != 0 and x % 3 != 0
//		// ...
//		// >>> filter(f, range(2, 25))
//		// [5, 7, 11, 13, 17, 19, 23]
//
//		class Filter {
//			boolean f(int x) {
//				return x % 2 != 0 && x % 3 != 0;
//			}
//		}
//		print(filter(f(new Filter()), range(2, 25)));
//
//		Collection<?> objitems = filter(f(new Filter()), range(2, 25));
//		Collection<Integer> myitems = gfilter(f(new Filter()), range(2, 25));
//
//		print(myitems, objitems);
//
//		// >>> def cube(x): return x*x*x
//		// ...
//		// >>> map(cube, range(1, 11))
//		// [1, 8, 27, 64, 125, 216, 343, 512, 729, 1000]
//
//		print(map(fn(c, s.cube), range(1, 11)));
//		List<?> list = map(fn(c, s.cube), range(1, 11));
//		List<String> gmap = gmap(fn(string, c, s.stringize), range(1, 11));
//		print(list, gmap);
//
//		print(reduce(fn(c, s.add), range(1, 11)));
//		Object object = reduce(fn(c, s.add), range(1, 11));
//		int greduce = greduce(fn(Integer.class, c, s.add), range(1, 11));
//		int greduce2 = greduce(fn(c, s.add), range(1, 11));
//		print(object, greduce, greduce2);
//
//		List<?> golfScores = map(fn(c, s.fmt2),
//				list("Rick", "Sue", "Bob", "Diana"), list(30, 40, 50, 100),
//				list("%s:%d", "%s=%d"));
//		List<String> scores = gmap(fn(string, c, s.fmt2),
//				list("Rick", "Sue", "Bob", "Diana"), list(30, 40, 50, 100),
//				list("%s:%d", "%s=%d"));
//
//		print(golfScores, scores);
//
//		// print """
//		// Usage: thingy [OPTIONS]
//		// -h Display this usage message
//		// -H hostname Hostname to connect to
//		// """
//		print(lines("				Usage: thingy [OPTIONS]",
//				"			     -h                        Display this usage message",
//				"			     -H hostname               Hostname to connect to"));
//
//		// >>> word = 'Help' + 'A'
//		// >>> word
//		// 'HelpA'
//		String word;
//		word = "Help" + "A";
//		print(word);
//		// >>> word[4]
//		// 'A'
//		// >>> word[0:2]
//		// 'He'
//		// >>> word[2:4]
//		// 'lp'
//		print(slice(word, 4));
//		print(slice(word, 0, 2));
//		print(slice(word, 2, 4));
//		// >>> word[:2] # The first two characters
//		// 'He'
//		// >>> word[2:] # Everything except the first two characters
//		// 'lpA'
//		print(slice(word, 0, 2));
//		print(slice(word, 2));
//
//		// >>> 'x' + word[1:]
//		// 'xelpA'
//		// >>> 'Splat' + word[4]
//		// 'SplatA'
//		print(lines("x" + slice(word, 1), "Splat" + word.charAt(4)));
//
//		// >>> word[:2] + word[2:]
//		// 'HelpA'
//		// >>> word[:3] + word[3:]
//		// 'HelpA'
//		print(lines(slice(word, 0, 2) + slice(word, 2), slice(word, 0, 3)
//				+ slice(word, 3)));
//		// >>> word[1:100]
//		// 'elpA'
//		// >>> word[10:]
//		// ''
//		// >>> word[2:1]
//		// ''
//		print(lines("1,00 " + slice(word, 1, 100), "10 " + slice(word, 10),
//				"2,1 " + slice(word, 2, 1)));
//		// >>> word[-1] # The last character
//		// 'A'
//		// >>> word[-2] # The last-but-one character
//		// 'p'
//		// >>> word[-2:] # The last two characters
//		// 'pA'
//		// >>> word[:-2] # Everything except the last two characters
//		// 'Hel'
//		// slice has a alias called slc, slc=slice, index=idx
//		print(lines("-1   :" + slc(word, -1), "-2   :" + idx(word, -2),
//				"-2   :" + slc(word, -2), "0,-2 :" + slc(word, 0, -2)));
//		// >>> word[-100:]
//		// 'HelpA'
//		print(slice(word, -100));
//
//		List<? extends Object> a;
//		// a = ['spam', 'eggs', 100, 1234]
//		// print (a)
//		// ['spam', 'eggs', 100, 1234]
//		a = ls("spam", "eggs", 100, 1234);
//		print(a);
//		// ls and list do the same thing
//		a = list("spam", "eggs", 100, 1234);
//		print(a);
//
//		// >>> a[0]
//		// 'spam'
//		print(idx(a, 0));
//		// >>> a[3]
//		// 1234
//		print(idx(a, 3));
//		// >>> a[-2]
//		// 100
//		print(idx(a, 3));
//		// >>> a[1:-1]
//		// ['eggs', 100]
//		print(slc(a, 1, -1));
//		// >>> a[:2]
//		print(slc(a, 0, 2));
//		// >>> a[:2] + ['bacon', 2*2]
//		// ['spam', 'eggs', 'bacon', 4]
//		print("addls", ls(slc(a, 0, 2), ls("bacon", 2 * 2)));
//
//		// >>> 3*a[:3] + ['Boo!']
//		// ['spam', 'eggs', 100, 'spam', 'eggs', 100, 'spam', 'eggs', 100,
//		// 'Boo!']
//
//		print(ls(mul(3, slc(a, 0, 3)), ls("Boo!")));
//		// ls(mul(3,slc(a,0,3)),ls("Boo!"))
//		// 3*a[:3]+['Boo!']
//		// 3*a[:3]+['Boo!']3*a[:3]+['Boo!'] //java is twice as long
//
//		// a[:]
//		// ['spam', 'eggs', 100, 1234]
//		print(copy(a));
//
//		// >>> a
//		// ['spam', 'eggs', 100, 1234]
//		// >>> a[2] = a[2] + 23
//		// >>> a
//		// ['spam', 'eggs', 123, 1234]
//
//		// Java is strongly typed so the above is a bit harder, better with
//		// uniform types.
//		List<Integer> b = ls(1, 2, 100, 1234);
//		b.set(2, b.get(2) + 23);
//		print(b);
//
//		// Java is strongly typed so the above is a bit harder
//		((List<Integer>) a).set(2, ((Integer) a.get(2) + 23));
//		print(a);
//
//		List<Integer> ia = (List<Integer>) a;
//		List<String> sa = (List<String>) a;
//		List<Object> oa = (List<Object>) a;
//
//		// b.set(2,b.get(2)+23);
//		// a[2]=a[2]+23 //for uniform type, python is 1/2 size
//		// ((List<Integer>)a).set(2,((Integer)a.get(2)+23)); //for non-uniform
//		// type, python is about 1/5 the size
//
//		// >>> # Replace some items:
//		// ... a[0:2] = [1, 12]
//		// >>> a
//		// [1, 12, 123, 1234]
//		List<Integer> ia2 = copy(ia);
//
//		replace(ia2, 0, 2, ls(1, 12));
//		print("ia2", ia2);
//
//		rpl(ia, 0, 2, ls(1, 12));
//		print("ia", ia);
//
//		// >>> # Remove some:
//		// ... a[0:2] = []
//		// >>> a
//		// [123, 1234]
//		rpl(ia, 0, 2, ls(new Integer[] {}));
//		print("after remove", ia);
//		// >>> # Insert some:
//		// ... a[1:1] = ['bletch', 'xyzzy']
//		// >>> a
//		// [123, 'bletch', 'xyzzy', 1234]
//		List<String> sa2 = copy(sa);
//		sa2.addAll(1, ls("bletch", "xyzzy"));
//		print(sa2);
//
//		rpl(sa, 1, 1, ls("bletch", "xyzzy"));
//		print(sa);
//
//		List<String> mystring;
//		// >>> mystrings = ["one", "two", "three", "four", "five", "six"]
//		// >>> mystrings[2:3] = ["3", "3 1/2", "3 3/4", "4"]
//		// >>> mystrings
//		// ['one', 'two', '3', '3 1/2', '3 3/4', '4', 'four', 'five', 'six']
//		mystring = ls("one", "two", "three", "four", "five", "six");
//		rpl(mystring, 2, 3, ls("3", "3 1/2", "3 3/4", "4"));
//		print(mystring);
//
//		// >>> # Insert (a copy of) itself at the beginning
//		// >>> a[:0] = a
//		// >>> a
//		// [123, 'bletch', 'xyzzy', 1234, 123, 'bletch', 'xyzzy', 1234]
//		b = ls(1, 2, 3, 4, 5, 6, 7, 8, 9);
//		rpl(b, 0, 0, copy(b));
//		print(b);
//
//		rpl(oa, 0, 0, copy(oa));
//		print(oa);
//
//		// >>> # Clear the list: replace all items with an empty list
//		// >>> a[:] = []
//		// >>> a
//		rpl(oa, 0, oa.size(), ls((Object)null));
//		print("empty", oa);
//
//		List<Character> a2;
//		// >>> a2 = ['a', 'b', 'c', 'd']
//		// >>> len(a2)
//		// 4
//		a2 = ls('a', 'b', 'c', 'd');
//		print(a2.size());
//
//		List<Integer> q;
//		// >>> q = [2, 3]
//		q = ls(2, 3);
//		// >>> p = [1, q, 4]
//		// >>> len(p)
//		// 3
//		List<?> p = ls(2, q, 3);
//		print(p.size());
//		// >>> p[1]
//		// [2, 3]
//		print(p.get(1));
//
//		List<List<Integer>> pi = (List<List<Integer>>) p; // strongly typed
//		// >>> p[1][0]
//		// 2
//		print(pi.get(1).get(0));
//
//		// can't really do this
//		// >>> p[1].append('xtra') # See section 5.1
//		// >>> p
//		pi.get(1).add(666);
//		// [1, [2, 3, 'xtra'], 4]
//		// >>> q
//		// [2, 3, 'xtra']
//		print(q);
//
//		// >>> '{0}, {1}, {2}'.format('a', 'b', 'c')
//		// 'a, b, c'
//		String str = format("{0}, {1}, {2}", "a", "b", "c");
//		print("fmt1", str);
//		//fmt is the short form of format
//		str = fmt("{0}, {1}, {2}", "a", "b", "c");
//		print("fmt2", str);
//		// >>> '{}, {}, {}'.format('a', 'b', 'c') # 2.7+ only
//		// 'a, b, c'
//		str = fmt("{}, {}, {}", "a", "b", "c");
//		print("fmt3", str);
//
//		// >>> '{2}, {1}, {0}'.format('a', 'b', 'c')
//		// 'c, b, a'
//		str = fmt("{2}, {1}, {0}", "a", "b", "c");
//		print("fmt4", str);
//
//		// >>> '{2}, {1}, {0}'.format(*'abc') # unpacking argument sequence
//		// 'c, b, a'
//		str = fmt("{2}, {1}, {0}", array("abc"));
//		print("fmt5", str);
//
//		str = fmt("{2}, {1}, {0}", (Object[]) array(1, 2, 3));
//		print("fmt6", str);
//
//		str = fmt("{2}, {1}, {0}", oarray(1, 2, 3));
//		print("fmt7", str);
//
//		str = fmt("{2}, {1}, {0}", oar(1, 2, 3));
//		print("fmt8", str);
//
//		// >>> '{0}{1}{0}'.format('abra', 'cad') # arguments' indices can be
//		// repeated
//		// 'abracadabra'
//		str = fmt("{0}{1}{0}", "abra", "cad");
//		print("fmt9", str);
//		// >>> 'Coordinates: {latitude}, {longitude}'.format(latitude='37.24N',
//		// longitude='-115.81W')
//		// 'Coordinates: 37.24N, -115.81W'
//		str = fmt("Coordinates: {latitude}, {longitude}",
//				mp("latitude", "37.24N", "longitude", "-115.81W"));
//		print("fmt10", str);
//		// >>> "{1:0>+10.3f},{1}".format(2.2,3.0)
//		// '0000+3.000,3.0'
//		str = fmt("{1:2.2f},{1}", 2.2, 3.0);
//		print("fmt11", str);
//
//		str = fmt("Coordinates: {latitude:2.2f}, {longitude}",
//				mp("latitude", 37.24, "longitude", -115.81));
//		print("fmt12", str);
//
//		str = fmt("{1[0]:2.2f},{1}", 2.2, ls(2.2, 3.0));
//		print("fmt13", str);
//
//		str = fmt("0={0},1={1},1.my2={1.my2:2.2f}", 2.2,
//				mp("my", 2.2, "my2", 3.0));
//		print("fmt14", str);
//
//		str = fmt("{0}, fn={0.firstName}, salary={0.salary:2.2f}",
//				emp("Rick", "Hightower", 1000.0));
//		print("fmt15", str);
//
//		str = fmt(
//				"{0}, fn={emps[0].firstName}, salary={emps[0].salary:2.2f}",
//				mp("emps",
//						ls(emp("Rick", "Hightower", 1000.0),
//								emp("Bob", "Hightower", 7000.0))));
//		print("fmt16", str);
//
//		print();
//		print();
//		str = fmt(
//				"a={test}\n" + "b={test.emps}\n" + "c={test.emps[0]}\n"
//						+ "d={test.emps[0].firstName}\n"
//						+ "e={test.emps[0].salary:2.2f}",
//				mp("test",
//						mp("emps",
//								ls(emp("Rick", "Hightower", 1000.0),
//										emp("Bob", "Hightower", 7000.0)))));
//		print("fmt17", str);
//
//	}
//
//	static Employee emp(String firstName, String lastName, double salary) {
//		return new Employee(firstName, lastName, salary);
//	}
//
//	public static class Employee {
//		String firstName;
//		String lastName;
//		boolean current = false;
//
//		public boolean isCurrent() {
//			return current;
//		}
//
//		public String getFirstName() {
//			return firstName;
//		}
//
//		public String getLastName() {
//			return lastName;
//		}
//
//		public void setSalary(double salary) {
//			this.salary = salary;
//		}
//
//		double salary;
//
//		public Employee(String firstName, String lastName, double salary) {
//			super();
//			this.firstName = firstName;
//			this.lastName = lastName;
//			this.salary = salary;
//		}
//
//		@Override
//		public String toString() {
//			return "Employee [firstName=" + firstName + ", lastName="
//					+ lastName + ", salary=" + salary + "]";
//		}
//
//	}
//
//	static String fmt2(String name, Integer value, String fmt) {
//		print("fmt", name, value, fmt);
//		if (fmt != null) {
//			return String.format(fmt, name, value);
//		} else {
//			return name + " = " + value;
//		}
//	}
//
//	static int add(int x, int y) {
//		return x + y;
//	}
//
//	static int cube(int i) {
//		return i * i * i;
//	}
//
//	static int pow2(int i) {
//		return i * i;
//	}
//
//	static String stringize(int i) {
//		switch (i) {
//		case 1:
//			return "one";
//		case 2:
//			return "two";
//		case 3:
//			return "three";
//		default:
//			return "" + i;
//		}
//	}
//
//	static void printItem(int i, String v) {
//		print(i, v);
//	}
//
//	static enum s {
//		printItem, cube, add, stringize, fmt2
//	};
//
//}
