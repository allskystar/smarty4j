package com.ruixus.test.smarty4j.modifier;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class DateFormatUnit extends BaseUnit {

	@Test
	@SuppressWarnings("deprecation")
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> config = new HashMap<String, Object>();
		config.put("date", "%I:%M %p");
		config.put("time", "%H:%M:%S");
		data.put("config", config);
		data.put("now", new Date(115, 0, 1, 14, 33, 0));
		data.put("yesterday", new Date(114, 11, 31, 14, 33, 0));

		Assert.assertEquals("Jan 1, 2015", getString("{$now|date_format:::US}", data));
		Assert.assertEquals("01/01/15", getString("{$now|date_format:'%D'}", data));
		Assert.assertEquals("02:33 PM", getString("{$now|date_format:$config.date::US}", data));
		Assert.assertEquals("Dec 31, 2014", getString("{$yesterday|date_format:::US}", data));
		Assert.assertEquals("Wednesday, December 31, 2014",
		    getString("{$yesterday|date_format:'%A, %B %e, %Y'::US}", data));
		Assert.assertEquals("14:33:00", getString("{$yesterday|date_format:$config.time}", data));
		Assert.assertEquals("null", getString("{$null|date_format}", data));
		Assert.assertEquals("test", getString("{$null|date_format::test}", data));
		Assert.assertNotEquals("test", getString("{'now'|date_format::test}", data));
		Assert.assertEquals("Sep 10, 2000", getString("{'10/9/2000 0:0:0'|date_format::test:US}", data));
	}
}
