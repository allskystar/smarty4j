package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class CountCharactersUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "Cold Wave Linked to Temperatures.");

		Assert.assertEquals("29", getString("{$articleTitle|count_characters}", data));
		Assert.assertEquals("33", getString("{$articleTitle|count_characters:true}", data));
	}
}
