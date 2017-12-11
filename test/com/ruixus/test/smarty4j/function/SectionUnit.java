package com.ruixus.test.smarty4j.function;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class SectionUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Assert
		    .assertEquals(
		        "section",
		        getString("{math assign=max equation=\"x+x\" x=1.0}{assign var=\"animals\" value=[Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar]}{section loop=$animals name=\"item\" start=5 step=-2 max=$max}{$item}\n{/section}"),
		        "5\n3\n");
		Assert
    .assertEquals(
        "section",
        getString("{assign var=\"animals\" value=[Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar]}{section loop=$animals name=\"item\" start=5 step=-2}{$item}\n{/section}"),
        "5\n3\n1\n");
		Assert
    .assertEquals(
        "section",
        getString("{assign var=\"animals\" value=[Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar]}{section loop=$animals name=\"item\" start=5 step=-2}{$smarty.section.item.index_prev}\n{/section}"),
        "null\n5\n3\n");
		Assert
		    .assertEquals(
		        "section",
		        getString("{assign var=\"animals\" value=[Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar]}{section loop=$animals name=\"item\" start=5 step=-2 max=2}{assign var=\"key\" value=\"1\"}{$item}\n{/section}"),
		        "5\n3\n");
		Assert
		    .assertEquals(
		        "section",
		        getString("{assign var=\"animals\" value=[Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar]}{section loop=$animals name=\"item\" start=5 step=-2 max=2}\n{/section}"),
		        "\n\n");

		Assert
    .assertEquals(
        "section",
        getString("{assign var=\"animals\" value=[Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar]}{section loop=$animals name=\"item\" start=5 step=-2 max=2}{eval var='{\\$item}'}\n{/section}"),
        "5\n3\n");
	}
}
