package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class CatUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "Psychics predict world didn't end");

		Assert
		    .assertEquals("Psychics predict world didn't end", getString("{$articleTitle|cat}", data));
		Assert.assertEquals("Psychics predict world didn't end",
		    getString("{$articleTitle|cat:$null}", data));
		Assert.assertEquals("Psychics predict world didn't end yesterday.",
		    getString("{$articleTitle|cat:' yesterday.'}", data));
	}
}
