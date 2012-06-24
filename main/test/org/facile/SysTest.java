package org.facile;

import static org.facile.Facile.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class SysTest {

	@Test
	public void test() {
		List<File> path = Sys.path();
		
		for (File p : path) {
			print (p);
		}
	}
}
