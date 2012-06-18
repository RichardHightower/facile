package org.facile;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.facile.Facile.copy;
import static org.facile.Facile.index;

class Templating {
	public static String mfmt(String str, Map<?, ?> map) {
		return mapFormat(str, map);
	}


	private static int _lookForFormatRules(_context cx, int i, char c) {
		cx.formatRules = false;
		for (; i < cx.ca.length && c != '}'; i++) {
			c = cx.ca[i];
			if (c == '[' || c == '!' || c == ':' || c == '.') {
				cx.formatRules = true;
			}
			if (c != '}') {
				if (!cx.formatRules) {
					cx.contents.append(c);
				} else {
					cx.formatRulesContents.append(c);
				}
			}
		}
		return i;
	}

	@SuppressWarnings("unchecked")
	private static int _handleLoop(int index, _context cx) {
		char c = '#';
		StringBuilder builder = new StringBuilder(256);
		String endloop = "{@endloop}";
		

		for (; index < cx.ca.length && c != '}'; index++) {
			c = cx.ca[index];
			if (c != '}')
				builder.append(c);
		}

		String expression = builder.toString();
		builder.setLength(0);

		char [] ca = cx.ca;
		for (; index < cx.ca.length; index++) {
			c = cx.ca[index];
			if (c == '{' && index + endloop.length() < ca.length) {
				int x = index;
				if (ca[x + 1] == '@' && ca[x + 2] == 'e' && ca[x + 3] == 'n'
						&& ca[x + 4] == 'd' && ca[x + 5] == 'l'
						&& ca[x + 6] == 'o' && ca[x + 7] == 'o'
						&& ca[x + 8] == 'p' && ca[x + 9] == '}') {
					index += endloop.length();
					break;
				}
			}
			builder.append(c);
		}

		String body = builder.toString();
		
		Object lobj = lookup(cx.namespace, expression);
		
		if (lobj instanceof List) {
			List <Object> list = (List<Object>) lobj;
			for (Object obj : list){
				((Map<String, Object>)cx.namespace).put("iter", obj);
				cx.builder.append(mfmt(body, cx.namespace));
			}
		}

		return index;
	}
	
	private static int _handleIf(int index, _context cx) {
		char c = '#';
		StringBuilder builder = new StringBuilder(256);
		String endloop = "{@endif}";
		

		for (; index < cx.ca.length && c != '}'; index++) {
			c = cx.ca[index];
			if (c != '}')
				builder.append(c);
		}

		String expression = builder.toString();
		builder.setLength(0);

		char [] ca = cx.ca;
		for (; index < cx.ca.length; index++) {
			c = cx.ca[index];
			if (c == '{' && index + endloop.length() < ca.length) {
				int x = index;
				if (ca[x + 1] == '@' && ca[x + 2] == 'e' && ca[x + 3] == 'n'
						&& ca[x + 4] == 'd' && ca[x + 5] == 'i'
						&& ca[x + 6] == 'f' && ca[x + 7] == '}') {
					index += endloop.length();
					break;
				}
			}
			builder.append(c);
		}

		String body = builder.toString();
		
		Boolean bool = (Boolean)lookup(cx.namespace, expression);
		if (bool) {
			cx.builder.append(mfmt(body, cx.namespace));
		}
		return index;
	}

	

	private static class _context {
		_context(Map<?, ?> map, String str) {
			this(str);
			this.namespace = copy(map);
		}

		_context(String str, Object... args) {
			this.str = str;
			this.args = args;
			charArray = this.str.toCharArray();
			ca = charArray;
			builder = new StringBuilder(str.length());
			contents = new StringBuilder(32);
			formatRulesContents = new StringBuilder(32);
			this.namespace = new HashMap<String, Object>();
		}

		Map<?, ?> namespace;
		String str;
		StringBuilder builder;
		StringBuilder contents;
		StringBuilder formatRulesContents;
		int argIndexKeeper = 0;
		int argIndex = -666;
		Object[] args;
		char charArray[];
		char ca[];
		boolean formatRules = false;
		final String loop = "@loop:";
		final String ifCmd = "@if:";
		int i;

	}
	
	
	public static String mapFormat(String astr, Map<?, ?> amap) {

		_context cx = new _context(amap, astr);

		for (; cx.i < cx.ca.length; cx.i++) {
			char c = cx.ca[cx.i];
			if (c == '{') {
				cx.contents.setLength(0);
				cx.formatRulesContents.setLength(0);
				cx.i++;

				if (_ifHandledLoopCmd(cx, cx.i)) {
					//
				} else if (_ifHandledIfCmd(cx, cx.i)) {
					
				}
				else {

					cx.i = _lookForFormatRules(cx, cx.i, c);

					if (!cx.formatRules) {
						cx.builder.append(cx.namespace.get(cx.contents
								.toString()));
					} else {
						cx.builder.append(_formatRule(
								cx.namespace.get(cx.contents.toString()),
								cx.formatRulesContents.toString()));
					}

					cx.i--;
				}
			} else {
				cx.builder.append(c);
			}
		}
		return cx.builder.toString();
	}


