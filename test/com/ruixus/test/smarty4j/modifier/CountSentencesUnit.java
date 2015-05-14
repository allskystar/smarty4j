package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class CountSentencesUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "Two Soviet Ships Collide - One Dies. Enraged Cow Injures Farmer with Axe.");

		Assert.assertEquals("2", getString("{$articleTitle|count_sentences}", data));
	}
}
