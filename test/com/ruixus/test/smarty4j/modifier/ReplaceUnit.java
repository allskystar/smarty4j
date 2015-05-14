package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class ReplaceUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "Child's Stool Great for Use in Garden.");

		Assert.assertEquals("Child's Stool Great for Use in Vineyard.",
		    getString("{$articleTitle|replace:'Garden':'Vineyard'}", data));
		Assert.assertEquals("Child's   Stool   Great   for   Use   in   Garden.",
		    getString("{$articleTitle|replace:' ':'   '}", data));
	}
}
