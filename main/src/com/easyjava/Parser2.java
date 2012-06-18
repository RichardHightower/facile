package com.easyjava;

import static com.easyjava.EasyJava.*;

import java.util.Arrays;
import java.util.List;
import org.hamcrest.core.IsSame;

public class Parser2 {

	enum Cardinality {
		ONCE, ONE_OR_MORE, ZERO_OR_MORE, SOME
	}

	enum RuleType {
		CHAR_RANGE, SEQUENCE, ONE_OF, EXACT_CHARS
	}

	public static final int NOT_FOUND = -1;
	public static final Cardinality once = Cardinality.ONCE;
	public static final Cardinality some = Cardinality.ONCE;
	public static final Cardinality oneOrMore = Cardinality.ONE_OR_MORE;
	public static final Cardinality zeroOrMore = Cardinality.ZERO_OR_MORE;

	public static class Range {
		char start;
		char stop;
	}

	public static class Rule implements Cloneable {
		private int min = 1;
		private int max = Integer.MAX_VALUE;
		private Cardinality cardinality;
		private char[] allowed;
		private boolean optional;
		private Rule[] rules;
		private String name;
		private RuleType type = RuleType.CHAR_RANGE;

		Rule times(int times) {
			if (times == 1) {
				return once();
			} else if (times > 1) {
				return times(times, times, some);
			}
			throw new IllegalStateException("times must be greater than one");
		}

		Rule times(int min, int max) {
			if (min > max) {
				throw new IllegalStateException("min is greater than max");
			}
			if (min == 0 && max == Integer.MAX_VALUE) {
				return zeroOrMore();
			} else if (min == 1 && max == 1) {
				return once();
			} else if (min == 1 && max == Integer.MAX_VALUE) {
				return oneOrMore();
			} else {
				return times(min, max, some);
			}

		}

		public Rule oneOrMore() {
			return times(1, Integer.MAX_VALUE, oneOrMore);
		}

		public Rule once() {
			return times(1, 1, once);
		}

		public Rule zeroOrMore() {
			return times(0, Integer.MAX_VALUE, zeroOrMore);
		}

		public Rule allow(char... chars) {
			checkArgumentsForNulls(chars);
			Rule clone = cloneMe();
			clone.allowed = chars;
			return clone;
		}

		public Rule optional() {
			Rule clone = cloneMe();
			clone.optional = true;
			return clone;
		}

		public Rule exact(char[] chars) {
			checkArgumentsForNulls(chars);
			Rule clone = cloneMe();
			clone.type = RuleType.EXACT_CHARS;
			clone.allowed = chars;
			return clone;
		}

		public Rule rules(Rule... rules) {
			checkArgumentsForNulls(rules);
			Rule clone = cloneMe();
			clone.type = RuleType.SEQUENCE;
			clone.rules = rules;
			return clone;
		}

		public Rule oneOf(Rule... rules) {
			checkArgumentsForNulls(rules);
			Rule clone = cloneMe();
			clone.type = RuleType.ONE_OF;
			clone.rules = rules;
			return clone;
		}

		private Rule cloneMe() {
			try {
				Rule clone = (Rule) this.clone();
				return clone;
			} catch (CloneNotSupportedException e) {
				throw new IllegalStateException(
						"Unable to clone, this will never happen");
			}
		}

		private Rule times(int min, int max, Cardinality cardinality) {
			checkArgumentsForNulls(cardinality);

			Rule clone = (Rule) cloneMe();

			clone.min = min;
			clone.max = max;
			clone.cardinality = cardinality;
			return clone;
		}

		@Override
		public String toString() {
			return "Rule [min=" + min + ", max=" + max + ", cardinality="
					+ cardinality + ", allowed=" + Arrays.toString(allowed)
					+ ", rules=" + Arrays.toString(rules) + ", name=" + name
					+ ", type=" + type + "]";
		}

		private Rule name(String name) {
			checkArgumentsForNulls(name);

			this.name = name;
			return this;
		}

		public Rule required() {
			Rule clone = cloneMe();
			clone.optional = false;
			return clone;
		}

	}

