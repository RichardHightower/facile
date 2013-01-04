package org.facile.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectField implements FieldAccess {
	protected final Field field;
	protected final boolean isFinal;
	protected final boolean isStatic;
	protected final boolean isVolatile;
	protected final boolean qualified;
	protected final boolean readOnly;
	private final Class<?> type;
	private final String name;

	public ReflectField(Field f) {
		field = f;
		isFinal = Modifier.isFinal(field.getModifiers());
		isStatic = Modifier.isStatic(field.getModifiers());
		isVolatile = Modifier.isVolatile(field.getModifiers());
		qualified = isFinal || isVolatile;
		readOnly = isFinal || isStatic ;
		type = f.getType();
		name = f.getName();
	}
	
	@Override
	public Object getValue(Object obj) {
		try {
			return field.get(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean getBoolean(Object obj) {
		try {
			return field.getBoolean(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public int getInt(Object obj) {
		try {
			return field.getInt(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public short getShort(Object obj) {
		try {
			return field.getShort(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public char getChar(Object obj) {
		try {
			return field.getChar(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public long getLong(Object obj) {
		try {
			return field.getLong(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public double getDouble(Object obj) {
		try {
			return field.getDouble(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public float getFloat(Object obj) {
		try {
			return field.getFloat(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public byte getByte(Object obj) {
		try {
			return field.getByte(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object getObject(Object obj) {
		return getValue(obj);
	}
	
		@Override
	public boolean getBoolean() {
		return getBoolean(null);
	}

		@Override
	public int getInt() {
		return getInt(null);

	}

		@Override
	public short getShort() {
		return getShort(null);
	}


	@Override
	public long getLong() {
		return getLong(null);
	}

	
	@Override
	public double getDouble() {
		return getDouble(null);
	}

	@Override
	public float getFloat() {
		return getFloat(null);
	}

	@Override
	public byte getByte() {
		return getByte(null);
	}

	@Override
	public Object getObject() {
		return getObject(null);
	}

	@Override
	public Field getField() {
		return field;
	}


	@Override
	public boolean isFinal() {
		return isFinal;
	}


	@Override
	public boolean isStatic() {
		return isStatic;
	}

	@Override
	public boolean isVolatile() {
		return isVolatile;
	}


	@Override
	public boolean isQualified() {
		return qualified;
	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}
	

	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setValue(Object obj, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBoolean(Object obj, boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInt(Object obj, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setShort(Object obj, short value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChar(Object obj, char value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLong(Object obj, long value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDouble(Object obj, double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFloat(Object obj, float value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setByte(Object obj, byte vaue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObject(Object obj, Object value) {
		// TODO Auto-generated method stub
		
	}

}
