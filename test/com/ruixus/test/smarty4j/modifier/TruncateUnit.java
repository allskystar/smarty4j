package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class TruncateUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "Grandmother of\neight makes\t    hole in one.");

		Assert.assertEquals("Grandmother of eight makes hole in one.",
		    getString("{$articleTitle|strip}", data));
		Assert.assertEquals("Grandmother&nbsp;of&nbsp;eight&nbsp;makes&nbsp;hole&nbsp;in&nbsp;one.",
		    getString("{$articleTitle|strip:'&nbsp;'}", data));
	}
}
