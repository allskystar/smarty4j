package com.ruixus.test.smarty4j.function;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class ContinueUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("array",
		    "Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar".split(","));
		Assert
		    .assertEquals(
		        "默认跳回一层",
		        getString(
		            "{foreach from=$array item=\"item\" key=\"key\"}{if $key>=4}{continue}{/if}{$key}:{$item}\n{/foreach}",
		            data), "0:Rat\n1:Ox\n2:Tiger\n3:Hare\n");
		Assert
		    .assertEquals(
		        "指定跳回多层",
		        getString(
		            "{section loop=$array name=\"item1\"}{foreach from=$array item=\"item\" key=\"key\"}{if $key==1}{continue 2}{/if}{$item}\n{/foreach}{$item1}{/section}",
		            data), "Rat\nRat\nRat\nRat\nRat\nRat\nRat\nRat\nRat\nRat\nRat\nRat\n");
		Assert.assertEquals("必须在循环体中", getException("{continue}"),
		    "\"continue\" must be used inside of a loop");
		Assert
		    .assertEquals(
		        "跳回太多的循环层级",
		        getException("{section loop=$array name=\"item\" start=5 step=-2 max=2}{continue 2}{$item}\n{/section}"),
		        "\"continue\" must be used inside of the 2th loop");
		Assert
		    .assertEquals(
		        "参数值必须大于0",
		        getException("{section loop=$array name=\"item\" start=5 step=-2 max=2}{continue 0}{$item}\n{/section}"),
		        "Parameter format not correct");
		Assert
		    .assertEquals(
		        "参数值必须大于0",
		        getException("{section loop=$array name=\"item\" start=5 step=-2 max=2}{continue -10}{$item}\n{/section}"),
		        "Parameter format not correct");
		Assert
		    .assertEquals(
		        "参数不接受浮点类型",
		        getException("{section loop=$array name=\"item\" start=5 step=-2 max=2}{continue 1.0}{$item}\n{/section}"),
		        "Parameter format not correct");
	}
}
