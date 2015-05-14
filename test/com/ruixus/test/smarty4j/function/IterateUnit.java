package com.ruixus.test.smarty4j.function;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class IterateUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Assert
		    .assertEquals(
		        "iterate",
		        getString("{assign var=\"animals\" value=[Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar]}{iterate from=$animals item=\"item\"}{$item}\n{/iterate}"),
		        "Rat\nOx\nTiger\nHare\nDragon\nSerpent\nHorse\nSheep\nMonkey\nRooster\nDog\nBoar\n");
		Assert
    .assertEquals(
        "iterate",
        getString("{assign var=\"animals\" value=[Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar]}{iterate from=$animals item=\"item\"}{eval var='{\\$item}'}\n{/iterate}"),
        "Rat\nOx\nTiger\nHare\nDragon\nSerpent\nHorse\nSheep\nMonkey\nRooster\nDog\nBoar\n");
	}
}
