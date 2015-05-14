package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class CapitalizeUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "next x-men film, x3, delaYed.");

		Assert.assertEquals("Next X-Men Film, x3, DelaYed.",
		    getString("{$articleTitle|capitalize}", data));
		Assert.assertEquals("Next X-Men Film, X3, DelaYed.",
		    getString("{$articleTitle|capitalize:true}", data));
		Assert.assertEquals("Next X-Men Film, X3, Delayed.",
		    getString("{$articleTitle|capitalize:true:true}", data));
	}
}
