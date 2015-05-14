package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class UpperUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "If Strike isn't Settled Quickly it may Last a While.");

		Assert.assertEquals("IF STRIKE ISN'T SETTLED QUICKLY IT MAY LAST A WHILE.",
		    getString("{$articleTitle|upper}", data));
	}
}
