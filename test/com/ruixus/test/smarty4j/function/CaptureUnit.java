package com.ruixus.test.smarty4j.function;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.smarty4j.Context;
import com.ruixus.test.smarty4j.BaseUnit;

public class CaptureUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Assert
		    .assertEquals(
		        "assign绑定",
		        getString("{capture assign=test}commands must be paired{/capture}{$test}. {$smarty.capture.default}"),
		        "commands must be paired. commands must be paired");
		Assert.assertEquals("缺省的name",
		    getString("{capture}commands must be paired{/capture}{$smarty.capture.default}"),
		    "commands must be paired");
		Assert.assertEquals("指定name",
		    getString("{capture name=test}commands must be paired{/capture}{$smarty.capture.test}"),
		    "commands must be paired");
	}

	@Test
	public void advance() throws Exception {
		Context ctx;
		// 变量不使用不生成代码
		ctx = getContext("{capture assign=test}value{/capture}");
		Assert.assertNull(ctx.get("test"));
		// 包含include时回写代码
		ctx = getContext("{capture assign=test}value{/capture}{include file='empty.tpl'}");
		Assert.assertNotNull(ctx.get("test"));
		// 包含eval时回写代码
		ctx = getContext("{capture assign=test}value{/capture}{eval var='{\\$test}'}");
		Assert.assertEquals(new String((byte[]) ctx.get("")), "value");
		Assert.assertNotNull(ctx.get("test"));
	}
}
