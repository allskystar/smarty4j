package com.ruixus.test.smarty4j.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

/**
 * unsupport list:
 */
public class VariableUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("A1", "Rat");
		map.put("A2", "Ox");
		map.put("A3", "Tiger");
		map.put("A4", "Hare");
		map.put("A5", "Dragon");
		map.put("A6", "Serpent");
		map.put("A7", "Horse");
		map.put("A8", "Sheep");
		map.put("A9", "Monkey");
		map.put("A10", "Rooster");
		map.put("A11", "Dog");
		map.put("A12", "Boar");

		List<String> list = new ArrayList<String>();
		list.add("Rat");
		list.add("Ox");
		list.add("Tiger");
		list.add("Hare");
		list.add("Dragon");
		list.add("Serpent");
		list.add("Horse");
		list.add("Sheep");
		list.add("Monkey");
		list.add("Rooster");
		list.add("Dog");
		list.add("Boar");

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("foo", "Foo");
		data.put("bar", "A2");
		data.put("x", 1);
		data.put("y", 2);
		data.put("array",
		    "Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar".split(","));
		data.put("map", map);
		data.put("list", list);

		Assert.assertEquals("Foo", getString("{$foo}", data));
		Assert.assertEquals("Dragon", getString("{$list[`$x+3.0`]}", data));
		Assert.assertEquals("Hare", getString("{$list[`3.0`]}", data));
		Assert.assertEquals("Dragon", getString("{$list[4]}", data));
		Assert.assertEquals("Dragon", getString("{$array[4]}", data));
		Assert.assertEquals("Rat", getString("{$map.A1}", data));
		Assert.assertEquals("Ox", getString("{$map.$bar}", data));
		Assert.assertEquals("foo", getString("{\"foo\"}", data));
		Assert.assertEquals("3", getString("{$x+$y}", data));
		Assert.assertEquals("Dragon", getString("{$list[$x+3]}", data));
		Assert.assertEquals("--3.0", getString("{$x=-3.0}-{$x}", data));
		Assert.assertEquals("-3", getString("{$x=3}-{$x}", data));
		Assert.assertEquals("-3", getString("{$map.x=3}-{$map.x}", data));
		Assert.assertEquals("-3", getString("{$list[0]=3}-{$list[0]}", data));
	}
}
