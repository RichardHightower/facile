package org.facile;

import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.util.Date;
import java.util.Map;


public class ObjectInputStream  extends InputStream implements ObjectInput, DataInput, Closeable{
	
	
	private DataInputStream stream;
	private BinaryDecoder decoder = new BinaryDecoder();
	

	
	public ObjectInputStream (InputStream stream) {
		this.stream = new DataInputStream(stream);
		decoder.setDataInput(this.stream);
	}
	
	public ObjectInputStream (DataInputStream stream) {
		this.stream = stream;
	}



	@Override
	public void close() throws IOException {
		stream.close();		
	}

	@Override
	public int read() throws IOException {
		return stream.read();
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		stream.readFully(b);
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		stream.readFully(b, off, len);
		
	}

	@Override
	public int skipBytes(int n) throws IOException {
		return stream.skipBytes(n);
	}

	@Override
	public boolean readBoolean() throws IOException {
		return decoder.decodeBoolean();
	}

	@Override
	public byte readByte() throws IOException {
		return decoder.decodeByte();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return decoder.decodeUnsignedByte();
	}

	@Override
	public short readShort() throws IOException {
		return decoder.decodeShort();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return decoder.decodeUnsignedShort();
	}

	@Override
	public char readChar() throws IOException {
		return decoder.decodeChar();
	}

	@Override
	public int readInt() throws IOException {
		return decoder.decodeInteger();
	}

	@Override
	public long readLong() throws IOException {
		return decoder.decodeLong();
	}

	@Override
	public float readFloat() throws IOException {
		return decoder.decodeFloat();
	}

	@Override
	public double readDouble() throws IOException {
		return decoder.decodeDouble();
	}

	@SuppressWarnings("deprecation")
	@Override
	public String readLine() throws IOException {
		return stream.readLine();
	}

	@Override
	public String readUTF() throws IOException {
		return decoder.decodeString();
	}

	@Override
	public Object readObject() throws ClassNotFoundException, IOException {
		return decoder.decodeValue();
	}

	@Override
	public int read(byte[] b) throws IOException {
		return stream.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return stream.read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		return stream.skip(n);
	}

	@Override
	public int available() throws IOException {
		return stream.available();
	}

	public double[] readDoubleArray() throws IOException{
		return decoder.decodeDoubleArray();
	}

	public float[] readFloatArray() throws IOException{
		return decoder.decodeFloatArray();
	}

	public short[] readShortArray() throws IOException {
		return decoder.decodeShortArray();
	}

	public int[] readIntArray() throws IOException {
		return decoder.decodeIntArray();
	}

	public long[] readLongArray() throws IOException {
		return decoder.decodeLongArray();
	}

	public char[] readCharArray() throws IOException {
		return decoder.decodeCharArray();
	}

	public byte[] readByteArray() throws IOException {
		return decoder.decodeByteArray();
	}

	public <K, V> Map<K, V> readMap(boolean stringKeys) throws IOException {
		return decoder.decodeMap(stringKeys);
	}


	public <K, V> Map<K, V> readMap() throws IOException {
		return readMap(true);
	}

	public Date readDate() throws IOException {
		return decoder.decodeDate();
	}

}