	public final static Rule RULE = new Rule();
	public final static Rule OPTIONAL = new Rule();
	public final static Rule ONCE = RULE.once();
	public final static Rule ONE_OR_MORE = RULE.oneOrMore();
	public final static Rule ZERO_OR_MORE = RULE.zeroOrMore();
	public final static Rule REQUIRED = new Rule();
	public final static Rule ALPHA = new Rule();
	public final static Rule ALPHA_NUMERIC = new Rule();
	public final static Rule DIGIT = new Rule();
	public final static Rule WHITE_SPACE = new Rule();
	public final static Rule VCHAR = new Rule();
	public final static Rule SP = new Rule();
	public final static Rule CRLF = new Rule();

	static {
		OPTIONAL.optional = true;
		REQUIRED.optional = false;

		ALPHA.allowed = add(range('a', 'z'), range('A', 'Z'));
		ALPHA.name = "alpha";

		DIGIT.allowed = range('0', '9');
		DIGIT.name = "digit";

		ALPHA_NUMERIC.allowed = add(ALPHA.allowed, DIGIT.allowed);

		WHITE_SPACE.allowed = chars('\t', '\n', '\r', ' ');
		WHITE_SPACE.name = "white_space";

		VCHAR.allowed = range('\u0021', '\u007E');
		VCHAR.name = "visible-characters";

		SP.allowed = chars(' ');
		SP.name = "space";

		CRLF.allowed = chars('\r', '\n');

	}

	public static Rule optional(Rule... rules) {
		checkArgumentsForNulls(rules);
		return OPTIONAL.rules(rules);
	}

	public static Rule optional(String name, Rule... rules) {
		checkArgumentsForNulls(name);
		checkArgumentsForNulls(rules);

		return OPTIONAL.rules(rules).name(name);
	}

	public static Rule oneOf(Rule... rules) {
		checkArgumentsForNulls(rules);

		return ONCE.oneOf(rules);
	}

	public static Rule oneOf(String name, Rule... rules) {
		checkArgumentsForNulls(name);
		checkArgumentsForNulls(rules);

		return ONCE.oneOf(rules).name(name);
	}

	public static Rule exact(String chars) {
		checkArgumentsForNulls(chars);

		return RULE.exact(chars.toCharArray()).name("exact-" + chars);
	}

	public static Rule once(Rule... rules) {
		checkArgumentsForNulls(rules);

		return ONCE.rules(rules);
	}

	public static Rule zeroOrMore(Rule... rules) {
		checkArgumentsForNulls(rules);

		return ZERO_OR_MORE.rules(rules);
	}

	public static Rule once(String name, Rule... rules) {
		checkArgumentsForNulls(name);
		checkArgumentsForNulls(rules);

		return ONCE.rules(rules).name(name);
	}

	public static Rule zeroOrMore(String name, Rule... rules) {
		checkArgumentsForNulls(name);
		checkArgumentsForNulls(rules);

		return ZERO_OR_MORE.rules(rules);
	}

	public static Rule required(Rule... rules) {
		checkArgumentsForNulls(rules);

		return REQUIRED.rules(rules);
	}

	public static Rule rules(Rule... rules) {
		checkArgumentsForNulls(rules);

		return REQUIRED.rules(rules);
	}

	public static Rule required(String name, Rule... rules) {
		checkArgumentsForNulls(name);
		checkArgumentsForNulls(rules);

		return REQUIRED.rules(rules).name(name);
	}

	public static Rule rules(String name, Rule... rules) {
		checkArgumentsForNulls(name);
		checkArgumentsForNulls(rules);

		return REQUIRED.rules(rules).name(name);
	}

	public static Rule allow(char... chars) {
		checkArgumentsForNulls(chars, "");

		return REQUIRED.allow(chars);
	}

	public static Rule allowOnce(char... chars) {
		checkArgumentsForNulls(chars);

		return REQUIRED.allow(chars).once();
	}

	public static Rule digits(int times) {
		return DIGIT.times(times);
	}

	public static Rule digits(String name, int times) {
		checkArgumentsForNulls(name);

		return DIGIT.times(times).name(name);
	}

	public static Rule alpha(int times) {
		return ALPHA.times(times);
	}

	public static Rule alpha(String name, int times) {
		checkArgumentsForNulls(name);

		return ALPHA.times(times).name(name);
	}

