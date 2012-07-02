package org.facile;

import static org.facile.Facile.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

public class FacileTest {
	Logger logger = Logger.getLogger(FacileTest.class.getName());
	ByteArrayOutputStream err;
	ByteArrayOutputStream out;
	StringBuilder builder;

	private void setupStreams() {
		err = new ByteArrayOutputStream();
		out = new ByteArrayOutputStream();
		builder = new StringBuilder();

		PrintStream ps = new PrintStream(err);
		System.setErr(ps);
		ps = new PrintStream(out);
		System.setOut(ps);
		logger.setLevel(Level.FINEST);
		
	}


	@Test
	public void test() {
		setupStreams();
		print("Hi", "Mom");
		assertEquals("Hi Mom \n", out.toString());

		setupStreams();
		fprint(logger, "Hi", "Mom");
		assertEquals(true, err.toString().contains("INFO: Hi Mom"));
		

		
		setupStreams();
		fprint(builder, "Hi", "Mom");
		assertEquals("Hi Mom \n", builder.toString());
		
		setupStreams();
		debugPrint("Hi", "Mom");
		assertEquals("Hi Mom \n", err.toString());
		
		setupStreams();
		printf("Hi %s \n", "Mom");
		assertEquals("Hi Mom \n", out.toString());

		setupStreams();
		println("Hi Mom ");
		assertEquals("Hi Mom \n", out.toString());
		
		setupStreams();
		fprintf(logger, "Hi %s \n", "Mom");
		fprintf(builder, "Hi %s \n", "Mom");
		assertEquals("Hi Mom \n", builder.toString());

		setupStreams();
		fprintf((Appendable) builder, "Hi %s \n", "Mom");
		assertEquals("Hi Mom \n", builder.toString());

		setupStreams();
		fprintln(System.err, "Hi Mom ");
		assertEquals("Hi Mom \n", err.toString());

		setupStreams();
		fprintln(builder, "Hi Mom ");
		assertEquals("Hi Mom \n", builder.toString());
		
		setupStreams();
		fprintln((Appendable)builder, "Hi Mom ");
		assertEquals("Hi Mom \n", builder.toString());


	}

}
