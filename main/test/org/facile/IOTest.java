package org.facile;

import static org.facile.Facile.*;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.facile.IO.FileObject;
import org.junit.Test;

public class IOTest {

	@Test
	public void testFile() throws IOException {
		File f = new File(".");
		f = f.getCanonicalFile();
		f = new File(f, "test/org/facile/test.txt");
		FileObject<String> file = open(f);
		
		testSimpleRead(file);
		testReadLine(file);		
		testReadLines(file);				

	}
	
	@Test
	public void testInputStream() throws IOException {
		InputStream inputStream = IOTest.class.getResourceAsStream("test.txt");
		FileObject<String> file = open(inputStream);
		
		testSimpleRead(file);
		testReadLine(file);		
		testReadLines(file);				

	}
	
	@Test
	public void testClassResource() throws IOException {
		FileObject<String> file = open(IOTest.class, "test.txt");
		
		testSimpleRead(file);
		testReadLine(file);		
		testReadLines(file);				

	}


	@Test
	public void testStringAndCarriageReturn() throws IOException {
		String lines = lines("abcdefg\r",
							"Line 1\r",
							"Line 2\r",
							"Line 3\r",
							"Line 4\r",
							"Line 5\r");
		
		FileObject<String> file = openString(lines);
		
		testSimpleRead(file);
		testReadLine(file);		
		testReadLines(file);				

	}

	@Test
	public void testString() throws IOException {
		String lines = lines("abcdefg",
							"Line 1",
							"Line 2",
							"Line 3",
							"Line 4",
							"Line 5");
		
		FileObject<String> file = openString(lines);
		
		testSimpleRead(file);
		testReadLine(file);		
		testReadLines(file);				

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

	


}