	public static Rule alpha(int min, int max) {
		return ALPHA.times(min, max);
	}

	public static Rule alpha(String name, int min, int max) {
		return ALPHA.times(min, max).name(name);
	}

	public static Rule alpha(Cardinality card) {
		checkArgumentsForNulls(card);

		return alpha(ALPHA.name, card);
	}

	public static Rule alpha(String name, Cardinality card) {
		checkArgumentsForNulls(new Object[] { name, card });

		switch (card) {
		case ZERO_OR_MORE:
			return ALPHA.zeroOrMore().name(name);
		case ONE_OR_MORE:
			return ALPHA.oneOrMore().name(name);
		case ONCE:
			return ALPHA.once().name(name);
		case SOME:
			return ALPHA.times(3).name(name);
		}
		return null;
	}

	public static Rule digits(int min, int max) {
		return DIGIT.times(min, max);
	}

	public static boolean match(String string, Rule rule) {
		checkArgumentsForNulls(string, rule);

		return match(chars(string), rule);
	}

	public static boolean match(char[] chars, Rule rule) {
		checkArgumentsForNulls(chars, rule);

		return match(chars, rule, 0, chars.length);
	}

	public static boolean match(char[] chars, Rule rule, int start) {
		checkArgumentsForNulls(chars, rule);

		return match(chars, rule, start, chars.length);

	}

	public static boolean match(char[] chars, Rule rule, int start, int end) {
		checkArgumentsForNulls(chars, rule);

		int index = doMatch(chars, rule, start, end);

		if (rule.optional) {
			return true;
		} else {
			return index == end;
		}
	}

	private static int doMatch(char[] chars, Rule rule, int start, int end) {

		if (rule.type == RuleType.SEQUENCE) {
			return matchSequence(chars, rule, start, end);
		} else if (rule.type == RuleType.CHAR_RANGE) {
			if (rule.optional) {
				return matchOptionalCharRangeStartEnd(chars, rule, start, end);

			} else {
				return matchCharRangeStartEnd(chars, rule, start, end);

			}
		} else if (rule.type == RuleType.EXACT_CHARS) {
			return matchCharRangeExact(chars, rule, start, end);
		} else if (rule.type == RuleType.ONE_OF) {
			return matchOneOf(chars, rule, start, end);
		} else {
			throw new IllegalStateException("Rule type is not value "
					+ rule.type);
		}
	}

	public static int matchSequence(char[] chars, Rule rule, int start, int end) {
		int index = start;
		for (Rule r : rule.rules) {
			if (r == null) {
				// print("null rule", rule.name, rule.rules.length);
				continue;
			}
			index = doMatch(chars, r, index, end);
			if (index == NOT_FOUND) {
				break;
			}
		}
		return index;
	}

	public static int matchOneOf(char[] chars, Rule rule, int start, int end) {
		// print("matchOneOf", start, end, rule.name);
		int index = start;
		for (Rule r : rule.rules) {
			index = doMatch(chars, r, start, end);
			if (index != NOT_FOUND) {
				break;
			}
		}
		return index;
	}

	public static int matchOptionalCharRangeStartEnd(char[] chars, Rule rule,
			int start, int end) {

		int index = matchCharRangeStartEnd(chars, rule, start, end);

		if (index == NOT_FOUND) {
			// print("not found was returned");
			return start;
		} else {
			return index;
		}

	}

	public static int matchCharRangeStartEnd(char[] chars, Rule rule,
			int start, int end) {

		char[] allowed = rule.allowed;
		int max = rule.max;
		int min = rule.min;

		int index = start;
		int count = 0;
		for (index = start; index < end && count != max; index++) {
			if (!isIn(chars[index], allowed)) {
				return NOT_FOUND;
			} else {
				count++;
			}
		}
		if (count >= min && count <= max) {
			return index;
		} else {
			return NOT_FOUND;
		}
	}

	public static int matchCharRangeExact(char[] chars, Rule rule, int start,
			int end) {
		char[] allowed = rule.allowed;

		int lengthLeft = end - start;
		if (allowed.length > lengthLeft) {
			return NOT_FOUND;
		}
		int count = 0;

		for (; count < allowed.length; count++) {
			char c1 = chars[start + count];
			char c2 = allowed[count];
			if (c1 != c2) {
				return NOT_FOUND;
			}
		}
		return count + start;

	}

