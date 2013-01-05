package org.facile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import static org.facile.Facile.*;
import static org.junit.Assert.*;

public class MapTest {

	public static class Person  {
		@SuppressWarnings("unused")
		private String name = "Human";

	}
	
	public static class Employee extends Person {
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
		@Override
		public String toString() {
			return "Employee [name=" + name + ", age=" + age + ", boss=" + boss
					+ ", emps=" + emps + ", nums=" + Arrays.toString(nums)
					+ ", nums2=" + Arrays.toString(nums2) + ", nums3=" + nums3
					+ "]";
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


	@Test
	public void testBasicNestedMapUsingCoerce() {
		Employee rick = new Employee();
		rick.boss = new Employee();
		rick.boss.name="Diana";
		rick.boss.age=25;
		rick.emps = new ArrayList<>();
		
		Employee emp1 = new Employee();
		emp1.name="Tommy";
		emp1.age=77;
		rick.emps.add(emp1);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> map = coerce(Map.class, rick);
		System.out.println(map);
		int age = get(integer, map, "age");
		String name = get(string, map, "name");
		assertEquals(100, age);
		assertEquals("Rick", name);
		assertEquals("Tommy", rick.emps.get(0).name);
		assertEquals(77, rick.emps.get(0).age);
		
		
	}

	
	@Test
	public void testMapToObject() {
		
		Map<String, Object> mp = mp(
				"name", "Sam", 
				"nums", new int[]{9,10,11},
				"nums3", ilist(1, 2, 3, 4, 5),
				"nums2", new Integer[]{12,13,14},
				"class", "org.facile.MapTest$Employee",
				"age", 26,
				"boss", new Employee(),
				"emps", ls(new Employee(), new Employee(), new Employee())
				);
		Employee employee = fromMap(mp, Employee.class);
		
		System.out.println(employee);
		assertEquals("Sam", employee.name);
		assertEquals(3, employee.nums.length);
		assertEquals(9, employee.nums[0]);
		assertEquals(11, employee.nums[2]);
		assertEquals(13, employee.nums2[1].intValue());
		assertEquals(26, employee.age);
		assertEquals("Rick", employee.boss.name);
		assertEquals("Rick", employee.emps.get(0).name);
		assertEquals(5, employee.nums3.size());
		assertEquals(5, idx(employee.nums3, 4).intValue());

		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testMapNestedMapToObject() {
		
		Map<String, Object> mp = mp(
				"name", "Sam", 
				"nums", new int[]{9,10,11},
				"nums3", ilist(1, 2, 3, 4, 5),
				"nums2", new Integer[]{12,13,14},
				"class", "org.facile.MapTest$Employee",
				"age", 26,
				"boss", Reflection.toMap(new Employee()),
				"emps", ls(Reflection.toMap(new Employee()), Reflection.toMap(new Employee()), Reflection.toMap(new Employee()))
				);
		Employee employee = fromMap(mp, Employee.class);
		
		System.out.println(employee);
		assertEquals("Sam", employee.name);
		assertEquals(3, employee.nums.length);
		assertEquals(9, employee.nums[0]);
		assertEquals(11, employee.nums[2]);
		assertEquals(13, employee.nums2[1].intValue());
		assertEquals(26, employee.age);
		assertEquals("Rick", employee.boss.name);
		assertEquals("Rick", employee.emps.get(0).name);
		assertEquals(5, employee.nums3.size());
		assertEquals(5, idx(employee.nums3, 4).intValue());

		
	}


	@SuppressWarnings("unchecked")
	@Test
	public void testMapNestedMapToObjectUsingCorece() {
		
		Map<String, Object> mp = mp(
				"name", "Sam", 
				"nums", new int[]{9,10,11},
				"nums3", ilist(1, 2, 3, 4, 5),
				"nums2", new Integer[]{12,13,14},
				"class", "org.facile.MapTest$Employee",
				"age", 26,
				"boss", Reflection.toMap(new Employee()),
				"emps", ls(Reflection.toMap(new Employee()), Reflection.toMap(new Employee()), Reflection.toMap(new Employee()))
				);
		Employee employee = coerce(Employee.class, mp);
		
		System.out.println(employee);
		assertEquals("Sam", employee.name);
		assertEquals(3, employee.nums.length);
		assertEquals(9, employee.nums[0]);
		assertEquals(11, employee.nums[2]);
		assertEquals(13, employee.nums2[1].intValue());
		assertEquals(26, employee.age);
		assertEquals("Rick", employee.boss.name);
		assertEquals("Rick", employee.emps.get(0).name);
		assertEquals(5, employee.nums3.size());
		assertEquals(5, idx(employee.nums3, 4).intValue());

		
	}
	
	
	@Test
	public void testMapToObjectDifferentTypes() {
		
		Map<String, Object> mp = mp(
				"name", "Sam", 
				"nums", ilist(9,10,11),
				"nums3", new int[] {1, 2, 3, 4, 5},
				"nums2", new Integer[]{12,13,14},
				"class", "org.facile.MapTest$Employee",
				"age", (short)26,
				"boss", new Employee(),
				"emps", ls(new Employee(), new Employee(), new Employee())
				);
		Employee employee = fromMap(mp, Employee.class);
		
		System.out.println(employee);
		assertEquals("Sam", employee.name);
		assertEquals(3, employee.nums.length);
		assertEquals(9, employee.nums[0]);
		assertEquals(11, employee.nums[2]);
		assertEquals(13, employee.nums2[1].intValue());
		assertEquals(26, employee.age);
		assertEquals("Rick", employee.boss.name);
		assertEquals("Rick", employee.emps.get(0).name);
		//assertEquals(5, employee.nums3.size());
		//assertEquals(5, idx(employee.nums3, 4).intValue());

		
	}


}