	public static String arrayFormat(String astr, Object... aargs) {
		_context cx = new _context(astr, aargs);

		for (; cx.i < cx.charArray.length; cx.i++) {
			char c = cx.charArray[cx.i];
			if (c == '{') {
				cx.contents.setLength(0);
				cx.formatRulesContents.setLength(0);
				cx.i++;

				if (_ifHandledLoopCmd(cx, cx.i)) {
					//
				} else {

					cx.i = _lookForFormatRules(cx, cx.i, c);
					_addTemplateValueToStringBuilder(cx);
					cx.i--;
				}
			} else {
				cx.builder.append(c);
			}
		}
		return cx.builder.toString();
	}

	private static boolean _ifHandledLoopCmd(_context cx, int i) {
		if( i + cx.loop.length() < cx.ca.length && cx.ca[i] == '@'
				&& cx.ca[i + 1] == 'l' && cx.ca[i + 2] == 'o'
				&& cx.ca[i + 3] == 'o' && cx.ca[i + 4] == 'p'
				&& cx.ca[i + 5] == ':') {
			cx.i = _handleLoop(i + cx.loop.length(), cx);
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean _ifHandledIfCmd(_context cx, int i) {
		if( i + cx.loop.length() < cx.ca.length && cx.ca[i] == '@'
				&& cx.ca[i + 1] == 'i' && cx.ca[i + 2] == 'f'
				&& cx.ca[i + 3] == ':') {
			cx.i = _handleIf(i + cx.ifCmd.length(), cx);
			return true;
		} else {
			return false;
		}
	}


	private static void _addTemplateValueToStringBuilder(_context cx) {
		String sindex = cx.contents.toString().trim();
		if (sindex.isEmpty()) {
			cx.argIndex = cx.argIndexKeeper;
			addTemplateValueToStringBuilder(cx);
			cx.argIndexKeeper++;
		} else {
			cx.argIndex = Integer.parseInt(sindex);
			addTemplateValueToStringBuilder(cx);
		}
	}

	private static void addTemplateValueToStringBuilder(_context cx) {
		Object obj = cx.args[cx.argIndex];
		if (obj==null) {
			obj = "NULL";
		}
		if (!cx.formatRules) {
			cx.builder.append(obj.toString());
		} else {
			cx.builder.append(_formatRule(obj,
					cx.formatRulesContents.toString()));
		}
	}

	private static String _formatRule(Object object, String rule) {
		StringBuilder format = new StringBuilder(16);
		StringBuilder idx = new StringBuilder(16);
		List<Object> idxs = new ArrayList<Object>(16);
		char[] charArray = rule.toCharArray();

		boolean hasFormat = false;
		boolean hasIndex = false;
		boolean hasDotIndex = false;

		for (int index = 0; index < charArray.length; index++) {
			char c = charArray[index];

			if (hasFormat) {
				format.append(c);
			} else if (hasIndex) {
				if (c == ']') {
					idxs.add(Integer.parseInt(idx.toString()));
					hasIndex = false;
				} else {
					idx.append(c);
				}
			} else if (hasDotIndex) {
				if (c == '[' || c == '.' || c == ':') {
					idxs.add(idx.toString());
					hasDotIndex = false;
				} else {
					idx.append(c);
					if (index + 1 == charArray.length) {
						idxs.add(idx.toString());
						hasDotIndex = false;
					}
				}
			}

			if (c == ':') {
				hasFormat = true;
				format.setLength(0);
			} else if (c == '[') {
				hasIndex = true;
				idx.setLength(0);
			} else if (c == '.') {
				hasDotIndex = true;
				idx.setLength(0);
			}

		}
		if (hasFormat) {
			format.insert(0, '%');
		}

		for (Object oindex : idxs) {
			if (oindex instanceof Integer) {
				object = _indexLookup(object, oindex);
			} else {
				String key = (String) oindex;
				if (object instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<String, ?> map = (Map<String, ?>) object;
					object = map.get(key);
				} else {
					object = _propLookup(object, key);
				}
			}
		}
		if (object == null) {
			return "";
		} else if (hasFormat) {
			return String.format(format.toString(), object);
		} else {
			return object.toString();
		}

	}

	public static Object lookup(Object object, String expression) {
		StringBuilder idx = new StringBuilder(16);
		StringBuilder op = new StringBuilder(16);
		List<Object> idxs = new ArrayList<Object>(16);
		
		List<Object> leftIdxs = null;

		char[] charArray = expression.toCharArray();
		

		boolean hasIndex = false;
		boolean hasDotIndex = false;
		
		if (charArray[0]=='[') {
			hasIndex = true;
		} else {
			hasDotIndex = true;
		}

		for (int index = 0; index < charArray.length; index++) {
			char c = charArray[index];

			if (hasIndex) {
				if (c == ']') {
					idxs.add(Integer.parseInt(idx.toString()));
					hasIndex = false;
				} else {
					idx.append(c);
				}
			} else if (hasDotIndex) {
				if (c == '[' || c == '.' || c == ':' || c=='>' || c=='<' || c=='=' || c==' ' || c=='!')  {
					idxs.add(idx.toString());
					hasDotIndex = false;
				} else {
					idx.append(c);
					if (index + 1 == charArray.length) {
						idxs.add(idx.toString());
						hasDotIndex = false;
					}
				}
			}

			if (c == '[') {
				hasIndex = true;
				idx.setLength(0);
			} else if (c == '.') {
				hasDotIndex = true;
				idx.setLength(0);
			} else if (c=='>' || c=='<' || c=='=' || c==' ' || c=='!') {
				op.setLength(0);
				op.append(c);
				
				inner:
				for (int x = 1; x < 5; x++) {
					char ch = '#';
					if (index+x <  charArray.length) {
						ch = charArray[index+x];
						if (ch=='>' || ch=='<' || ch=='=' || ch=='!' || ch==' ') {
							op.append(charArray[index+x]);						
						} else {
							leftIdxs = idxs;
							idxs = new ArrayList<Object>(16);
							if (ch=='[') {
								hasIndex = true;
							} else {
								hasDotIndex = true;
							}
							idx.setLength(0);
							index = index+x-1;
							break inner;
						}
					}
				}
			}

		}
		
		if (leftIdxs==null) {
			object = findByPath(object, idxs);
			return object;
		} else {
			Object left =  findByPath(object, leftIdxs);
			//Object right = findByPath(root, idxs);
			return left; //later evaluate
		}

	}

	private static Object findByPath(Object object, List<Object> idxs) {
		for (Object oindex : idxs) {
			if (oindex instanceof Integer) {
				object = _indexLookup(object, oindex);
			} else {
				String key = (String) oindex;
				if (object instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<String, ?> map = (Map<String, ?>) object;
					object = map.get(key);
				} else {
					object = _propLookup(object, key);
				}
			}
		}
		return object;
	}

	private static Object _indexLookup(Object object, Object oindex) {
		int index = (Integer) oindex;
		if (object.getClass().isArray()) {
			object = Array.get(object, index);
		} else if (object instanceof List) {
			object = index(((List<?>) object), index);
		} else {
			object = null;
		}
		return object;
	}

	private static Object _propLookup(Object object, final String key) {
		if (object == null) {
			return null;
		}
		object.getClass().getDeclaredMethods();
		Class<? extends Object> clz = object.getClass();
		outer: while (clz != Object.class) {
			Method[] methods = clz.getDeclaredMethods();
			for (Method method : methods) {
				method.setAccessible(true);
				if (method.getParameterTypes().length == 0
						&& method.getName().toLowerCase()
								.endsWith(key.toLowerCase())
						&& (method.getName().startsWith("is")
								|| method.getName().startsWith("get") || method
								.getName().length() == key.length())) {
					try {
						object = method.invoke(object, (Object[]) null);
						break outer;
					} catch (Exception ex) {
						continue;
					}
				}
			}
			Field[] declaredFields = clz.getDeclaredFields();
			for (Field field : declaredFields) {
				field.setAccessible(true);
				if (field.getName().equals(key)) {
					try {
						object = field.get(object);
						break outer;
					} catch (Exception ex) {
						break;
					}
				}
			}

			clz = clz.getSuperclass();
		}
		return object;

	}

	
	private interface NamespaceResolver<KEY> {
		Object lookup(KEY key);
	}

	@SuppressWarnings("unused")
	private class ObjectArrayResolver implements NamespaceResolver<Integer> {

		Object[] args;

		ObjectArrayResolver(Object[] args) {
			this.args = args;
		}

		public Object lookup(Integer index) {
			return args[index];
		}

	}

	public static String cfmt(String str, char... args) {
		return cformat(str, args);
	}

	public static String cformat(String str, char... args) {
		StringBuilder builder = new StringBuilder(str.length());
		StringBuilder contents = new StringBuilder(32);

		int argIndexKeeper = 0;
		int argIndex = -666;

		char[] charArray = str.toCharArray();
		for (int index = 0; index < charArray.length; index++) {
			char c = charArray[index];
			if (c == '{') {
				contents.setLength(0);
				index++;

				for (; index < charArray.length && c != '}'; index++) {
					c = charArray[index];
					if (c != '}') {
						contents.append(c);
					}
				}

				String sindex = contents.toString().trim();
				if (sindex.isEmpty()) {
					argIndex = argIndexKeeper;
					builder.append(args[argIndex]);
					argIndexKeeper++;
				} else {
					argIndex = Integer.parseInt(sindex);
					builder.append(args[argIndex]);
				}
				index--;
			} else {
				builder.append(c);
			}
		}
		return builder.toString();
	}

}
