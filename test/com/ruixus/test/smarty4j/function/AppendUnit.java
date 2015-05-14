package com.ruixus.test.smarty4j.function;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.smarty4j.Context;
import com.ruixus.test.smarty4j.BaseUnit;

public class AppendUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name", new HashMap<String, Object>());

		Assert.assertEquals("The first name is Bob.", getString("{append var='name' value='Bob' index='first'}The first name is {$name.first}."));
		Assert.assertEquals("The last name is Meyer.", getString("{append var='name' value='Meyer' index='last'}The last name is {$name.last}.", data));
	}

	@Test
	public void advance() throws Exception {
		Context ctx;
		//变量不使用不生成代码
		ctx = getContext("{append var='test' value='Bob' index='first'}");
		Assert.assertNull(ctx.get("test"));
		ctx = getContext("{append var='test' value='Bob' index='first'}{include file='empty.tpl' inline}");
		Assert.assertNull(ctx.get("test"));
		//包含include时回写代码
		ctx = getContext("{append var='test' value='Bob' index='first'}{include file='empty.tpl'}");
		Assert.assertNotNull(ctx.get("test"));
		//包含eval时回写代码
		ctx = getContext("{append var='test' value='Bob' index='first'}{eval var='{\\$test}'}");
		Assert.assertEquals(new String((byte[])ctx.get("")), "{first=Bob}");
		Assert.assertNotNull(ctx.get("test"));
	}
}
