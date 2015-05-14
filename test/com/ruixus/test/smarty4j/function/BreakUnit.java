package com.ruixus.test.smarty4j.function;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class BreakUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("array",
		    "Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar".split(","));
		Assert.assertEquals(
		    "默认跳出一层",
		    getString(
		        "{section loop=$array name=\"item\" start=5 step=-2 max=2}{break}{$item}\n{/section}",
		        data), "");
		Assert
		    .assertEquals(
		        "指定跳出多层",
		        getString(
		            "{foreach from=$array item=\"item1\"}{foreach from=$array item=\"item\" key=\"key\"}{if $key==4}{break 2}{/if}{$key}:{$item}\n{/foreach}{/foreach}",
		            data), "0:Rat\n1:Ox\n2:Tiger\n3:Hare\n");
		Assert.assertEquals("必须在循环体中", getException("{break}"),
		    "\"break\" must be used inside of a loop");
		Assert
		    .assertEquals(
		        "跳出太多的循环层级",
		        getException("{foreach from=$array item=\"item\" key=\"key\"}{if $key==4}{break 2}{/if}{$key}:{$item}\n{/foreach}"),
		        "\"break\" must be used inside of the 2th loop");
		Assert
		    .assertEquals(
		        "参数必须大于0",
		        getException("{foreach from=$array item=\"item\" key=\"key\"}{if $key==4}{break 0}{/if}{$key}:{$item}\n{/foreach}"),
		        "Parameter format not correct");
		Assert
		    .assertEquals(
		        "参数必须大于0",
		        getException("{foreach from=$array item=\"item\" key=\"key\"}{if $key==4}{break -10}{/if}{$key}:{$item}\n{/foreach}"),
		        "Parameter format not correct");
		Assert
		    .assertEquals(
		        "参数不接受浮点类型",
		        getException("{foreach from=$array item=\"item\" key=\"key\"}{if $key==4}{break 1.0}{/if}{$key}:{$item}\n{/foreach}"),
		        "Parameter format not correct");
	}
}
