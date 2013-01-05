package org.facile;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.facile.Facile.Entry;
import org.facile.Facile.Func;
import org.facile.reflect.FieldAccess;
import org.facile.reflect.ReflectField;
import org.facile.reflect.UnsafeField;

import static org.facile.Facile.*;

public class Reflection {

	static Class<Reflection> reflection = Reflection.class;

	private static final Logger log = Facile.log(reflection);

	static Func<Field> isStaticField = fn(Field.class, reflection,
			"isStaticField");

	@SuppressWarnings("serial")
	public static class ReflectionException extends RuntimeException {

		public ReflectionException() {
			super();
		}

		public ReflectionException(String message, Throwable cause) {
			super(message, cause);
		}

		public ReflectionException(String message) {
			super(message);
		}

		public ReflectionException(Throwable cause) {
			super(cause);
		}
	}

	public static boolean isArray(Object obj) {
		return obj.getClass().isArray();
	}

	public static boolean isStaticField(Field field) {
		return Modifier.isStatic(field.getModifiers());
	}

	@SuppressWarnings("unchecked")
	public static <V> V[] array(List<V> list) {
		if (list.size() > 0) {
			Object newInstance = Array.newInstance(list.get(0).getClass(),
					list.size());
			return (V[]) list.toArray((V[]) newInstance);
		} else {
			Facile.complain("array(list): The list has to have at least one item in it");
			return null;
		}
	}

	public static class FuncImpl<T> implements Func<T> {
		Method method;
		Object that;

		FuncImpl(Class<T> clazz, Method method, Object that) {
			method.setAccessible(true);
			this.method = method;
			this.that = that;
		}

		@SuppressWarnings("unchecked")
		public T execute(Object... params) {

			notNull(method);
			notNull(params);
			T ret = null;
			try {
				ret = (T) method.invoke(that, params);
			} catch (Exception ex) {
				throw new ReflectionException("unable to execute function "
						+ method.getName() + " of " + method, ex);
			}
			return ret;
		}

	}

	public static <T> Func<T> f(Class<T> returnType, Object that) {
		try {
			return fn(returnType, that, "f");
		} catch (Exception e) {
			throw new ReflectionException(e);
		}
	}

	public static Func<?> f(Object that) {
		try {
			return fn(that, "f");
		} catch (Exception e) {
			throw new ReflectionException(e);
		}
	}

	public static Func<?> fn(Object that, Object name) {
		return fn(Object.class, that, name);
	}

	public static <T> Func<T> fn(Class<T> returnType, Object that, Object name) {
		return doFuncLookup(returnType, that, name, -1, (Class<?>[]) null);
	}

	public static <T> Func<T> fn(Class<T> returnType, Object that, Object name,
			int numArgs) {
		return doFuncLookup(returnType, that, name, numArgs, (Class<?>[]) null);
	}

	public static <T> Func<T> fn(Class<T> returnType, Object that, Object name,
			Class<?>... argTypes) {
		return doFuncLookup(returnType, that, name, -1, argTypes);
	}

