package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class DefaultUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "Dealers Will Hear Car Talk at Noon.");
		data.put("email", "");

		Assert.assertEquals("Dealers Will Hear Car Talk at Noon.", getString("{$articleTitle|default:'no title'}", data));
		Assert.assertEquals("no title", getString("{$myTitle|default:'no title'}", data));
		Assert.assertEquals("No email address available", getString("{$email|default:'No email address available'}", data));
	}
}
