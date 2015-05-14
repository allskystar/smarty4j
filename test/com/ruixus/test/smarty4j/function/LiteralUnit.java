package com.ruixus.test.smarty4j.function;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class LiteralUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Assert.assertEquals("literal", getString("{literal}a\\nb\n{assign}aa{/literal}"),
		    "a\\nb\n{assign}aa");
		Assert
		    .assertEquals(
		        "literal",
		        getString("{literal}aodun.alert_stop(\"\", function() { location.reload(); });{/literal}11"),
		        "aodun.alert_stop(\"\", function() { location.reload(); });11");
	}
}