	public static class Match {
		Matcher match;

		private Match(Matcher match) {
			this.match = match;
		}

		public List<Integer> getErrors() {
			return noZeroList(match.errorsPositions);
		}

		public List<Rule> getRulesForErrors() {
			return lsRange(0, match.errors, match.errorsRulePositions);
		}

		public List<String> getReasonsForErrors() {
			return lsRange(0, match.errors, match.errorReasons);
		}

		public List<Rule> getRulesForMatches() {
			return lsRange(0, match.matches, match.matchedRules);
		}

		public boolean isMatch() {
			return match.didMatch;
		}

		public int getErrorCount() {
			return match.errors;
		}

		public int getLastMatch() {
			return match.lastMatch;
		}

		public String getReason() {
			return idx(getReasonsForErrors(), 0);
		}

		public int index() {
			return match.index;
		}

		public String toString() {
			return lines(
					fmt("Error Count {0}, Last Match {1}, Was Match {2}, {3}",
							match.errors, match.lastMatch, match.didMatch,
							getReason()), str(getReasonsForErrors()));
		}

	}

	public static Match matchex(String string, Rule rule) {
		return (new Matcher(chars(string), rule)).match();
	}

	public static Match matchex(char[] chars, Rule rule) {
		return new Matcher(chars, rule, 0).match();
	}

	public static Match matchex(char[] chars, Rule rule, int start) {
		return new Matcher(chars, rule, start, chars.length).match();

	}

	public static Match matchex(char[] chars, Rule rule, int start, int end) {
		return new Matcher(chars, rule, start, end).match();
	}

	
	private static class Matcher {
		private boolean didMatch;
		private int errors;
		private int[] errorsPositions = new int[20];
		private int matches;
		private Rule[] errorsRulePositions = new Rule[20];
		private String[] errorReasons = new String[20];

		private int[] matchPositions = new int[2000];
		private Rule[] matchedRules = new Rule[2000];
		char[] chars;
		Rule mainRule;
		Rule parentRule;
		int start;
		int end;
		int lastMatch;
		int index;
		boolean found;

		private Matcher(String string, Rule rule) {
			this(chars(string), rule);
		}

		private Matcher(char[] chars, Rule rule) {
			this(chars, rule, 0);
		}

		private Matcher(char[] chars, Rule rule, int start) {
			this(chars, rule, start, chars.length);

		}

		private Matcher(char[] chars, Rule rule, int start, int end) {
			this.chars = chars;
			this.mainRule = rule;
			this.end = end;
			this.start = start;
			this.index = start;
		}

		private Match match() {
			_startMatch();
			

			return new Match(this);
		}

		private void _startMatch() {

			_doMatch(mainRule);
			
			didMatch=false;

			if (errors==0 && index<end) {
				setErrorLocation(this.mainRule, "unexpected remaining characters");
				didMatch = false;
			}	

			if (errors == 0 && matches > 0) {
				didMatch = true;
			} else if (mainRule.optional == true) {
				didMatch = true;
			} 
			else {

				didMatch = false;
			}
		}

		private void _doMatch(Rule rule) {
			found = false;
			Rule previousParent = this.parentRule;
			this.parentRule = rule;

			if (rule.type == RuleType.SEQUENCE || rule.type == RuleType.ONE_OF) {
				_doMatchGroup(rule);

			} else if (rule.type == RuleType.CHAR_RANGE) {
				if (rule.optional) {
					_matchOptionalCharRange(rule);

				} else {
					_matchCharRange(rule);

				}
			} else if (rule.type == RuleType.EXACT_CHARS) {
				_matchCharRangeExact(rule);
			} else {
				throw new IllegalStateException("Rule type is not value "
						+ rule.type);
			}

			this.parentRule = previousParent;

		}

