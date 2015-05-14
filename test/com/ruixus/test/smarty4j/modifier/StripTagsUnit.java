package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class StripTagsUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(
		    "articleTitle",
		    "Blind Woman Gets <font face=\"helvetica\">New Kidney</font> from Dad she Hasn't Seen in <b>years</b>.");

		Assert.assertEquals("Blind Woman Gets  New Kidney  from Dad she Hasn't Seen in  years .",
		    getString("{$articleTitle|strip_tags}", data));
		Assert.assertEquals("Blind Woman Gets New Kidney from Dad she Hasn't Seen in years.",
		    getString("{$articleTitle|strip_tags:false}", data));
	}
}
