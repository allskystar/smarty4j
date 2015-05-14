package com.ruixus.test.smarty4j.function;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class StripUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("border", 0);
		data.put("URL", "http://my.domain.com");

		Assert
		    .assertEquals(
		        "strip",
		        getString(
		            "{strip}\n<table border={$border}>\n  <tr>\n    <td>\n<pre>\n <b >Test</ b><radio   checked>\n</pre   >\n<textarea>\n <b >Test</ b><radio   checked>\n< / textarea >\t<a   href  =  \"{$URL}\"  >\n      <  font   color=\"red\"  >This is a test</font>\n      <  /  a  >\n    </td>\n  </tr>\n\r</table>{/strip}1111",
		            data),
		        "<table border=0><tr><td><pre>\n <b>Test</b><radio checked>\n</pre><textarea>\n <b >Test</ b><radio   checked>\n</textarea><a href=\"http://my.domain.com\"><font color=\"red\">This is a test</font></a></td></tr></table>1111");
		Assert
		    .assertEquals(
		        "strip",
		        getString(
		            "{strip}<style>\n  .aa   #bb \n {  ;;background  :  no-repeat    #FFF   ;  \n  border:1px solid red;}  .bb   {   \n\ttext-align:center\n  }  \n</style><script>\n\tvar a=\"</script>\"\n</script>{/strip}",
		            data),
		        "<style>.aa #bb{background:no-repeat #FFF;border:1px solid red}.bb{text-align:center}</style><script>\n\tvar a=\"</script>\"\n</script>");
	}
}
