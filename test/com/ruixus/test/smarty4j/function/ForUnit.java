package com.ruixus.test.smarty4j.function;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.smarty4j.Context;
import com.ruixus.test.smarty4j.BaseUnit;

public class ForUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		data.put("map", map);
		map.put("Hunan", "Changsha");
		map.put("Hubei", "Wuhan");
		map.put("Henan", "Zhengzhou");
		map.put("Hebei", "Shijiazhuang");

		Assert.assertEquals("1\n2\n3\n", getString("{for $i=1 to 3}{$i}\n{forelse}else{/for}"));
		Assert.assertEquals("3\n5\n", getString("{for $i=3 to 8 step 2 max=2}{$i}\n{forelse}else{/for}"));
		Assert.assertEquals("8\n6\n", getString("{for $i=8 to 0 step -2 max=2}{$i}\n{forelse}else{/for}"));
		Assert.assertEquals("3\n2\n1\n", getString("{for $i=3 to 1 step -1}{$i}\n{forelse}else{/for}"));
		Assert.assertEquals("else", getString("{for $i=1 to 3 step 0}{$i}\n{forelse}else{/for}"));
	}

	@Test
	public void advance() throws Exception {
		Context ctx;
		//变量不使用不生成代码
		ctx = getContext("{for $test=1 to 3}{$test}\n{forelse}else{/for}");
		Assert.assertNull(ctx.get("test"));
		ctx = getContext("{for $test=1 to 3}{$test}\n{forelse}else{/for}{include file='empty.tpl' inline}");
		Assert.assertNull(ctx.get("test"));
		//包含include时回写代码
		ctx = getContext("{for $test=1 to 3}{$test}\n{forelse}else{/for}{include file='empty.tpl'}");
		Assert.assertNotNull(ctx.get("test"));
		//包含eval时回写代码
		ctx = getContext("{for $test=1 to 3}{eval var='{\\$test}'}\n{forelse}else{/for}");
		Assert.assertEquals(new String((byte[])ctx.get("")), "1\n2\n3\n");
		Assert.assertNull(ctx.get("test"));
		ctx = getContext("{for $test=1 to 3}{eval var='{\\$test}'}\n{forelse}else{/for}");
		Assert.assertEquals(new String((byte[])ctx.get("")), "1\n2\n3\n");
		Assert.assertNull(ctx.get("test"));
	}
}
