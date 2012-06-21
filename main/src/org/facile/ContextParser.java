package org.facile;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.facile.Facile;
import org.facile.IO.FileObject;
import org.facile.IO.FileTextReader;
import static org.facile.Facile.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContextParser {
	// TODO # What if you create a file context like Perl's <IN>.
	// This context could work with regex too like $1 (group 1 in split), $_
	// (line), @_ (array), next (regex), and unless (regex)

	@SuppressWarnings("serial")
	public static class my extends HashMap<String, String> {
		@SuppressWarnings("rawtypes")
		private Class enm;
		private Enum<?> e;

		public my(Class<Enum<?>> enm, Enum<?> e) {
			this.e =e;
			this.enm = enm;
		}

		@SuppressWarnings({ "static-access", "unchecked" })
		public String put(String key, String value) {
			try {
				e.valueOf(enm, key);
				return super.put(key, value);
			} catch (Exception ex) {
				throw new IllegalArgumentException("Not a valid key");
			}			
		}
		public void i(String key, String value) {
				this.put(key, value);
		}
		
		public void i(@SuppressWarnings("rawtypes") Enum e, String value) {
			this.put(e.name(), value);
		}

	}
	@SuppressWarnings("unchecked")
	public static my my(@SuppressWarnings("rawtypes") Class enm, Enum<?> e) {
		return new my(enm, e);
	}
	

	private static final Logger log = Logger.getLogger(ContextParser.class
			.getName());

	public static enum Input {
		FILE, INPUT, IN, OUT;
	}

	private static ThreadLocal<Ctx> ctx;

	public static Ctx ctx() {
		if (ctx==null) {
			ctx = new ThreadLocal<Ctx>();
		}
		Ctx c = ctx.get();
		if (c == null) {
			c = new Ctx();
			ctx.set(c);
		} else {
			Facile.warning(log, "Context was already present and should not be");
		}
		return c;
	}

	public static Ctx currentCtx() {
		if (ctx==null) {
			Facile.error(log, "Context holder is missing");
		}
		Ctx c = ctx.get();
		if (c == null) {
			Facile.error(log, "Context is missing from holder");
		} 
		return c;
	}

	public static void cleanCtx() {
		Ctx c = null;
		c = ctx.get();
		if (c == null) {
			Facile.warning(log, "Tried to clear a context that does not exist");
			return;
		}

		if (c.FILE != null) {
			c.FILE.close();
		}
		if (c.IN != null) {
			c.IN.close();
		}
		if (c.OUT != null) {
			c.OUT.close();
		}
		if (c.INPUT != null) {
			c.INPUT.close();
		}

	}
	
	public static boolean IN() {
		return currentCtx().IN();
	}
	public static void open(Input t, String file) {
		currentCtx().open(t, file);
	}
	
	public static boolean ok(String regex) {
		return currentCtx().ok(regex);
	}
	public static String $1() {
		return currentCtx().$1;
	}
	public static String $2() {
		return currentCtx().$2;
	}
	public static String $3() {
		return currentCtx().$3;
	}
	public static String $4() {
		return currentCtx().$4;
	}
	public static String $5() {
		return currentCtx().$5;
	}
	public static String $6() {
		return currentCtx().$6;
	}
	public static String $7() {
		return currentCtx().$7;
	}
	public static String $8() {
		return currentCtx().$8;
	}
	public static String $9() {
		return currentCtx().$9;
	}
	public static String $10() {
		return currentCtx().$10;
	}
	public static String $11() {
		return currentCtx().$11;
	}
	public static String $12() {
		return currentCtx().$12;
	}
	public static String $13() {
		return currentCtx().$13;
	}
	public static String $15() {
		return currentCtx().$15;
	}
	public static String $16() {
		return currentCtx().$16;
	}
	public static String $17() {
		return currentCtx().$17;
	}
	public static String $18() {
		return currentCtx().$18;
	}

	public static String $_() {
		return currentCtx().$_;
	}
	
	public String _$(int index){
		return currentCtx().$$.get(index);
	}
	public List<String> $$(){
		return currentCtx().$$;
	}


	public static class Ctx {
		public String $_;
		public String _;
		public String $0;
		public String $1;
		public String $2;
		public String $3;
		public String $4;
		public String $5;
		public String $6;
		public String $7;
		public String $8;
		public String $9;
		public String $10;
		public String $11;
		public String $12;
		public String $13;
		public String $14;
		public String $15;
		public String $16;
		public String $17;
		public String $18;
		public String $19;
		List<String> $$;
		
		

		public FileObject<String> FILE;
		public FileObject<String> INPUT;
		public FileObject<String> IN;
		public FileObject<String> OUT;
		private Map <String, FileObject<String>> f = new HashMap<String, FileObject<String>>(10);
		public Matcher matcher;

		public FileObject<String> f(Object obj) {
			return f.get(obj.toString());
			
		}
		public void open(Input t, String file) {
			FileObject<String> fileObject = Facile.openFile(file);
			if (fileObject instanceof FileTextReader) {
				((FileTextReader)fileObject).ctx(this);
				((FileTextReader)fileObject).startIteration();
			}
			switch (t) {
			case FILE:
				FILE = fileObject;
				break;
			case INPUT:
				INPUT = fileObject;
				break;
			case IN:
				IN = fileObject;
				break;
			case OUT:
				OUT = fileObject;
				break;
			}
		}

		public void open(Object obj, String file) {
			FileObject<String> fileObject = Facile.openFile(file);
			f.put(obj.toString(), fileObject);
		}
		
		public boolean IN(){
			return !IN.eof();
		}
		
		public String $(int index){
			return $$.get(index);
		}
		public List<String> $$(){
			return copy($$);
		}
		
		public boolean ok(String regex) {
			initGroups();
			regex(regex);
			
			if (matcher.find()) {
				
				extractGroups();

				return true;
			} else {
				return false;
			}	
		}
		private void regex(String regex) {
			Pattern pattern = Pattern.compile(regex);
			matcher = pattern.matcher($_);
		}
		
		public boolean match(String regex) {
			initGroups();
			regex(regex);
			
			if (matcher.matches()) {
				extractGroups();

				return true;
			} else {
				return false;
			}	
		}

		
		private void extractGroups() {
			int count = matcher.groupCount();
			$0 = matcher.group(0);

			if (count>=1) {
				$1 = matcher.group(1);
			}
			if (count>=2) {
				$2 = matcher.group(2);
			}
			if (count>=3) {
				$3 = matcher.group(3);
			}
			if (count>=4) {
				$4 = matcher.group(4);
			}
			if (count>=5) {
				$5 = matcher.group(5);
			}
			if (count>=6) {
				$6 = matcher.group(6);
			}
			if (count>=7) {
				$7 = matcher.group(7);
			}
			if (count>=8) {
				$8 = matcher.group(8);
			}
			if (count>=9) {
				$9 = matcher.group(9);
			}
			if (count>=10) {
				$10 = matcher.group(10);
			}
			if (count>=11) {
				$11 = matcher.group(11);
			}
			if (count>=12) {
				$11 = matcher.group(11);
			}
			if (count>=13) {
				$11 = matcher.group(11);
			}
			if (count>=14) {
				$11 = matcher.group(11);
			}
			if (count>=15) {
				$11 = matcher.group(11);
			}
			if (count>=16) {
				$16 = matcher.group(16);
			}
			if (count>=17) {
				$17 = matcher.group(17);
			}
			if (count>=18) {
				$18 = matcher.group(18);
			}
			if (count>=19) {
				$19 = matcher.group(19);
			}
			
			$$ = list ($0, $1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17, $18, $19);
			
			if (count > 19) {
				for (int index = 20; index < count -1; count++) {
					$$.add(matcher.group(index));
				}
			}
		}
		private void initGroups() {
			$0 = null;
			$1 = null;
			$3 = null;
			$4 = null;
			$5 = null;
			$6 = null;
			$7 = null;
			$8 = null;
			$9 = null;
			$10 = null;
			$11 = null;
			$12 = null;
			$13 = null;
			$14 = null;
			$15 = null;
			$16 = null;
			$17 = null;
			$18 = null;
			$19 = null;
		}
		

	}

}
