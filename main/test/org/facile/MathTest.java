package org.facile;

import org.junit.Test;
import static org.facile.Facile.*;
import static org.facile.Math.*;

public class MathTest {
	
	@Test
	public void test() {
		Double m = greduce(min, list(1.1, 2.1, 3.3, 4.4, 5.5));
		print (m);
	}
}
