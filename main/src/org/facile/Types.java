package org.facile;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import static org.facile.Facile.*;

public class Types {
	static Class<Types> types = Types.class;

	private static final Logger log = log(types);

	public static int toInt(Object obj) {
		try {
			if (obj instanceof Number) {
				return ((Number) obj).intValue();
			} else if (obj instanceof CharSequence) {
				try {
					return Integer.parseInt(((CharSequence) obj).toString());
				} catch (Exception ex) {
					char[] chars = chars(obj);
					Appendable buf = buf(chars.length);
					boolean found = false;
					for (char c : chars) {
						if (Character.isDigit(c) && !found) {
							found = true;
							add(buf, c);
						} else if (Character.isDigit(c) && found) {
							add(buf, c);
						} else if (!Character.isDigit(c) && found) {
						}
					}
					try {
						if (len(buf) > 0) {
							return Integer.parseInt(str(buf));
						}
					} catch (Exception ex2) {
						// noop
					}
					warning(log, "unable to convert to int");
					return obj.hashCode();
				}
			} else {
				String str = obj.toString();
				return toInt(str);
			}
		} catch (Exception ex) {
			warning(log,
					"unable to convert to int and there was an exception %s",
					ex.getMessage());
			return obj.hashCode();
		}
	}

	public static double toDouble(Object obj) {
		try {
			if (obj instanceof Double) {
				return (Double) obj;
			} else if (obj instanceof Number) {
				return ((Number) obj).doubleValue();
			} else if (obj instanceof CharSequence) {
				try {
					return Double.parseDouble(((CharSequence) obj).toString());
				} catch (Exception ex) {
					String svalue = str(obj);
					Matcher re = Regex.re(
							"[-+]?[0-9]+\\.?[0-9]+([eE][-+]?[0-9]+)?", svalue);
					if (re.find()) {
						svalue = re.group(0);
						return Double.parseDouble(svalue);
					}
					warning(log, "unable to convert to double after regex");
					return Double.NaN;
				}
			} else {
				String str = obj.toString();
				return toInt(str);
			}
		} catch (Exception ex) {
			warning(log,
					"unable to convert to int and there was an exception %s",
					ex.getMessage());
			return Double.NaN;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T coerce(Class<T> clz, Object value) {
		if (clz == integer || clz == pint) {
			Integer i = toInt(value);
			return (T) i;
		} else if (clz == dbl || clz == pdouble) {
			Double i = toDouble(value);
			return (T) i;
		} else if (clz == sarray) {
			return (T) toStringArray(value);
		} else {// TODO toFloat, toList, toArray
			return (T) value;
		}
	}

	public static String[] toStringArray(Object value) {
		if (value instanceof CharSequence) {
			String str = toString(value);

			List<Character> delims = ls(',', '\t', ' ', '|', ':', ';');
			char[] chars = chars(str);
			for (char c : delims) {
				if (isIn(c, chars)) {
					return split(chars, c);
				}
			}

		} else if (value instanceof List) {
			// complete this
		}

		return null;
	}

	public static String toString(Object obj) {
		if (obj instanceof String) {
			return (String) obj;
		} else {
			return obj.toString();
		}
	}

}
