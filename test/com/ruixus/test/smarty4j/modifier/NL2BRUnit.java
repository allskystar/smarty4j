package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class NL2BRUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "Sun or rain expected\ntoday, dark tonight");

		Assert.assertEquals("Sun or rain expected<br />today, dark tonight",
		    getString("{$articleTitle|nl2br}", data));
	}
}
