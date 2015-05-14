package com.ruixus.test.smarty4j.function;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class CallFunctionUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Assert
		    .assertEquals(
		        "function",
		        getString("{function name=test def=3}{$def}{/function}{call name=test def=1}-{test}"),
		        "1-3");
		Assert.assertEquals("include", getString("{function name=test}{$def}{assign var=def value=Lily}{/function}{test def=Bob return=def} -> {$def|default}"), "Bob -> Lily");
	}
}
