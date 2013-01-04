package org.facile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import static org.facile.Facile.*;
import static org.junit.Assert.*;

public class MapTest {

	public static class Employee {
		private String name = "Rick";
		private int age = 100;
		private Employee boss;
		private List<Employee> emps;

		int [] nums = {1,2,3,4};
		Integer [] nums2 = {1,2,3,4};
		List <Integer> nums3 = new ArrayList<>();
		{
			for (Integer num : nums2) {
				nums3.add(num);
			}
		}

	}
	
	@Test
	public void testBasicMap() {
		Employee employee = new Employee();
		employee.nums3.add(99);
		Map<String, Object> map = Reflection.toMap(employee);
		System.out.println(map);
		int age = get(integer, map, "age");
		String name = get(string, map, "name");
		assertEquals(100, age);
		assertEquals("Rick", name);
		@SuppressWarnings("unchecked")
		List<Integer> nums3 = get(List.class, map, "nums3");
		assertNotNull(nums3);
		assertEquals(5, nums3.size());
		assertEquals(99, nums3.get(4).intValue());
	}
	
	

	@Test
	public void testBasicNestedMap() {
		Employee rick = new Employee();
		rick.boss = new Employee();
		rick.boss.name="Diana";
		rick.boss.age=25;
		rick.emps = new ArrayList<>();
		
		Employee emp1 = new Employee();
		emp1.name="Tommy";
		emp1.age=77;
		rick.emps.add(emp1);
		
		Map<String, Object> map = Reflection.toMap(rick);
		System.out.println(map);
		int age = get(integer, map, "age");
		String name = get(string, map, "name");
		assertEquals(100, age);
		assertEquals("Rick", name);
		assertEquals("Tommy", rick.emps.get(0).name);
		assertEquals(77, rick.emps.get(0).age);
		
		
	}
}
