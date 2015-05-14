package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class LowerUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "Two Convicts Evade Noose, Jury Hung.");

		Assert.assertEquals("two convicts evade noose, jury hung.",
		    getString("{$articleTitle|lower}", data));
	}
}
