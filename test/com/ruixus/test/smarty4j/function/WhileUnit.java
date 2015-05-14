package com.ruixus.test.smarty4j.function;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class WhileUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Assert.assertEquals("while",
		    getString("{while $int<5}{assign var='int' value=10}{$int}{/while}"), "10");
	}
}
