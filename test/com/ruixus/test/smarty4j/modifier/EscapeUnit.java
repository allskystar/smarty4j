package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class EscapeUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "'Stiff Opposition Expected to Casketless Funeral Plan'");
		data.put("EmailAddress", "smarty@example.com");

		Assert.assertEquals("&#39;Stiff Opposition Expected to Casketless Funeral Plan&#39;",
		    getString("{$articleTitle|escape}", data));
		Assert.assertEquals("&#39;Stiff Opposition Expected to Casketless Funeral Plan&#39;",
		    getString("{$articleTitle|escape:'html'}", data));
		Assert
		    .assertEquals(
		        "&#39;Stiff&nbsp;Opposition&nbsp;Expected&nbsp;to&nbsp;Casketless&nbsp;Funeral&nbsp;Plan&#39;",
		        getString("{$articleTitle|escape:'htmlall'}", data));
		Assert
		    .assertEquals(
		        "<a href=\"?title=%27Stiff%20Opposition%20Expected%20to%20Casketless%20Funeral%20Plan%27\">click here</a>",
		        getString("<a href=\"?title={$articleTitle|escape:'url'}\">click here</a>", data));
		Assert.assertEquals("\\'Stiff Opposition Expected to Casketless Funeral Plan\\'",
		    getString("{$articleTitle|escape:'quotes'}", data));
		Assert
		    .assertEquals(
		        "<a href=\"mailto:%73%6D%61%72%74%79%40%65%78%61%6D%70%6C%65%2E%63%6F%6D\">&#x73;&#x6D;&#x61;&#x72;&#x74;&#x79;&#x40;&#x65;&#x78;&#x61;&#x6D;&#x70;&#x6C;&#x65;&#x2E;&#x63;&#x6F;&#x6D;</a>",
		        getString(
		            "<a href=\"mailto:{$EmailAddress|escape:'hex'}\">{$EmailAddress|escape:'hexentity'}</a>",
		            data));
		Assert.assertEquals("smarty [AT] example [DOT] com",
		    getString("{$EmailAddress|escape:'mail'}", data));
	}
}
