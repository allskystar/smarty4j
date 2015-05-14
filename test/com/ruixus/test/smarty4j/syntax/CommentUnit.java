package com.ruixus.test.smarty4j.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

/**
 * unsupport list:
 */
public class CommentUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		list.add(true);
		data.put("int", -1);
		data.put("list", list);
		
		Assert.assertEquals("",
		    getString("{* I am a Smarty comment, I don't exist in the compiled output  *}"));
		Assert.assertEquals("",
		    getString("{* this multiline smarty\n comment is\n not sent to browser\n  *}"));
		Assert
		    .assertEquals(
		        "\n <options=null selected=null>\n</select>\n*}",
		        getString("{*\n<select name=\"company\">\n {* <option value=\"0\">-- none -- </option> *}\n <options={$vals} selected={$selected_id}>\n</select>\n*}"));

		Assert.assertEquals("", getString("{* \"comment *}"));
		Assert.assertEquals("", getString("{ * comment * }"));
		Assert.assertEquals("", getString("{  *comment*comment*  }"));
		// nest comments
		Assert.assertEquals("comment*}", getString("{*comment{* *}comment*}"));

		Assert.assertEquals("Syntax error on token \"*\"", getException("{*}"));
		Assert.assertEquals("Syntax error on token \"*\"", getException("{ * }"));
		Assert.assertEquals("Syntax error on token \"*\"", getException("{*comment}"));

		Assert.assertEquals("9", getString("{8+2|count_characters}"));
		Assert.assertEquals("2", getString("{(8+2)|count_characters}"));
	}
}
