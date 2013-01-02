package org.facile;

import java.util.Map;

import org.junit.Test;
import static org.facile.Facile.*;
import static org.junit.Assert.*;

public class MapTest {

	@SuppressWarnings("unused")
	public static class Employee {
		private String name = "Rick";
		private int age = 100;
		private Employee boss;
		int [] nums = {1,2,3,4};
		Integer [] nums2 = {1,2,3,4};

	}
	
	@Test
	public void testBasicMap() {
		Map<String, Object> map = Reflection.toMap(new Employee());
		System.out.println(map);
		int age = get(integer, map, "age");
		String name = get(string, map, "name");
		assertEquals(100, age);
		assertEquals("Rick", name);
		
		
	}
	
	

	@Test
	public void testBasicNestedMap() {
		Employee rick = new Employee();
		rick.boss = new Employee();
		rick.boss.name="Diana";
		rick.boss.age=25;
		Map<String, Object> map = Reflection.toMap(rick);
		System.out.println(map);
		int age = get(integer, map, "age");
		String name = get(string, map, "name");
		assertEquals(100, age);
		assertEquals("Rick", name);
		
		
	}
}
