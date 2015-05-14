package com.ruixus.test.smarty4j.modifier;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class WordwrapUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("articleTitle", "Blind woman gets new kidney from dad she hasn't seen in years.");

		Assert.assertEquals("Blind woman gets new kidney\nfrom dad she hasn't seen in\nyears.",
		    getString("{$articleTitle|wordwrap:30}", data));
		Assert.assertEquals("Blind woman gets new\nkidney from dad she\nhasn't seen in\nyears.",
		    getString("{$articleTitle|wordwrap:20}", data));
		Assert.assertEquals(
		    "Blind woman gets new kidney<br />\nfrom dad she hasn't seen in<br />\nyears.",
		    getString("{$articleTitle|wordwrap:30:'<br />\\n'}", data));
		Assert.assertEquals("Blind woman gets new kidn\ney from dad she hasn't se\nen in years.",
		    getString("{$articleTitle|wordwrap:25:'\\n':true}", data));
	}
}
