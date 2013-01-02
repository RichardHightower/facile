package org.facile.reflect;

import java.lang.reflect.Field;

public interface FieldAccess {
    public abstract String getName();
    
	public abstract Object getValue(Object obj);

	public abstract boolean getBoolean(Object obj);

	public abstract int getInt(Object obj);

	public abstract short getShort(Object obj);

	public abstract char getChar(Object obj);

	public abstract long getLong(Object obj);

	public abstract double getDouble(Object obj);

	public abstract float getFloat(Object obj);

	public abstract byte getByte(Object obj);

	public abstract Object getObject(Object obj);

	public abstract boolean getBoolean();

	public abstract int getInt();

	public abstract short getShort();

	public abstract long getLong();

	public abstract double getDouble();

	public abstract float getFloat();

	public abstract byte getByte();

	public abstract Object getObject();

	public abstract Field getField();

	public abstract boolean isFinal();

	public abstract boolean isStatic();

	public abstract boolean isVolatile();

	public abstract boolean isQualified();

	public abstract boolean isReadOnly();

	public abstract Class<?> getType();

}