package com.ruixus.test.smarty4j.function;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.smarty4j.Context;
import com.ruixus.test.smarty4j.BaseUnit;

public class MathUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Assert.assertEquals("math", getString("{math equation=\"x\" x='3' y=1}"), "3");
		Assert.assertEquals("math", getString("{math equation=\"(x+(x+y * y))*x\" x='3' y=1}"), "21");
		Assert.assertEquals("math", getException("{math equation=\"(x+(x+y * y))*x\" y=1}"),
		    "\"x\" not found");
	}

	@Test
	public void advance() throws Exception {
		Context ctx;
		// 变量不使用不生成代码
		ctx = getContext("{math equation=\"(x+(x+y * y))*x\" x='3' y=1 assign=test}");
		Assert.assertNull(ctx.get("test"));
		// 包含include时回写代码
		ctx = getContext("{math equation=\"(x+(x+y * y))*x\" x='3' y=1 assign=test}{include file='empty.tpl'}");
		// 包含eval时回写代码
		ctx = getContext("{math equation=\"(x+(x+y * y))*x\" x='3' y=1 assign=test}{eval var='{\\$test}'}");
		Assert.assertEquals(new String((byte[]) ctx.get("")), "21");
		Assert.assertNotNull(ctx.get("test"));
	}
}
