package com.ruixus.test.smarty4j.function;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.smarty4j.Context;
import com.ruixus.test.smarty4j.BaseUnit;

public class AssignUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name", "Bob");
		data.put("number", "1");
		data.put("names", "Ouyang Xianwei--Wuqiang--Lanxiaobin");

		Assert.assertEquals("常量赋值", getString("{assign var=test value={a:1,'b':true}}{$test}"),
		    "{a=1, b=true}");
		Assert.assertEquals("常量赋值", getString("{assign var=test value=[true,, 10, 'test']}{$test}"),
		    "[true, null, 10, test]");
		Assert.assertEquals("常量赋值", getString("{assign var=test value=true}{$test}"),
		    "true");
		Assert.assertEquals("常量赋值", getString("{assign var=test value=5}{$test}"), "5");
		Assert.assertEquals("表达式赋值",
		    getString("{assign var=test value=$name}{$test}", data), "Bob");
		Assert.assertEquals("表达式赋值",
		    getString("{assign var=test value=$number}{$test}", data), "1");
		Assert.assertEquals("var必须指定", getException("{assign value=$names delimiter='--'}{$test[2]}", data), "\"var\" is required");
		Assert.assertEquals("value必须指定", getException("{assign var=test delimiter='--'}{$test[2]}", data), "\"value\" is required");
	}

	@Test
	public void advance() throws Exception {
		Context ctx;
		//变量不使用不生成代码
		ctx = getContext("{assign var=test value=value}");
		Assert.assertNull(ctx.get("test"));
		ctx = getContext("{assign var=test value=value}{include file='empty.tpl' inline}");
		Assert.assertNull(ctx.get("test"));
		//包含include时回写代码
		ctx = getContext("{assign var=test value=value}{include file='empty.tpl'}");
		Assert.assertNotNull(ctx.get("test"));
		//包含eval时回写代码
		ctx = getContext("{assign var=test value=value}{eval var='{\\$test}'}");
		Assert.assertEquals(new String((byte[])ctx.get("")), "value");
		Assert.assertNotNull(ctx.get("test"));
	}
}
