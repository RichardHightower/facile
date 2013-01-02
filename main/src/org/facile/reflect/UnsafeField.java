package org.facile.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import sun.misc.Unsafe;

import static org.facile.Facile.*;

public class UnsafeField implements FieldAccess {
	
	

	public static Unsafe getUnsafe() {
		    try {
		            Field f = Unsafe.class.getDeclaredField("theUnsafe");
		            f.setAccessible(true);
		            return (Unsafe)f.get(null);
		    } catch (Exception e) { return null; }
	}

	static final Unsafe unsafe = getUnsafe();
	protected final Field field;
	protected final long offset;
	protected final boolean isFinal;
	protected final Object base;
	protected final boolean isStatic;
	protected final boolean isVolatile;
	protected final boolean qualified;
	protected final boolean readOnly;
	private final Class<?> type;
	private final String name;

	public UnsafeField(Field f) {
		
		name = f.getName();
		if (!name.startsWith("$")) {
			offset = unsafe.objectFieldOffset(f);
		} else {
			offset = -1;
		}
		field = f;
		isFinal = Modifier.isFinal(field.getModifiers());
		isStatic = Modifier.isStatic(field.getModifiers());
		
		if (isStatic) {
			base = unsafe.staticFieldBase(field);
		} else {
			base = null;
		}
		isVolatile = Modifier.isVolatile(field.getModifiers());
		qualified = isFinal || isVolatile;
		readOnly = isFinal || isStatic;
		type = f.getType();
	}
	

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getValue(java.lang.Object)
	 */
	@Override
	public Object getValue(Object obj) {
		if (type == pint) {
			int i = this.getInt(obj);
			return Integer.valueOf(i);
		} else if (type == plong) {
			long l = this.getLong(obj);
			return Long.valueOf(l);
		} else if (type == pbyte) {
			byte b = this.getByte(obj);
			return Byte.valueOf(b);
		} else if (type == pshort) {
			short s = this.getShort(obj);
			return Short.valueOf(s);
		} else if (type == pchar) {
			char c = this.getChar(obj);
			return Character.valueOf(c);
		} else if (type == pdouble) {
			double d = this.getDouble(obj);
			return Double.valueOf(d);
		} else if (type == pfloat) {
			float f = this.getFloat(obj);
			return Float.valueOf(f);
		} else {
			return this.getObject(obj);
		}
	}


	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getBoolean(java.lang.Object)
	 */
	@Override
	public boolean getBoolean(Object obj) {
		if (isVolatile) {
			return unsafe.getBooleanVolatile(obj, offset);
		} else {
			return unsafe.getBoolean(obj, offset);
		}
	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getInt(java.lang.Object)
	 */
	@Override
	public int getInt(Object obj) {
		if (isVolatile) {
			return unsafe.getIntVolatile(obj, offset);
		} else {
			return unsafe.getInt(obj, offset);
		}

	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getShort(java.lang.Object)
	 */
	@Override
	public short getShort(Object obj) {
		if (isVolatile) {
			return unsafe.getShortVolatile(obj, offset);
		} else {
			return unsafe.getShort(obj, offset);
		}

	}


	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getChar(java.lang.Object)
	 */
	@Override
	public char getChar(Object obj) {
		if (isVolatile) {
			return unsafe.getCharVolatile(obj, offset);
		} else {
			return unsafe.getChar(obj, offset);
		}

	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getLong(java.lang.Object)
	 */
	@Override
	public long getLong(Object obj) {
		if (isVolatile) {
			return unsafe.getLongVolatile(obj, offset);
		} else {
			return unsafe.getLong(obj, offset);
		}

	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getDouble(java.lang.Object)
	 */
	@Override
	public double getDouble(Object obj) {
		if (isVolatile) {
			return unsafe.getDoubleVolatile(obj, offset);
		} else {
			return unsafe.getDouble(obj, offset);
		}

	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getFloat(java.lang.Object)
	 */
	@Override
	public float getFloat(Object obj) {
		if (isVolatile) {
			return unsafe.getFloatVolatile(obj, offset);
		} else {
			return unsafe.getFloat(obj, offset);
		}

	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getByte(java.lang.Object)
	 */
	@Override
	public byte getByte(Object obj) {
		if (isVolatile) {
			return unsafe.getByteVolatile(obj, offset);
		} else {
			return unsafe.getByte(obj, offset);
		}

	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getObject(java.lang.Object)
	 */
	@Override
	public Object getObject(Object obj) {
		if (isVolatile) {
			return unsafe.getObjectVolatile(obj, offset);
		} else {
			return unsafe.getObject(obj, offset);
		}

	}
	
	

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getBoolean()
	 */
	@Override
	public boolean getBoolean() {
		return getBoolean(base);
	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getInt()
	 */
	@Override
	public int getInt() {
		return getInt(base);

	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getShort()
	 */
	@Override
	public short getShort() {
		return getShort(base);


	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getLong()
	 */
	@Override
	public long getLong() {
		return getLong(base);
	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getDouble()
	 */
	@Override
	public double getDouble() {
		return getDouble(base);
	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getFloat()
	 */
	@Override
	public float getFloat() {
		return getFloat(base);
	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getByte()
	 */
	@Override
	public byte getByte() {
		return getByte(base);
	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getObject()
	 */
	@Override
	public Object getObject() {
		return getObject(base);
	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getField()
	 */
	@Override
	public Field getField() {
		return field;
	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#isFinal()
	 */
	@Override
	public boolean isFinal() {
		return isFinal;
	}

	public Object getBase() {
		return base;
	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#isStatic()
	 */
	@Override
	public boolean isStatic() {
		return isStatic;
	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#isVolatile()
	 */
	@Override
	public boolean isVolatile() {
		return isVolatile;
	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#isQualified()
	 */
	@Override
	public boolean isQualified() {
		return qualified;
	}

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return readOnly;
	}
	

	/* (non-Javadoc)
	 * @see org.facile.reflect.FieldAccess#getType()
	 */
	@Override
	public Class<?> getType() {
		return type;
	}


	@Override
	public String getName() {
		return name;
	}

}
