package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class IndentUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(
		    "articleTitle",
		    "NJ judge to rule on nude beach.\nSun or rain expected today, dark tonight.\nStatistics show that teen pregnancy drops off significantly after 25.");

		Assert.assertEquals("    NJ judge to rule on nude beach.\n    Sun or rain expected today, dark tonight.\n    Statistics show that teen pregnancy drops off significantly after 25.",
		    getString("{$articleTitle|indent}", data));
		Assert.assertEquals("          NJ judge to rule on nude beach.\n          Sun or rain expected today, dark tonight.\n          Statistics show that teen pregnancy drops off significantly after 25.",
		    getString("{$articleTitle|indent:10}", data));
		Assert
		    .assertEquals(
		        "\tNJ judge to rule on nude beach.\n\tSun or rain expected today, dark tonight.\n\tStatistics show that teen pregnancy drops off significantly after 25.",
		        getString("{$articleTitle|indent:1:'\t'}", data));
	}
}
