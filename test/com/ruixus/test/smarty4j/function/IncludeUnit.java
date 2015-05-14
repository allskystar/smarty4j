package com.ruixus.test.smarty4j.function;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class IncludeUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Assert.assertEquals("include", getString("{include file='test.tpl'}"), "null");
		Assert.assertEquals("include", getString("{include file='test.tpl' assign='test'}"), "");
		Assert.assertEquals("include", getString("{include file='test.tpl' assign='test'}{$test}"),
		    "null");
		Assert.assertEquals("include", getString("{include file='test.tpl' name=Bob}{$name|default}"), "Bob");
	}
}
