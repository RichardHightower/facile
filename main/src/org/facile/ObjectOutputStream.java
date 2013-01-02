package org.facile;

import java.io.Closeable;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;


public class ObjectOutputStream extends OutputStream implements ObjectOutput, DataOutput, Flushable, Closeable{
	
	
	private DataOutputStream stream;
	private BinaryEncoder encoder = new BinaryEncoder();
	

	
	public ObjectOutputStream (OutputStream stream) {
		this.stream = new DataOutputStream(stream);
		encoder.setDataOutput(this.stream);
	}
	
	public ObjectOutputStream (DataOutputStream stream) {
		this.stream = stream;
		encoder.setDataOutput(this.stream);
	}

	@Override
	public void writeObject(Object obj) throws IOException {
		encoder.encodeValue(obj);
	}

	public void writeArray(Object obj) throws IOException {
		encoder.encodeArray(obj);
	}


	public void writeFloatArray(float[] array) throws IOException {
		encoder.encodeFloatArray(array);
	}

	public void writeDoubleArray(double[] array) throws IOException {
		encoder.encodeDoubleArray(array);
		
	}

	public void writeCharArray(char[] array) throws IOException {
		encoder.encodeCharArray(array);
	}

	public void writeIntArray(int[] array) throws IOException {
		encoder.encodeIntArray(array);
	}
	
	public void writeShortArray(short[] array) throws IOException {
		encoder.encodeShortArray(array);
	}
	
	public <K, V> void writeMap(Map<K, V> map) throws IOException {
		encoder.encodeMap(map);
	}



	@Override
	public void write(int b) throws IOException {
		encoder.encodeByte((byte)b );		
	}

	@Override
	public void writeBoolean(boolean v) throws IOException {
		encoder.encodeBoolean(v );
		
	}

	@Override
	public void writeByte(int b) throws IOException {
		encoder.encodeByte((byte)b );				
	}

	@Override
	public void writeShort(int v) throws IOException {
		encoder.encodeShort((short)v );				
		
	}

	@Override
	public void writeChar(int v) throws IOException {
		encoder.encodeChar((char)v );						
	}

	@Override
	public void writeInt(int v) throws IOException {
		encoder.encodeInteger(v );				
	}

	public void writeUnsignedInt(long v) throws IOException {
		encoder.encodeUnsignedInteger(v);
	}

	public void writeUnsignedByte(short v) throws IOException {
		encoder.encodeUnsignedByte(v);
	}

	@Override
	public void writeLong(long v) throws IOException {
		encoder.encodeLong(v );					
	}

	@Override
	public void writeFloat(float v) throws IOException {
		encoder.encodeFloat(v );					
		
	}

	@Override
	public void writeDouble(double v) throws IOException {
		encoder.encodeDouble(v );							
	}

	@Override
	public void writeBytes(String s) throws IOException {
		encoder.encodeString(s );							
		
	}

	@Override
	public void writeChars(String s) throws IOException {
		encoder.encodeString(s );							
		
	}

	@Override
	public void writeUTF(String s) throws IOException {
		encoder.encodeString(s );							
		
	}


	@Override
	public void write(byte[] b) throws IOException {
		encoder.encodeByteArray(b);		
	}


	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		stream.write(b, off, len);
	}


	@Override
	public void flush() throws IOException {
		stream.flush();
	}


	@Override
	public void close() throws IOException {
		stream.close();		
	}

	public void writeLongArray(long[] array) throws IOException {
		encoder.encodeLongArray(array);
	}

	public void writeByteArray(byte[] array) throws IOException {
		encoder.encodeByteArray(array);
	}

	public void writeDate(Date date) throws IOException {
		encoder.encodeDate(date);
		
	}

	public void writeDate(Date date, boolean timeZone) throws IOException {
		encoder.encodeDate(date, timeZone);
		
	}


}
