package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class RegexReplaceUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "Infertility unlikely to\nbe passed on, experts say.");

		Assert.assertEquals("Infertility unlikely to be passed on, experts say.",
		    getString("{$articleTitle|regex_replace:'[\\r\\t\\n]':' '}", data));
	}
}