	private static <T> Func<T> doFuncLookup(Class<T> returnType, Object that,
			Object name, int numArgs, Class<?>... args) {
		try {
			Method[] methods = clazz(that).getDeclaredMethods();
			if (methods.length == 1) {
				methods[0].setAccessible(true);
				if (Modifier.isStatic(methods[0].getModifiers())) {
					return new FuncImpl<T>(returnType, methods[0], null);
				} else {
					return new FuncImpl<T>(returnType, methods[0], that);
				}
			}
			// Prefer static methods
			for (Method m : methods) {
				if (m.getName().equals(name.toString())
						&& Modifier.isStatic(m.getModifiers())) {
					if (numArgs == -1 && (args == null || args.length == 0)) {
						return new FuncImpl<T>(returnType, m, null);
					} else if (numArgs > -1
							&& m.getParameterTypes().length == numArgs) {
						return new FuncImpl<T>(returnType, m, null);
					} else if (args != null && args.length > 0) {
						Class<?>[] types = m.getParameterTypes();
						boolean noMatch = false;
						int index = 0;
						for (Class<?> argType : types) {
							Class<?> matchType = args[index];
							if (matchType != argType) {

								if (argType.isPrimitive()) {
									if (argType == pint && matchType == integer) {

									} else if (argType == pfloat
											&& matchType == flt) {

									} else if (argType == pdouble
											&& matchType == dbl) {

									} else if (argType == plong
											&& matchType == lng) {

									} else {
										noMatch = true;
										break;
									}
								} else {
									break;
								}
							}
							index++;
						}
						if (!noMatch) {
							return new FuncImpl<T>(returnType, m, null);
						}
					}
				}
			}

			// Accept non static methods

			if (that instanceof Class) {
				Constructor<?> constructor = ((Class<?>) that)
						.getDeclaredConstructors()[0];
				constructor.setAccessible(true);
				that = constructor.newInstance((Object[]) null);

				return doFuncLookup(returnType, that, name, numArgs, args);
			}

			for (Method m : methods) {
				if (m.getName().equals(name.toString())
						&& !Modifier.isStatic(m.getModifiers())) {
					if (numArgs == -1 && (args == null || args.length == 0)) {
						return new FuncImpl<T>(returnType, m, null);
					} else if (numArgs > -1
							&& m.getParameterTypes().length == numArgs) {
						return new FuncImpl<T>(returnType, m, null);
					} else {
						Class<?>[] types = m.getParameterTypes();
						boolean noMatch = false;
						int index = 0;
						for (Class<?> t : types) {
							if (args[index] != t) {
								noMatch = true;
								break;
							}
							index++;
						}
						if (!noMatch) {
							return new FuncImpl<T>(returnType, m, null);
						}
					}
				}
			}

		} catch (Exception ex) {
			Facile.error(log, ex, "Unable to find function that=%s, name=%s",
					that, name);
		}
		Facile.die("Unable to find function that=%s, name=%s", that, name);
		return null;
	}

	public static int arrayLength(Object obj) {
		return Array.getLength(obj);
	}

	public static void copyArgs(Class<?> clz, Map<String, ?> args) {

		Collection<Field> fields = Facile.gfilter(isStaticField,
				clz.getDeclaredFields());

		for (Field field : fields) {
			field.setAccessible(true);
			try {
				Object value = args.get(field.getName());
				if (value == null) {
					continue;
				}
				field.set(null, Facile.coerce(field.getType(), value));
			} catch (Exception e) {
				handle(e);
			}
		}

	}

	private static void handle(Exception ex) {
		throw new ReflectionException(ex);
	}

	private static Class<?> clazz(Object that) {
		if (that instanceof Class) {
			return (Class<?>) that;
		} else {
			return that.getClass();
		}
	}

	public static Object idx(Object object, int index) {
		object = Array.get(object, index);
		return object;
	}

