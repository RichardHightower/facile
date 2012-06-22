package org.facile;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	
	private static Map<String, Pattern> patterns = new HashMap<String, Pattern>();
	
	public static Matcher re(String regex, String input) {
		Pattern re = re(regex);
		Matcher matcher = re.matcher(input);
		return matcher;
	}

	public static Pattern re(String regex) {
		return regex(regex);
	}
	
	public static Pattern regex(String regex) {

		Pattern pattern = patterns.get(regex);
		if (pattern == null) {

			boolean transform = false;
			boolean forwardEscape = false;
			if (regex.startsWith("r'") && regex.endsWith("'")) {
				regex = regex.substring(2, regex.length() - 1);
				transform = true;
			} else if (regex.startsWith("/") && regex.endsWith("/")) {
				regex = regex.substring(1, regex.length() - 1);
				transform = true;
				forwardEscape = true;
			}

			if (forwardEscape) {
				regex = regex.replace("//", "{@#$%^}");
				regex = regex.replace("/", "\\");
				regex = regex.replace("{@#$%^}", "/");
				regex = regex.replace("{fw}", "/");
			}
			//http://docs.oracle.com/javase/1.5.0/docs/api/java/util/regex/Pattern.html
			if (transform) {
				regex = regex.replace("{char}", "\\w");
				regex = regex.replace("{word}", "\\w");
				regex = regex.replace("{a word}", "\\w+");
				regex = regex.replace("{digit}", "\\d");
				regex = regex.replace("{whitespace}", "\\s");
				regex = regex.replace("{sp}", "\\s+");
				regex = regex.replace("{no char}", "\\W");
				regex = regex.replace("{no digit}", "\\D");
				regex = regex.replace("{no whitespace}", "\\S");
				regex = regex.replace("{any}", ".*");
				regex = regex.replace("{dot}", "\\.");
				regex = regex.replace("{lower}", "\\p{javaLowerCase}");
				regex = regex.replace("{upper}", "\\p{javaUpperCase}");
				regex = regex.replace("{white}", "\\p{javaWhitespace}");
				regex = regex.replace("{mirror}", "\\p{javaMirrored}");
				regex = regex.replace("{blank}", "\\p{Blank}");
				regex = regex.replace("{alnum}", "\\p{Alnum}");
				regex = regex.replace("{alpha numeric}", "\\p{Alnum}");
				regex = regex.replace("{ascii}", "\\p{ASCII}");
				regex = regex.replace("{punct}", "\\p{Punct}");
				regex = regex.replace("{alnum}", "\\p{Alnum}");
				regex = regex.replace("{alpha}", "\\p{Alpha}");
				regex = regex.replace("{start line}", "^");
				regex = regex.replace("{end line}", "$");
				regex = regex.replace("{start input}", "\\A");
				regex = regex.replace("{start}", "\\A");
				regex = regex.replace("{end input}", "\\Z");
				regex = regex.replace("{end}", "\\z");
				regex = regex.replace("{optional once}", "?");
				regex = regex.replace("{optional many}", "*");
				regex = regex.replace("{zero or more}", "*");
				regex = regex.replace("{one or more}", "+");
				regex = regex.replace("{required once}", "+");
				regex = regex.replace("{only one}", "{1}");
				regex = regex.replace("{only two}", "{2}");
				regex = regex.replace("{relunctant}", "?");
				regex = regex.replace("{not greedy}", "?");
				regex = regex.replace("{possessive}", "+");
				regex = regex.replace("{match all}", "+");
				regex = regex.replace("{ OR }", "|");
				regex = regex.replace("{or}", "|");
				regex = regex.replace("{not}", "^");
				regex = regex.replace("{word boundary}", "\\b");
				regex = regex.replace("{no word boundary}", "\\B");
				regex = regex.replace("{visible}", "\\p{Graph}");
				regex = regex.replace("{print}", "\\p{Print}");
			}
		}

		pattern = Pattern.compile(regex);
		return pattern;
	}


}
