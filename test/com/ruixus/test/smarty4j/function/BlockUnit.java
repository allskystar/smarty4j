package com.ruixus.test.smarty4j.function;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class BlockUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name", new HashMap<String, Object>());

		Assert.assertEquals("<title>test | Default Title | </title>",
		    getString("{extends file='parent.tpl'}\n{block name=title append}test{/block}"));
		Assert.assertEquals("<title>The Child Title was inserted here</title>",
		    getString("{extends file='parent2.tpl'}\n{block name=title}Child Title{/block}"));
	}
}
