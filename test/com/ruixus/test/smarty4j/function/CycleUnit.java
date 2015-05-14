package com.ruixus.test.smarty4j.function;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.smarty4j.Context;
import com.ruixus.test.smarty4j.BaseUnit;

public class CycleUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("array", "#eeeeee,#d0d0d0".split(","));
		Assert
		    .assertEquals(
		        "缺省name及普通参数测试",
		        getString("{cycle values=\"#eeeeee,#d0d0d0\" print=false advance=false}{cycle print=true}{cycle}{cycle print=false}{cycle}"),
		        "#eeeeee#d0d0d0");
		Assert
		    .assertEquals(
		        "指定name与assign",
		        getString("{cycle values=\"#eeeeee,#d0d0d0\" name=test assign=test}{cycle name=test}{$test}"),
		        "#d0d0d0#eeeeee");
		Assert.assertEquals("使用变量",
		    getString("{cycle values=$array assign=test}{cycle}{$test}", data),
		    "#d0d0d0#eeeeee");
	}

	@Test
	public void advance() throws Exception {
		Context ctx;
		// 变量不使用不生成代码
		ctx = getContext("{cycle values=\"#eeeeee,#d0d0d0\" assign=test}");
		Assert.assertNull(ctx.get("test"));
		// 包含include时回写代码
		ctx = getContext("{cycle values=\"#eeeeee,#d0d0d0\" assign=test}{include file='empty.tpl'}");
		// 包含eval时回写代码
		ctx = getContext("{cycle values=\"#eeeeee,#d0d0d0\" assign=test}{eval var='{\\$test}'}");
		Assert.assertEquals(new String((byte[]) ctx.get("")), "#eeeeee");
		Assert.assertNotNull(ctx.get("test"));
	}
}
