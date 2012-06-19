package org.facile;

import static org.facile.Facile.*;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.facile.IO.FileObject;
import org.junit.Test;

public class IOTest {

	@Test
	public void test() throws IOException {
		File f = new File(".");
		f = f.getCanonicalFile();
		f = new File(f, "test/org/facile/test.txt");
		FileObject<String> file = open(f);
		
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
