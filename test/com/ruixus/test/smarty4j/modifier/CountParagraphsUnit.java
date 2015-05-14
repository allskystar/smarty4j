package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class CountParagraphsUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "War Dims Hope for Peace. Child's Death Ruins Couple's Holiday.\n\nMan is Fatally Slain. Death Causes Loneliness, Feeling of Isolation.");

		Assert.assertEquals("2", getString("{$articleTitle|count_paragraphs}", data));
	}
}
