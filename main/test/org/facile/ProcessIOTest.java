package org.facile;

import static org.facile.Facile.*;

import org.facile.ProcessIO.ProcessOut;
import org.junit.Test;

public class ProcessIOTest {

	@Test
	public void test() {
		expect ("", 0, exec("ls", "/etc/"));
	}
	

	@Test
	public void test2() {
		ProcessOut out = run("ls", "/etc/");
		expect ("", 0, out.exit);
		print (out);
	}

	@Test
	public void test3() {
		expect ("", 0, exec(1, "ls"));
	}
	

	@Test
	public void test4() {
		ProcessOut out = run(1, "ls");
		expect ("", 0, out.exit);
		print (out);
	}

	@Test
	public void test5() {
		ProcessOut out = run(1, "sleep", "10");
		expect ("", 143, out.exit);
		print (out);

	}
	
	@Test
	public void test6() {
		ProcessOut out = run("sleep", "1");
		expect ("", 0, out.exit);
		print (out);

	}

}
