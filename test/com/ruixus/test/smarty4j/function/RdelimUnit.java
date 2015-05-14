package com.ruixus.test.smarty4j.function;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class RdelimUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Assert.assertEquals("基本功能测试", getString("{rdelim}"), "}");
		engine.setRightDelimiter("}//");
		Assert.assertEquals("基本功能测试", getString("{rdelim}//"), "}//");
		engine.setRightDelimiter("}");
	}
}