		private void _doMatchGroup(Rule rule) {
			// print ("group");

			int min = rule.min;
			int max = rule.max;
			int card;
			for (card = 0; card < max; card++) {

				if (index >= end) {
					break;
				}

				if (rule.type == RuleType.SEQUENCE) {
					_matchSequence(rule);
				} else if (rule.type == RuleType.ONE_OF) {
					_matchOneOf(rule);
				}

				if (!found) {
					// print("group not found exiting loop");
					break;
				}

			}

			if (card < min) {
				setErrorLocation(rule, "expected at least min " + min);
			} else if (card > max) {
				setErrorLocation(rule, "exceeded max " + min);
			}
			

		}

		private void _matchOptionalCharRange(Rule rule) {
			int start = index;
			_matchCharRange(rule);
			if (!found) {
				index = start;
				clearLastError();
			}

		}

		private void _matchCharRange(Rule rule) {

			char[] allowed = rule.allowed;
			int max = rule.max;
			int min = rule.min;

			int count = 0;
			for (; index < end && count < max; index++) {
				if (!isIn(chars[index], allowed)) {
						setErrorLocation(
							rule,
							fmt("Characters {0} not in allowed set {1}",
									list(chars), list(allowed)));
					break;
				} else {
					count++;
				}
			}
			if (count > max) {
				setErrorLocation(
						rule,
						fmt("Cardinality too high expected no more than {0} but is {1} for rule name {2.name}->{3.name}",
								min, count, this.parentRule, rule));
			} else if (count < min) {
				setErrorLocation(
						rule,
						fmt("Cardinality too low expected at least {0} but is {1} for rule name {2.name}->{3.name}",
								max, count, this.parentRule, rule));
			} else {
				setMatchPosition(rule);
			}
		}

		private final void clearLastError() {
			if (errors > 0) {
				errors--; // remove the last error
				this.errorsPositions[errors] = 0;
				this.errorReasons[errors] = null;
				this.errorsRulePositions[errors] = null;
			}
		}

		private void setErrorLocation(Rule rule, String message) {
			print ("error", errors+1, index, message);
			if (errors < 20) {
				errorsPositions[errors] = index;
				errorReasons[errors] = message;
				errorsRulePositions[errors] = rule;
			}
			errors++;
			
			found = false;
		}

		private void setMatchPosition(Rule rule) {

			found=true;
			lastMatch = index;
			matchPositions[matches] = index;
			matchedRules[matches] = rule;
			matches++;

			print("set match position", index, rule.name, matches);
		}

		private boolean inSequence() {
			if (this.mainRule == this.parentRule) {
				return false;
			}
			return parentRule != null
					&& this.parentRule.type != RuleType.SEQUENCE;
		}

		private boolean inOneOf() {
			if (this.mainRule == this.parentRule) {
				return false;
			}
			return parentRule != null
					&& this.parentRule.type != RuleType.SEQUENCE;
		}

		private void _matchCharRangeExact(Rule rule) {
			char[] allowed = rule.allowed;
			int aindex = 0;

			int lengthLeft = end - index;
			if (allowed.length > lengthLeft) {
				this.setErrorLocation(
						rule,
						fmt("Text '{0}' length is greater than comparison '{1}'",
								string(allowed), string(start, end, chars)));
				return;
			}

			if (allowed.length != lengthLeft && !inSequence() && !inOneOf()) {
				this.setErrorLocation(
						rule,
						fmt("Text '{0}' length not equal to comparison '{1}'",
								string(allowed), string(start, end, chars)));
				return;

			}

			for (; aindex < allowed.length; aindex++, index++) {
				char c1 = chars[index];
				char c2 = allowed[aindex];
				if (c1 != c2) {
					this.setErrorLocation(
							rule,
							fmt("Text '{1}' did not match '{0}'",
									string(allowed), string(start, end, chars)));
					return;
				}
			}
			this.setMatchPosition(rule);

		}

		private void _matchSequence(Rule rule) {
			for (Rule r : rule.rules) {
				_doMatch(r);
				if (!found) {
					break;
				} else {

				}
			}
		}

		private void _matchOneOf(Rule rule) {
			int start = 0;
			boolean match = false;
			print("matchOne");

			for (Rule r : rule.rules) {
				// print(index, end);

				if (index == end) {
					break;
				}
				start = index;
				
				_doMatch(r);
				if (!found) {
					this.clearLastError();
					index = start;
				} else {
					match = true;
					break;
				}
			}
			if (match == true) {
				setMatchPosition(rule);
			}
		}

	}

}
