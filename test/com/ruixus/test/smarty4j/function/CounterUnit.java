package com.ruixus.test.smarty4j.function;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.smarty4j.Context;
import com.ruixus.test.smarty4j.BaseUnit;

public class CounterUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Assert
		    .assertEquals(
		        "缺省name及普通参数测试",
		        getString("{counter start=4 skip=2 print=false direction=keep}{counter print=true direction=down}-{counter skip=3 direction=up}-{counter}"),
		        "4-2-5");
		Assert.assertEquals("指定name与assign",
		    getString("{counter name=test}-{counter print=false name=test assign=test}{$test}"), "1-2");
		Assert.assertEquals("direction值测试", getException("{counter name=test direction=test}"),
		    "\"direction\" cannot be resolved to either up, down or keep");
	}

	@Test
	public void advance() throws Exception {
		Context ctx;
		// 变量不使用不生成代码
		ctx = getContext("{counter assign=test}");
		Assert.assertNull(ctx.get("test"));
		// 包含include时回写代码
		ctx = getContext("{counter assign=test}{include file='empty.tpl'}");
		// 包含eval时回写代码
		ctx = getContext("{counter assign=test}{eval var='{\\$test}'}");
		Assert.assertEquals(new String((byte[]) ctx.get("")), "1");
		Assert.assertNotNull(ctx.get("test"));
	}
}