	public static void idx(Object object, int index, Object value) {
		Array.set(object, index, value);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getProperty(Class<T> t, Object object, final String key) {
		return (T) getProp(object, key);
	}

	public static Object getProp(Object object, final String key) {
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

	public static List<Method> getPropertyGetterMethods(
			Class<? extends Object> theClass) {

		Method[] methods = theClass.getMethods();

		List<Method> methodList = new ArrayList<Method>(methods.length);

		for (int index = 0; index < methods.length; index++) {
			Method method = methods[index];
			String name = method.getName();

			if (method.getParameterTypes().length > 0
					|| method.getReturnType() == Void.class
					|| !(name.startsWith("get") || name.startsWith("is"))
					|| name.equals("getClass")) {
				continue;
			}
			methodList.add(method);

		}
		return methodList;
	}

	private static boolean _useUnsafe;
	static {
		try {
			Class.forName("sun.misc.Unsafe");
			_useUnsafe = true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			_useUnsafe = false;
		}
	}

	private static final boolean useUnsafe = _useUnsafe;

	@SuppressWarnings("unchecked")
	public static <T> T fromMap(Map<String, Object> map, Class<T> clazz) {
		return (T) fromMap(map);
	}

	public static Object fromMap(Map<String, Object> map) {
		String className = (String) map.get("class");
		Object newInstance = null;
		Class<?> clazz = null;

		try {
			clazz = Class.forName(className);
			newInstance = clazz.newInstance();
		} catch (Exception ex) {
			info("we were not able to load the class so we are leaving this as a map");
			return map;
		}

		List<FieldAccess> fields = getAllAccessorFields(clazz);

		for (FieldAccess field : fields) {
			Object value = map.get(field.getName());
			if (value!=null) {
				field.setValue(newInstance, value);
			}
		}

		return newInstance;
	}

	public static Map<String, Object> toMap(final Object object) {
		Map<String, Object> map = new HashMap<>();

		class FieldToEntryConverter implements
				Converter<Entry<String, Object>, FieldAccess> {
			@Override
			public Entry<String, Object> convert(FieldAccess from) {
				if (from.isReadOnly()) {
					return null;
				}
				Entry<String, Object> entry = new EntryImpl<>(from.getName(),
						from.getValue(object));
				return entry;
			}
		}
		List<FieldAccess> fields = getAllAccessorFields(object.getClass());
		fields = new ArrayList<>(fields);
		Collections.reverse(fields); //make super classes fields first that their values get overriden by subclass fields with the same name

		List<Entry<String, Object>> entries = mapFilterNulls(
				new FieldToEntryConverter(), fields);

		map.put("class", object.getClass().getName());

		for (Entry<String, Object> entry : entries) {
			Object value = entry.value();
			if (value == null) {
				continue;
			}
			if (Types.isBasicType(value)) {
				map.put(entry.key(), entry.value());
			} else if (isArray(value)
					&& Types.isBasicType(value.getClass().getComponentType())) {
				map.put(entry.key(), entry.value());
			} else if (isArray(value)) {
				int length = len(value);
				List<Map<String, Object>> list = new ArrayList<>(length);
				for (int index = 0; index < length; index++) {
					Object item = idx(value, index);
					list.add(toMap(item));
				}
				map.put(entry.key(), list);
			} else if (value instanceof Collection) {
				Collection<?> collection = (Collection<?>) value;
				Class<?> componentType = getComponentType(collection);
				if (Types.isBasicType(componentType)) {
					map.put(entry.key(), value);
				} else {
					List<Map<String, Object>> list = new ArrayList<>(
							collection.size());
					for (Object item : collection) {
						list.add(toMap(item));
					}
					map.put(entry.key(), list);
				}
			} else if (value instanceof Map) {

			} else {
				map.put(entry.key(), toMap(value));
			}
		}
		return map;
	}

	public static Class<?> getComponentType(Collection<?> value) {
		if (value.size() > 0) {
			Object next = value.iterator().next();
			return next.getClass();
		} else {
			return object;
		}
	}

	public static class FieldConverter implements Converter<FieldAccess, Field> {
		@Override
		public FieldAccess convert(Field from) {
			if (useUnsafe) {
				return new UnsafeField(from);
			} else {
				return new ReflectField(from);
			}
		}
	}

	static Map<Class<? extends Object>, List<FieldAccess>> allAccessorFieldsCache = new ConcurrentHashMap<>();

	public static List<FieldAccess> getAllAccessorFields(
			Class<? extends Object> theClass) {
		List<FieldAccess> list = allAccessorFieldsCache.get(theClass);
		if (list == null) {
			list = map(new FieldConverter(), getAllFields(theClass));
		} else {
			allAccessorFieldsCache.put(theClass, list);
		}
		return list;
	}

	public static List<Field> getAllFields(Class<? extends Object> theClass) {
		List<Field> list = getFields(theClass);
		while (theClass != object) {
			theClass = theClass.getSuperclass();
			getFields(theClass, list);
		}
		return list;
	}

	public static void getFields(Class<? extends Object> theClass,
			List<Field> list) {
		List<Field> more = getFields(theClass);
		list.addAll(more);
	}

	public static List<Field> getFields(Class<? extends Object> theClass) {
		List<Field> list = list(theClass.getDeclaredFields());
		for (Field field : list) {
			field.setAccessible(true);
		}
		return list;
	}

}
