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
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;

import org.facile.ContextParser.Ctx;

import static org.facile.Facile.*;

public class IO {


	public static class IOFile extends File {
		private static final long serialVersionUID = 1L;

		public IOFile(File parent, String child) {
			super(parent, child);
		}

		public IOFile(String parent, String child) {
			super(parent, child);
		}

		public IOFile(String pathname) {
			super(pathname);
		}

		public IOFile(URI uri) {
			super(uri);
		}

		
	}
	
	public static File file (String file) {
		return new IOFile(file);
	}
	public static File file (File f, String file) {
		return new IOFile(f, file);
	}
	
	public static String cwd () {
		File f = new File(".");
		try {
			return f.getCanonicalFile().toString();
		} catch (IOException e) {
			throw new InputOutputException(e);
		}
	}
	
	public static File cwf () {
		IOFile f = new IOFile(".");
		try {
			return f.getCanonicalFile();
		} catch (IOException e) {
			throw new InputOutputException(e);
		}
	}

	public static File subdir (File dir, String str) {
		if (!dir.isDirectory()) {
			dir = dir.getParentFile();
		}
		return file(dir, str);
	}

	public static File subdir (String str) {
		return subdir(cwf(), str);
	}
	
	public static List<File> subdirs() {
		return subdirs(cwf());
	}

	public static List<File> allSubDirs(File dir) {
		return doFilesSearch(dir, true, false, true, null);
	}
	public static List<File> filesWithExtension(File dir, String extension) {
		return doFilesSearch(dir, true, true, false, ".*\\."+extension);
	}
	
	public static List<File> filesWithExtension(String extension) {
		return filesWithExtension(cwf(), extension);
	}

	public static List<File> subdirs(File dir) {
		List<File> files = list(dir.listFiles());
		ListIterator<File> listIterator = files.listIterator();
		while(listIterator.hasNext()) {
			File next = listIterator.next();
			if (!next.isDirectory()) {
				listIterator.remove();
			}
		}
		return files;
	}
	
	public static List<File> files(final String regex) {
		return files(cwf(), regex);
	}
	
	private static List<File> doFilesSearch(File dir, final boolean recursive, boolean filesOnly, boolean dirsOnly, final String regex) {
		if (!dir.isDirectory()) {
			dir = dir.getParentFile();
		}
		final List<File> outList = ls(File.class);

		
		File[] listFiles = dir.listFiles();
		for (File file : listFiles) {
			if (file.isFile()) {
				List<String> list = ls(file.getAbsolutePath(), file.getPath());
				for (String input : list) {
					if (regex!=null) {
						Matcher matcher = re(regex, input);
						if (matcher.matches()) {
							if (!dirsOnly){
								outList.add(file);
							}
							break;
						}
					}
				}
			} else if (file.isDirectory()){
				if (!filesOnly) {
					outList.add(file);
				}
				if (recursive) {
					List<File> files = doFilesSearch(file, recursive, filesOnly, false, regex);
					outList.addAll(files);
				}
			}
		}
		
		return outList;

	}
	public static List<File> files(File dir, final String regex) {
		return doFilesSearch(dir, true, true, false, regex);
	}


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

		void println(String password);

		void autoFlush();

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

		@Override
		public void println(String password) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void autoFlush() {
			// TODO Auto-generated method stub
			
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
		Ctx ctx;
		
		public void ctx(Ctx ctx) {
			this.ctx = ctx;
		}
		
		public Ctx ctx() {
			return ctx;
		}


		public FileTextReader() {
			init(null);
		}

		public FileTextReader(Reader reader) {
			init(reader);
		}

		private void init(Reader reader) {
			this.reader = reader;
			ctx = new Ctx();

		}

		public void increaseBufferSize() {
			bufferSize = bufferSize * 2;
		}

		@Override
		public String read(int size) {
			String string = doRead(reader, size);
			ctx.$_ = string;
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
		private Iterator<String> iterator;

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

			String line = buf.toString();
			ctx.$_ = line;
			return line;

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
				this.eof = true;
				close();
			}
		}


		public void startIteration() {
			iterator = this.iterator();
		}
		@Override
		public boolean eof() {
			if (iterator==null) {
				return eof;
			} else {
				iterator.hasNext();
				return eof;
			}
		}

		@Override
		public void println(String password) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void autoFlush() {
			// TODO Auto-generated method stub
			
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
	public static FileObject<?> open(OutputStream outputStream) {
		return null;
	}

}
