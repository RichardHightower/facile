package com.examples;

import java.lang.reflect.Method;

import static org.facile.Facile.*;

public class Generator {

	public static void main (String [] args) throws Exception {
		Class<Math> math = Math.class;
		Method[] methods = math.getDeclaredMethods();
		for (Method method : methods) {
			Appendable buf = buf(80);
			int index = 0;
			for (Class <?> type : method.getParameterTypes()) {
				add(buf, typeName(type));
				
				if (index == method.getParameterTypes().length-1) {
					
				}else {
					add(buf, ',');
				}
				index++;
			}

			if (method.getReturnType() == pfloat) {
				print("public static Func<"+ wrapperName(method.getReturnType()) +">",
					"f" + method.getName(),
					" = fn(",
					comma(typeName(method.getReturnType())),
					comma("math"),
					len(buf) > 0 ? comma(quote(method.getName())) : quote(method.getName()),
					buf,
					");"
					);
			} else  if (method.getReturnType() == pdouble){
				print("public static Func<"+ wrapperName(method.getReturnType()) +">",
						"d" + method.getName(),
						" = fn(",
						comma(typeName(method.getReturnType())),
						comma("math"),
						len(buf) > 0 ? comma(quote(method.getName())) : quote(method.getName()),
						buf,
						");"
						);				
				print("public static Func<"+ wrapperName(method.getReturnType()) +">",
						method.getName(),
						" = fn(",
						comma(typeName(method.getReturnType())),
						comma("math"),
						len(buf) > 0 ? comma(quote(method.getName())) : quote(method.getName()),
						buf,
						");"
						);				

			} else  if (method.getReturnType() == plong) {
				print("public static Func<"+ wrapperName(method.getReturnType()) +">",
						"l" + method.getName(),
						" = fn(",
						comma(typeName(method.getReturnType())),
						comma("math"),
						len(buf) > 0 ? comma(quote(method.getName())) : quote(method.getName()),
						buf,
						");"
						);				
				
			}else  if (method.getReturnType() == pint) {
				print("public static Func<"+ wrapperName(method.getReturnType()) +">",
						"i" + method.getName(),
						" = fn(",
						comma(typeName(method.getReturnType())),
						comma("math"),
						len(buf) > 0 ? comma(quote(method.getName())) : quote(method.getName()),
						buf,
						");"
						);				
				
			}

		}
		 
	}

	private static String typeName(Class<?> type) {
		if (type == plong || type == lng) {
			return "lng";
		} else if (type == pint || type == integer) {
			return "integer";
		} else if (type == pfloat || type == flt) {
			return "flt";
		} else if (type == pdouble || type == dbl) {
			return "dbl";
		} else {
			return type.getName();
		}
	}
	private static String wrapperName(Class<?> type) {
		if (type == pint || type == integer) {
			return "Integer";
		} else if (type == plong || type == lng) {
			return "Long";
		} else if (type == pfloat || type == flt) {
			return "Float";
		} else if (type == pdouble || type == dbl) {
			return "Double";
		} else {
			return type.getName();
		}
	}
}
