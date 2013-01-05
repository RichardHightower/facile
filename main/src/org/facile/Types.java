package org.facile;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import static org.facile.Facile.*;

public class Types {
	static Class<Types> types = Types.class;

	private static final Logger log = log(types);

	public static int toInt(byte[] bytes, int offset) {
		
		ByteArrayInputStream bis = new ByteArrayInputStream (bytes, offset, bytes.length);
		DataInputStream instream = new DataInputStream(bis);
		try {
			return instream.readInt();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public static int toInt(byte[] bytes) {
		ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
		DataInputStream instream = new DataInputStream(bis);
		try {
			return instream.readInt();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

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
						warning(log, ex,
								"unable to convert to int and there was an exception %s",
								ex.getMessage());
					}					
				}
			} else {
			}
		} catch (Exception ex) {
			warning(log, ex,
					"unable to convert to int and there was an exception %s",
					ex.getMessage());
		}
		die("Unable to convert %s to a int", obj.getClass());
		return -666; //die throws an exception

	}


	public static byte toByte(Object obj) {
		try {
			if (obj instanceof Number) {
				return ((Number) obj).byteValue();
			} else if (obj instanceof CharSequence) {
				try {
					return Byte.parseByte(((CharSequence) obj).toString());
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
							return Byte.parseByte(str(buf));
						}
					} catch (Exception ex2) {
						warning(log, ex,
								"unable to convert to byte and there was an exception %s",
								ex.getMessage());
					}					
				}
			} else {
			}
		} catch (Exception ex) {
			warning(log, ex,
					"unable to convert to byte and there was an exception %s",
					ex.getMessage());
		}
		die("Unable to convert %s to a byte", obj.getClass());
		return -66; //die throws an exception

	}
	
	public static short toShort(Object obj) {
		try {
			if (obj instanceof Number) {
				return ((Number) obj).shortValue();
			} else if (obj instanceof CharSequence) {
				try {
					return Short.parseShort(((CharSequence) obj).toString());
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
							return Short.parseShort(str(buf));
						}
					} catch (Exception ex2) {
						warning(log, ex,
								"unable to convert to short and there was an exception %s",
								ex.getMessage());
					}					
				}
			} else {
			}
		} catch (Exception ex) {
			warning(log, ex,
					"unable to convert to byte and there was an exception %s",
					ex.getMessage());
		}
		die("Unable to convert %s to a short", obj.getClass());
		return -66; //die throws an exception

	}
	
	
	public static char toChar(Object obj) {
		try {
			if (obj instanceof Character) {
				return ((Character) obj).charValue();
			} else if (obj instanceof CharSequence) {
				obj.toString().charAt(0);
			} else {
			}
		} catch (Exception ex) {
			warning(log, ex,
					"unable to convert to byte and there was an exception %s",
					ex.getMessage());
		}
		die("Unable to convert %s to a byte", obj.getClass());
		return 'Z'; //die throws an exception

	}


	public static long toLong(Object obj) {
		try {
			if (obj instanceof Number) {
				return ((Number) obj).longValue();
			} else if (obj instanceof CharSequence) {
				try {
					return Long.parseLong(((CharSequence) obj).toString());
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
							return Long.parseLong(str(buf));
						}
					} catch (Exception ex2) {
						warning(log, ex,
								"unable to convert to long and there was an exception %s",
								ex.getMessage());

					}
				}
			} else {
			}
		} catch (Exception ex) {		
			warning(log, ex,
					"unable to convert to long and there was an exception %s",
					ex.getMessage());

		}
		
		die("Unable to convert %s to a long", obj.getClass());
		return -666; //die throws an exception

	}

	public static boolean toBoolean(Object obj) {

		Set<String> trueSet = set("t", "true", "True", "y", "yes", "1", "aye",
				"ofcourse", "T", "TRUE", "ok");
		if (obj instanceof String || obj instanceof CharSequence
				|| obj.getClass() == char[].class) {
			String str = str(obj);
			if (str.length() == 0) {
				return false;
			} else {
				return isIn(str, trueSet);
			}
		} else if (obj instanceof Boolean) {
			return ((Boolean) obj).booleanValue();
		} else if (isArray(obj) || obj instanceof Collection) {
			return len(obj) > 0;
		} else {
			return toBoolean(str(obj));
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
			}
		} catch (Exception ex) {
			warning(log, ex,
					"unable to convert to double and there was an exception %s",
					ex.getMessage());
		}
		
		die("Unable to convert %s to a double", obj.getClass());
		return -666d; //die throws an exception

	}

	public static float toFloat(Object obj) {
		try {
			if (obj instanceof Float) {
				return (Float) obj;
			} else if (obj instanceof Number) {
				return ((Number) obj).floatValue();
			} else if (obj instanceof CharSequence) {
				try {
					return Float.parseFloat(((CharSequence) obj).toString());
				} catch (Exception ex) {
					String svalue = str(obj);
					Matcher re = Regex.re(
							"[-+]?[0-9]+\\.?[0-9]+([eE][-+]?[0-9]+)?", svalue);
					if (re.find()) {
						svalue = re.group(0);
						return Float.parseFloat(svalue);
					}
					warning(log, "unable to convert to float after regex");
					return Float.NaN;
				}
			} else {
			}
		} catch (Exception ex) {
			warning(log,
					"unable to convert to float and there was an exception %s",
					ex.getMessage());
		}
		
		die("Unable to convert %s to a float", obj.getClass());
		return -666f; //die throws an exception

	}

	@SuppressWarnings("unchecked")
	public static <T> T coerce(Class<T> clz, Object value) {
		if (clz == integer || clz == pint) {
			Integer i = toInt(value);
			return (T) i;
		} else if (clz == lng || clz == plong) {
			Long l = toLong(value);
			return (T) l;
		} else if (clz == dbl || clz == pdouble) {
			Double i = toDouble(value);
			return (T) i;
		} else if (clz == flt || clz == pfloat) {
			Float i = toFloat(value);
			return (T) i;
		} else if (clz == sarray) {
			return (T) toStringArray(value);
		} else if (clz == bool || clz == pboolean) {
			Boolean b = toBoolean(value);
			return (T) b;
		} else if (clz == fileT) {
			return (T) toFile(value);
		} else {// TODO toFloat, toList, toArray

			return (T) value;
		}
	}

	public static File toFile(Object value) {
		if (value instanceof File) {
			return (File) value;
		} else if (value instanceof CharSequence) {
			return file(str(value));
		} else {
			return toFile(value.toString());
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
		if (obj == null) {
			return "";
		}
		if (obj instanceof String) {
			return (String) obj;
		} else {
			return obj.toString();
		}
	}

	public static Number toWrapper(long l) {
		if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE){
			return toWrapper((int)l);
		} else {
			return Long.valueOf(l);
		}
	}
	public static Number toWrapper(int i) {
		if (i >= Byte.MIN_VALUE && i <= Byte.MAX_VALUE) {
			return Byte.valueOf((byte)i);
		} else if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE) {
			return Short.valueOf((short)i);
		} else {
			return Integer.valueOf(i);
		} 
	}

	public static boolean isBasicType(Object value) {
		return (value instanceof Number || value instanceof CharSequence || 
		value instanceof Date || value instanceof Calendar);
	}
	
	public static boolean isBasicType(Class<?> theClass) {
		return (number.isAssignableFrom(theClass) || chars.isAssignableFrom(theClass) || date.isAssignableFrom(theClass)
				|| calendar.isAssignableFrom(theClass) || theClass.isPrimitive());
	}

}
