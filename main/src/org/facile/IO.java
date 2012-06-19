package org.facile;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Flushable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import static org.facile.Facile.*;

public class IO {
	
	enum Mode {
		read_text,
		read_binary,

		readwrite_text,
		readwrite_binary,
		
		write_binary,
		write_text
		
	}
	
	// File
	// File
	public static interface FileObject<T> extends Closeable, Flushable, Iterable<T>{
		String read(int size);
		int read(char [] buffer);
		char read();
		String readLine();
		String[] readLines();
		
		boolean eof();
	
		byte[] input(long size);
		int input(byte [] buffer);
		byte input();
		
		Iterator<T> iterator();
		
		long tell();
		void seek (long pos);
		
		void close();
		void flush();
		
		String readAll();

	}
	
	@SuppressWarnings("serial")
	public static class InputOutputException extends RuntimeException{
		InputOutputException (Throwable t) {
			super (t);
		}
	}
	
	private static void  handle(IOException ex) {
		throw new InputOutputException(ex);
	}
	
	public static class FileTextReader implements FileObject<String> {
		Reader reader;
		char [] buffer = new char[256];
		int bufferSize = 256;
		int position = 0;
		
		public FileTextReader(Reader reader) {
			this.reader = reader;
		}
		
		public void increaseBufferSize() {
			bufferSize = bufferSize * 2;
		}
		

		@Override
		public String read(int size) {
			int count=0;
			try {
				count = reader.read(buffer, 0, (int) size);
			} catch (IOException e) {
				handle(e);
			}
			if (count==-1) {
				return null;
			}
			position+=count;
			return string(0, count, buffer);
		}

		@Override
		public int read(char[] buffer) {
			int len = 0;
			try {
				len = reader.read(buffer);
			} catch (IOException e) {
				handle(e);
			}
			position += len;
			return len;
		}

		boolean eof;
		
		@Override
		public char read() {
			char c = 0;
			try {
				int i = reader.read();
				if (i==-1) {
					eof = true;
					c = 0;
				} else {
					c = (char) i;
				}
			} catch (IOException e) {
				handle(e);
			}
			if (!eof) {
				position++;
			}
			return c;
		}
		
		boolean lastCharCarriageReturn=false;
		
		@Override
		public String readLine() {
			if(eof) {
				return null;
			}
			
			StringBuilder buf = new StringBuilder(bufferSize);
			char ch =  read();
			if(eof) {
				return null;
			}
			
			
			if (ch=='\n' && lastCharCarriageReturn){
				lastCharCarriageReturn = false;
				ch = read();
			}
			
			while (!eof) {
				
				if (ch!='\r' && ch !='\n') {
					buf.append(ch);
				} else {
					break;
				}
				ch = read();
			}
			
			if (ch=='\r') {
				lastCharCarriageReturn = true;
			}
			return buf.toString();
				
		}

		@Override
		public String[] readLines() {
			String all = readAll();
			return toLines(all);
		}

		@Override
		public byte[] input(long size) {
			notSupported();
			return null;
		}

		@Override
		public int input(byte[] buffer) {
			notSupported();
			return 0;
		}

		@Override
		public byte input() {
			notSupported();
			return 0;
		}

		@Override
		public Iterator<String> iterator() {
			return new Iterator<String>() {

				String readLine;
				
				@Override
				public boolean hasNext() {
					readLine = readLine();
					if (readLine==null) {
						return false;
					} else {
						return true;
					}
				}

				@Override
				public String next() {
					return readLine;
				}

				@Override
				public void remove() {
					notSupported();
				}
			};
		}

		@Override
		public long tell() {
			return position;
		}

		@Override
		public void seek(long pos) {
			try {
				reader.skip(pos);
			} catch (IOException e) {
				handle(e);
			}
		}

		@Override
		public void close() {
			try {
				reader.close();
			} catch(IOException e) {
				handle(e);
			}
		}

		@Override
		public void flush() {
		}

		@Override
		public String readAll() {
			try {
				final int size = this.bufferSize*10;
				final char buffer [] = new char[size];
				final StringBuilder buf = new StringBuilder(size);
				while (true) {
					try {
						int count = reader.read(buffer);
						buf.append(buffer);
						
						if (count < size) {
							break;
						}
					} catch (IOException e) {
						handle(e);
					}
			}
			return buf.toString();
			} finally {
				close();
			}
		}

		@Override
		public boolean eof() {
			return eof;
		}
		
	}

	public static FileObject <String> open(File file) {
		FileReader fis = null;
		try {
			fis = new FileReader(file);
		} catch (FileNotFoundException e) {
			handle(e);
		}
		FileTextReader textReader = new FileTextReader(fis);
		return textReader;
	}
	
	public static String[] readLines(File file) {
		return open(file).readLines();
	}

	public static String[] readAll(File file) {
		return open(file).readLines();
	}
	
	public static String[] readLinesFromFile(String file) {
		return open(new File(file)).readLines();
	}

	public static String[] readLinesAllFromFile(String file) {
		return open(new File(file)).readLines();
	}


}
