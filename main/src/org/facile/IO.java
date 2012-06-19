package org.facile;

import java.io.CharArrayReader;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;

import static org.facile.Facile.*;

public class IO {

	enum Mode {
		read_text, read_binary,

		readwrite_text, readwrite_binary,

		write_binary, write_text

	}

	// File
	// File
	public static interface FileObject<T> extends Closeable, Flushable,
			Iterable<T> {
		String read(int size);

		int read(char[] buffer);

		char read();

		String readLine();

		String[] readLines();

		boolean eof();

		byte[] input(long size);

		int input(byte[] buffer);

		byte input();

		Iterator<T> iterator();

		long tell();

		void seek(long pos);

		void close();

		void flush();

		String readAll();

	}

	@SuppressWarnings("serial")
	public static class InputOutputException extends RuntimeException {
		InputOutputException(Throwable t) {
			super(t);
		}
	}

	private static void handle(IOException ex) {
		throw new InputOutputException(ex);
	}

	public static class FileBinary implements FileObject<String> {
		
		private InputStream stream;
		private DataInputStream dataInput;
		private Reader reader;
		private boolean eof;
		


		public FileBinary() {
		}

		public FileBinary(InputStream stream) {
			init(stream);
		}

		private void init(InputStream stream) {
			this.stream = stream;
			this.dataInput = new DataInputStream(this.stream);
			this.reader = new InputStreamReader(dataInput);
		}

		@Override
		public String read(int size) {
			return doRead(reader, size);
		}

		@Override
		public int read(char[] buffer) {
			notSupported();
			return 0;
		}

		@Override
		public char read() {
			try {
				return dataInput.readChar();
			} catch (IOException e) {
				handle(e);
			}
			return 0;
		}

		@Override
		public String readLine() {
//			try {
//				return reader.read();
//			} catch (IOException e) {
//				handle(e);
//			}
//			return "";
			return null;
		}

		@Override
		public String[] readLines() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean eof() {
			// TODO Auto-generated method stub
			return eof;
		}

		@Override
		public byte[] input(long size) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int input(byte[] buffer) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public byte input() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Iterator<String> iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long tell() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void seek(long pos) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void close() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void flush() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String readAll() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private static String doRead(Reader reader, int size) {
		int count = 0;
		char[] buffer = new char[size];

		try {
			count = reader.read(buffer, 0, (int) size);
		} catch (IOException e) {
			handle(e);
		}
		if (count == -1) {
			return null;
		}
		return string(0, count, buffer);
	}

	public static class FileTextReader implements FileObject<String> {
		Reader reader;
		int bufferSize = 256;
		int position = 0;

		public FileTextReader() {
		}

		public FileTextReader(Reader reader) {
			init(reader);
		}

		private void init(Reader reader) {
			this.reader = reader;
		}

		public void increaseBufferSize() {
			bufferSize = bufferSize * 2;
		}

		@Override
		public String read(int size) {
			String string = doRead(reader, size);
			position += string.length();
			return string;
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
				if (i == -1) {
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

		@Override
		public String readLine() {
			StringBuilder buf = new StringBuilder(bufferSize);
			char ch = read();
			if (eof) {
				return null;
			}

			while (!eof) {

				if (ch == '\r') {
					if (this.reader.markSupported()) {
						try {
							reader.mark(4);
							ch = read();
							if (ch != '\n') {
								reader.reset();
							}
						} catch (IOException e) {
							handle(e);
						}
					} else {
						ch = read();
						if (ch != '\n') {
							continue;
						}
					}
					break;
				} else if (ch == '\n') {
					break;
				} else {
					buf.append(ch);
				}
				ch = read();
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
				int has = 0;
				int next = 0;

				@Override
				public boolean hasNext() {
					readLine = readLine();
					has++;
					if (readLine == null) {
						close();
						return false;
					} else {
						return true;
					}
					
				}

				@Override
				public String next() {
					next++;
					if ( has < next ) {
						hasNext();
					}
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
			} catch (IOException e) {
				handle(e);
			}
		}

		@Override
		public void flush() {
		}

		@Override
		public String readAll() {
			try {
				final int size = this.bufferSize * 10;
				final char buffer[] = new char[size];
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

	public static FileObject<String> open(File file) {
		FileReader reader = null;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			handle(e);
		}
		FileTextReader textReader = new FileTextReader(reader);
		return textReader;
	}

	public static FileObject<String> openFile(String str) {
		return open(new File(str));
	}

	public static FileObject<String> openString(String str) {
		StringReader reader = new StringReader(str);
		FileTextReader textReader = new FileTextReader(reader);
		return textReader;
	}

	public static FileObject<String> open(char buffer[]) {
		CharArrayReader reader = new CharArrayReader(buffer);
		FileTextReader textReader = new FileTextReader(reader);
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

	public static FileObject<String> open(InputStream inputStream) {
		FileTextReader textReader = new FileTextReader(new InputStreamReader(
				inputStream));
		return textReader;
	}

	public static FileObject<String> open(URL url) {

		FileTextReader textReader = null;
		try {
			textReader = new FileTextReader(new InputStreamReader(
					url.openStream()));
		} catch (IOException e) {
			handle(e);
		}
		return textReader;
	}

	public static FileObject<String> open(Class<?> clz, String resource) {
		FileTextReader textReader = new FileTextReader(new InputStreamReader(
				clz.getResourceAsStream(resource)));
		return textReader;
	}

	public static FileObject<String> open(String suri) {
		URI uri = URI.create(suri);
		String scheme = uri.getScheme();
		if (scheme == null) {
			try {
				File file = new File(suri);
				if (file.exists() || file.getParentFile().exists()) {
					return open(file);
				}
			} catch (Exception ex) {
				handle(new IOException(ex));
				return null;
			}
		} else if (scheme.equals("classpath")) {
			ClassLoader contextClassLoader = Thread.currentThread()
					.getContextClassLoader();
			
			URL resource = contextClassLoader.getResource(uri.getPath());
			
			if (resource == null) {
				resource = ClassLoader.getSystemResource(uri.getPath());
			}

			if (resource == null) {
				resource = IO.class.getResource(uri.getPath());
			}
			return open(resource);

		} else {
			try {
				return open(uri.toURL());
			} catch (MalformedURLException e) {
				handle(e);
				return null;
			}
		}

		return null;

	}

}
