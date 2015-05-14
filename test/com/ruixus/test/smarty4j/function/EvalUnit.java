package com.ruixus.test.smarty4j.function;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class EvalUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Assert
		    .assertEquals(
		        "eval",
		        getString("{capture name=\"test\"}{ldelim}$smarty.capture.test{rdelim}{/capture}{eval var=$smarty.capture.test}"),
		        "{$smarty.capture.test}");
		Assert
		    .assertEquals(
		        "eval",
		        getString("{capture name=\"test\"}{ldelim}$smarty.capture.test{rdelim}{/capture}{eval var=$smarty.capture.test assign=test}"),
		        "");
		Assert
		    .assertEquals(
		        "eval",
		        getString("{capture name=\"test\"}{ldelim}$smarty.capture.test{rdelim}{/capture}{eval var=$smarty.capture.test assign=test}{$test}"),
		        "{$smarty.capture.test}");
	}
}
