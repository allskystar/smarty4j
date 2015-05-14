package com.ruixus.test.smarty4j.function;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class ConfigLoadUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Assert.assertEquals(getString("{config_load file='test.cfg'}{$smarty.config.foo}{#foo#}"),
		    "123123");
	}
}
