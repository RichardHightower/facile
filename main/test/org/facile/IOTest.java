package org.facile;

import static org.facile.Facile.*;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.facile.IO.FileObject;
import org.junit.Test;

public class IOTest {

	@Test 
	public void testcwd() throws IOException {
		String cwd = cwd();
		File subdir = subdir("foo");
		
		assertEquals(cwd, subdir.getParentFile().getCanonicalFile().toString());
		
	}
	
	@Test 
	public void findFileTest() throws IOException {
		File subdir = subdir("bin/org/facile");
		List<File> files = files(subdir, ".*\\.txt");
		
		assertEquals(4, len(files));

	}

	@Test
	public void testFileEnum() throws IOException {
		File f = subdir( "test/org/facile/test.txt");
		FileObject<String> file = open(f);

		int index = 0;

		for (String line : file) {
			if (index == 0) {
				assertEquals("abcdefg", line);
			} else if (index == 1) {
				assertEquals("Line 1", line);
			} else if (index == 2) {
				assertEquals("Line 2", line);
			}
			index++;
		}
	}

	@Test
	public void testFileEnumNext() throws IOException {
		File f = new File(".");
		f = f.getCanonicalFile();
		f = new File(f, "test/org/facile/test.txt");
		FileObject<String> file = open(f);

		assertEquals("abcdefg", file.iterator().next());
		assertEquals("Line 1", file.iterator().next());
		assertEquals("Line 2", file.iterator().next());
		assertEquals("Line 3", file.iterator().next());
		assertEquals("Line 4", file.iterator().next());
		assertEquals("Line 5", file.iterator().next());
		assertEquals(null, file.iterator().next());
	}

	@Test
	public void testFile() throws IOException {
		File f = new File(".");
		f = f.getCanonicalFile();
		f = new File(f, "test/org/facile/test.txt");
		FileObject<String> file = open(f);

		testSimpleRead(file);
		testReadLine(file);
		testReadLines(file);
		
		file.close();

	}

	@Test
	public void testInputStream() throws IOException {
		InputStream inputStream = IOTest.class.getResourceAsStream("test.txt");
		FileObject<String> file = open(inputStream);

		testSimpleRead(file);
		testReadLine(file);
		testReadLines(file);
		

		file.close();

	}

	@Test
	public void testClassResource() throws IOException {
		FileObject<String> file = open(IOTest.class, "test.txt");

		testSimpleRead(file);
		testReadLine(file);
		testReadLines(file);
		

		file.close();

	}

	@Test
	public void testStringAndCarriageReturn() throws IOException {
		String lines = lines("abcdefg\r", "Line 1\r", "Line 2\r", "Line 3\r",
				"Line 4\r", "Line 5\r");

		FileObject<String> file = openString(lines);

		testSimpleRead(file);
		testReadLine(file);
		testReadLines(file);
		

		file.close();

	}

	@Test
	public void testString() throws IOException {
		String lines = lines("abcdefg", "Line 1", "Line 2", "Line 3", "Line 4",
				"Line 5");

		FileObject<String> file = openString(lines);

		testSimpleRead(file);
		testReadLine(file);
		testReadLines(file);
		

		file.close();

	}

	private void testSimpleRead(FileObject<String> file) {
		char c = file.read();
		assertEquals('a', c);
	}

	private void testReadLine(FileObject<String> file) {
		assertEquals("bcdefg", file.readLine());
	}

	private void testReadLines(FileObject<String> file) {
		assertEquals("Line 4", file.readLines()[3]);
	}

	//@Test Long test
	public void testURL() throws IOException {
		FileObject<String> file = open(new URL(
				"https://raw.github.com/RichardHightower/facile/master/main/test/org/facile/test.txt"));

		testSimpleRead(file);
		testReadLine(file);
		testReadLines(file);
		file.close();

	}

	@Test
	public void testClassResourceURL() throws IOException {
		URL resource = this.getClass().getResource("test.txt");
		print(resource);
		FileObject<String> file = open(resource);

		testSimpleRead(file);
		testReadLine(file);
		testReadLines(file);
		file.close();

	}

	@Test
	public void testClassResourceURL2() throws IOException {

		FileObject<String> file = open("classpath:/org/facile/test.txt");

		testSimpleRead(file);
		testReadLine(file);
		testReadLines(file);
		file.close();


	}

}
