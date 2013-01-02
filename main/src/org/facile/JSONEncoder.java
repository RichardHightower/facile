package org.facile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Converts a Java object into a JSON string. I opted for ASCII safe encoding
 * after reading this:
 * http://stackoverflow.com/questions/583562/json-character-encoding
 * */
public class JSONEncoder  {

    boolean outputType = false; // turned off for now. Thinking of only
                                // supporting Plain JSON versus what Jackson
                                // does.

    private String encodeString(String str) {
        StringBuilder builder = new StringBuilder(str.length()
                + (int) (str.length() * 0.2f));
        builder.append("\""); //$NON-NLS-1$
        char[] charArray = str.toCharArray();

        for (int index = 0; index < charArray.length; index++) {
            char c = charArray[index];

            switch (c) {
            case '\"':
                builder.append('\\').append('\"');
                break;
            case '\\':
                builder.append('\\').append('\\');
                break;
            case '/':
                builder.append('\\').append('/');
                break;
            case '\b':
                builder.append('\\').append('b');
                break;
            case '\f':
                builder.append('\\').append('f');
                break;
            case '\n':
                builder.append('\\').append('n');
                break;
            case '\r':
                builder.append('\\').append('r');
                break;
            case '\t':
                builder.append('\\').append('t');
                break;

            default:
                /* Encode unicode character. */
                // if (!asciiEncoder.canEncode(c)){ //This works to but worried
                // it might be too slow http://en.wikipedia.org/wiki/ASCII
                if (c > 0x7F) { // See if it is out of range of ASCII
                    // I don't like this for performance, I am going to roll my
                    // own.
                    // builder.append(String.format("\\u%4H", c).replace(' ',
                    // '0'));
                    String hexString = Integer.toHexString(c).toUpperCase();
                    builder.append('\\').append('u');

                    if (hexString.length() >= 4) {
                        builder.append(hexString);
                    } else {
                        int howMany0 = 4 - hexString.length();
                        for (int i = 0; i < howMany0; i++) {
                            builder.append('0');
                        }
                        builder.append(hexString);
                    }
                } else {
                    builder.append(c);
                }

            }
        }
        builder.append("\""); //$NON-NLS-1$
        return builder.toString();
    }

    @SuppressWarnings("rawtypes")
	public String encode(Object obj) {
        try {
            if (obj == null) {
                return "null";
            }
            if (obj instanceof Number || obj instanceof Boolean) {
                return obj.toString();
            } else if (obj instanceof String) {
                return encodeString((String) obj);
            } else if (obj instanceof Collection) {
                Collection<?> collection = (Collection<?>) obj;
                Object[] array = collection.toArray(new Object[collection
                        .size()]);

                return encodeArray(array);

            } else if (obj instanceof Map) {
                return encodeMap((Map)obj);
            }
            else if (obj.getClass().isArray()) {
                return encodeArray((Object[]) obj);
            }

            else {
                return encodeObject(obj);
            }
        } catch (Exception ex) {
            JSONException.handleException(ex);
        }
        return null;

    }

	private String encodeObject(Object obj) throws IllegalAccessException,
			InvocationTargetException {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		if (outputType) {
		    builder.append("\"java_type\":\"");
		    builder.append(obj.getClass().getName());
		    builder.append('"');
		}

		List<Method> methodList = Reflection.getPropertyGetterMethods(obj.getClass());

		if (methodList.size() > 0) {
		    builder.append(',');
		}

		for (int index = 0; index < methodList.size(); index++) {
		    Method method = methodList.get(index);
		    String name = method.getName();
		    if (name.charAt(0) == 'g') {
		        name = name.substring(3);
		    } else {
		        name = name.substring(2);
		    }
		    name = "" + Character.toLowerCase(name.charAt(0))
		            + name.substring(1);
		    builder.append('\"');
		    builder.append(name);
		    builder.append("\":");
		    Object object = method.invoke(obj);
		    builder.append(encode(object));

		    if (index + 1 != methodList.size()) {
		        builder.append(',');
		    }
		}

		builder.append("}");
		return builder.toString();
	}

    @SuppressWarnings("nls")
    private String encodeArray(Object[] array) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int index = 0; index < array.length; index++) {
            builder.append(encode(array[index]));
            if (index != array.length - 1) {
                builder.append(',');
            }
        }
        builder.append("]");
        return builder.toString();
    }
    
    
    private String encodeMap(@SuppressWarnings("rawtypes") Map map) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (Object o : map.entrySet()) {
            @SuppressWarnings("rawtypes")
            Entry entry = (Entry) o;
            String key = entry.getKey().toString();
            builder.append("\"");
            builder.append(key);
            builder.append("\" : ");
            String value = encode(entry.getValue());
            builder.append(value);
            builder.append(",");
        }
        
        if (builder.charAt(builder.length()-1) == ',') {
            builder.deleteCharAt(builder.length()-1);
        }
        builder.append("}");
        return builder.toString();
    }


}
