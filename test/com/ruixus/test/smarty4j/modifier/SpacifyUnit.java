package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class SpacifyUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "Something Went Wrong in Jet Crash, Experts Say.");

		Assert
		    .assertEquals(
		        "S o m e t h i n g   W e n t   W r o n g   i n   J e t   C r a s h ,   E x p e r t s   S a y .",
		        getString("{$articleTitle|spacify}", data));
		Assert
		    .assertEquals(
		        "S^^o^^m^^e^^t^^h^^i^^n^^g^^ ^^W^^e^^n^^t^^ ^^W^^r^^o^^n^^g^^ ^^i^^n^^ ^^J^^e^^t^^ ^^C^^r^^a^^s^^h^^,^^ ^^E^^x^^p^^e^^r^^t^^s^^ ^^S^^a^^y^^.",
		        getString("{$articleTitle|spacify:'^^'}", data));
	}
}
