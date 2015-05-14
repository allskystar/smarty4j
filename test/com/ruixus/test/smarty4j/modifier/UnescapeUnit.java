package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class UnescapeUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "Germans use &quot;&Uuml;mlauts&quot; and pay in &euro;uro");

		Assert.assertEquals("Germans use \"&Uuml;mlauts\" and pay in &euro;uro",
		    getString("{$articleTitle|unescape:html}", data));
		Assert.assertEquals("Germans use \"Ümlauts\" and pay in €uro",
		    getString("{$articleTitle|unescape:htmlall}", data));
	}
}
