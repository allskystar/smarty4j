package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class StripUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "Two Sisters Reunite after Eighteen Years at Checkout Counter.");

		Assert.assertEquals("Two Sisters Reunite after Eighteen Years at Checkout Counter.",
		    getString("{$articleTitle|truncate}", data));
		Assert.assertEquals("Two Sisters Reunite after...",
		    getString("{$articleTitle|truncate:30}", data));
		Assert.assertEquals("Two Sisters Reunite after",
		    getString("{$articleTitle|truncate:30:''}", data));
		Assert.assertEquals("Two Sisters Reunite after---",
		    getString("{$articleTitle|truncate:30:'---'}", data));
		Assert.assertEquals("Two Sisters Reunite after Eigh",
		    getString("{$articleTitle|truncate:30:'':true}", data));
		Assert.assertEquals("Two Sisters Reunite after E...",
		    getString("{$articleTitle|truncate:30:'...':true}", data));
		Assert.assertEquals("Two Sisters Re..ckout Counter.",
		    getString("{$articleTitle|truncate:30:'..':true:true}", data));
	}
}
