package com.ruixus.test.smarty4j.function;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class BytesUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bytes", "测试字节数组".getBytes());
		
		Assert.assertEquals("基本功能", new String(getBytes("{bytes $bytes}", data)), "测试字节数组");
		Assert.assertEquals("参数不支持常量", getException("{bytes \"key\"}", data), "Parameter format not correct");
	}
}
