package com.ruixus.test.smarty4j.function;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class ForeachUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		data.put("map", map);
		map.put("Hunan", "Changsha");
		map.put("Hubei", "Wuhan");
		map.put("Henan", "Zhengzhou");
		map.put("Hebei", "Shijiazhuang");

		Assert
		    .assertEquals(
		        "信息体",
		        getString("{assign var=\"animals\" value=[Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar]}{foreach name=test from=$animals item=\"item\" key=\"key\"}{$smarty.foreach.test.first}:{$smarty.foreach.test.last}\n{/foreach}"),
		        "true:false\nfalse:false\nfalse:false\nfalse:false\nfalse:false\nfalse:false\nfalse:false\nfalse:false\nfalse:false\nfalse:false\nfalse:false\nfalse:true\n");
		Assert
		    .assertEquals(
		        "item与key读写",
		        getString("{assign var=\"animals\" value=[Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar]}{foreach from=$animals item=\"item\" key=\"key\"}{$key+1}:{$item}\n{/foreach}"),
		        "1:Rat\n2:Ox\n3:Tiger\n4:Hare\n5:Dragon\n6:Serpent\n7:Horse\n8:Sheep\n9:Monkey\n10:Rooster\n11:Dog\n12:Boar\n");
		Assert
		    .assertEquals(
		        "循环变量不改变外部变量生存域的值",
		        getString("{assign var=\"animals\" value=[Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar]}{assign var=\"item\" value='1'}{assign var=\"key\" value='A'}{foreach from=$animals item=\"item\" key=\"key\"}{assign var=\"key\" value=\"1\"}{$key}:{$item}\n{/foreach}{$item}{$key}"),
		        "1:Rat\n1:Ox\n1:Tiger\n1:Hare\n1:Dragon\n1:Serpent\n1:Horse\n1:Sheep\n1:Monkey\n1:Rooster\n1:Dog\n1:Boar\n1A");
		Assert
		    .assertEquals(
		        "未指定的循环变量值变化将改变外部变量生存域的值",
		        getString("{assign var=\"animals\" value=[Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar]}{assign var=\"item\" value='1'}{assign var=\"key\" value='A'}{foreach from=$animals item=\"item\"}{assign var=\"key\" value=\"1\"}{$key}:{$item}\n{/foreach}{$item}{$key}"),
		        "1:Rat\n1:Ox\n1:Tiger\n1:Hare\n1:Dragon\n1:Serpent\n1:Horse\n1:Sheep\n1:Monkey\n1:Rooster\n1:Dog\n1:Boar\n11");
		Assert
		    .assertEquals(
		        "基于Map循环",
		        getString(
		            "{foreach from=$map item=\"item\" key=\"key\"}{if $key=='Hunan'}{eval var='{\\$key}:{\\$item}'}{/if}{/foreach}",
		            data), "Hunan:Changsha");
		Assert
		    .assertEquals(
		        "foreachelse测试",
		        getString("{foreach from=$animals1 item=\"item\" key=\"key\"}{$key}:{$item}\n{foreachelse}2{/foreach}"),
		        "2");
		Assert.assertEquals("foreachelse只能定义在foreach中",
		    getException("{if $animals}{$key}:{$item}\n{foreachelse}2{/if}"),
		    "\"foreachelse\" must be used inside of \"foreach\"");
		Assert.assertEquals("foreachelse不能重复定义",
		    getException("{foreach from=$map item=item}1{foreachelse}2{foreachelse}3{/foreach}"),
		    "\"foreachelse\" is already defined");

		Assert
    .assertEquals(
        "smarty3语法",
        getString("{assign var=animals value=[Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar]}{foreach $animals as $key => $item}{$item@index+1}:{$item}\n{/foreach}"),
        "1:Rat\n2:Ox\n3:Tiger\n4:Hare\n5:Dragon\n6:Serpent\n7:Horse\n8:Sheep\n9:Monkey\n10:Rooster\n11:Dog\n12:Boar\n");
	}
}
