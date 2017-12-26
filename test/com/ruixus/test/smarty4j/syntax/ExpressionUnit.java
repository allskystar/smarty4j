package com.ruixus.test.smarty4j.syntax;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ruixus.smarty4j.Context;
import com.ruixus.smarty4j.Engine;
import com.ruixus.smarty4j.Template;
import com.ruixus.util.SimpleStringWriter;

public class ExpressionUnit {

	public class Bean {
		public int getNumber() {
			return 10;
		}

		public String getAAA() {
			return "AAA";
		}

		public String toString() {
			return "test.SmartyUnit$Bean";
		}
	}

	private static Context ctx;

	private static Engine engine = new Engine();

	@Before
	public void setUp() throws Exception {
		ctx = new Context();

		List<Object> list = new ArrayList<Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		Bean bean = new Bean();

		ctx.set("now", new Date());
		ctx.set("bytes", "测试字节数组".getBytes("UTF-8"));
		ctx.set("boolean", false);
		ctx.set("string", "测试字符串");
		ctx.set("int", new Integer(-1));
		ctx.set("double", new Double(0.0));
		ctx.set("bean", bean);
		ctx.set("array",
		    "Rat,Ox,Tiger,Hare,Dragon,Serpent,Horse,Sheep,Monkey,Rooster,Dog,Boar".split(","));

		ctx.set("list", list);
		list.add(true);
		list.add(0);
		list.add(3.4);
		list.add("string");
		list.add(bean);

		ctx.set("map", map);
		map.put("boolean", true);
		map.put("int", 0);
		map.put("double", 3.4);
		map.put("string", "bean");
		map.put("bean", bean);

		engine.setTemplatePath(System.getProperty("user.dir").replace('\\', '/')
		    + "/WEB-INF/classes/org/lilystudio/test");
	}

	@Test
	public void testChildTemplate() throws Exception {
		Template tpl = new Template(engine, "{cycle values=\"#eeeeee,#d0d0d0\"}");
		Context childContext = new Context(ctx);
		Writer writer = new SimpleStringWriter();
		tpl.merge(childContext, writer);
	}

	@Test
	public void testBasicSyntax() throws Exception {
		// 基础语法测试
		Assert.assertNull("空语句", getResult("{}"));
		Assert.assertNull("有空格的空语句", getResult("{  }"));

		// 关键字语法测试
		Assert.assertEquals(getResult("{true}"), "true");
		Assert.assertEquals(getResult("{false}"), "false");
		Assert.assertEquals(getResult("{null}"), "null");

		// 函数语法测试
		Assert.assertNull("函数必须的条件不能省略", getResult("{if}{/if}"));
		Assert.assertNull("函数要有结束标记", getResult("{if $boolean}"));
		Assert.assertNull("函数嵌入标记有顺序要求",
		    getResult("{if $boolean}{foreach from=$list item='item'}{else}{/foreach}{/if}"));
		Assert.assertEquals("函数属性使用字符串可以不用引号", getResult("{assign var=out value='string'}{$out}"),
		    "string");
		Assert.assertEquals("函数属性可以使用表达式", getResult("{assign var=out value=$int+$int test=true test1=false}{$out}"),
		    "-2");
		Assert.assertEquals("引号内的值嵌入", getResult("{'$string.'}"), "测试字符串.");
		Assert.assertEquals("引号内的值嵌入", getResult("{'$list[3]'}"), "string");
		Assert.assertEquals("引号内的值嵌入", getResult("{'$map.string'}"), "bean");
		Assert.assertEquals("引号内的值嵌入", getResult("{'`$map.string`'}"), "bean");
		Assert.assertEquals("引号内的值嵌入", getResult("{'$'}"), "$");
		Assert.assertEquals("引号内的转义字符", getResult("{'\\\"\\'\\$\\`'}"), "\"'$`");

		// 错误语法
		Assert.assertNull(".后面必须扩展", getResult("{$test.}"));
		Assert.assertNull("@后面必须扩展", getResult("{$test|@}"));
		Assert.assertNull("[]必须有内容", getResult("{$test[]}"));
		Assert.assertNull("[必须结束", getResult("{$test[}"));

		// 直接计算输出
		Assert.assertEquals("与运算", getResult("{$string and $int}"), "-1");
		Assert.assertEquals("与运算", getResult("{$null && $int}"), "null");
		Assert.assertEquals("或运算", getResult("{$string || $int}"), "测试字符串");
		Assert.assertEquals("或运算", getResult("{$null || $int}"), "-1");
	}

	@Test
	public void testBean() throws Exception {
		Context c = new Context();
		Bean bean = new Bean();
		c.putBean(bean);
		Template tpl = new Template(engine, "{$number}");
		Writer writer = new SimpleStringWriter();
		tpl.merge(c, writer);
		Assert.assertEquals("Bean导入", writer.toString(), "10");

		ctx.set("bean", bean);
		Assert.assertEquals("Bean导入", getResult("{$bean.number}{$bean.number}{$bean.aaa}{$bean.aaa}"),
		    "1010nullnull");
	}

	@Test
	public void testAnalyse() throws Exception {
		// 基本的分词识别, 如果不包含任何内容, 或者内容只是一些空格, 提示语法错误
		Assert.assertNull("空语句", getResult("{}"));
		Assert.assertNull("空语句", getResult("{    }"));
		// 引号没有结束, 不是合法的句型
		Assert.assertNull("引号", getResult("{\"}"));
		Assert.assertNull("引号", getResult("{\"'}"));

		Assert.assertEquals("文本输出", getResult("test\n"), "test\n");
		Assert.assertEquals("转义字符", getResult("{\"}'\\\"\\n\\t\"}"), "}'\"\n\t");
		Assert.assertEquals("转义字符", getResult("{'}\"\\'\\n\\t\\\\'}"), "}\"'\n\t\\");

		engine.setLeftDelimiter("<!--");
		engine.setRightDelimiter("-->");
		Assert.assertEquals("自定义左右边界符", getResult("<!--$string-->"), "测试字符串");
		engine.setLeftDelimiter("{");
		engine.setRightDelimiter("}");
	}

	@Test
	public void testPrint() throws Exception {
		Assert.assertNull("缺.引用", getResult("{$map(status)}"));
		Assert.assertNull("括号缺失", getResult("{$map.status)}"));
		Assert.assertNull("括号缺失", getResult("{$map.(status}"));
		Assert.assertNull("括号缺失", getResult("{$list[0}"));
		Assert.assertNull("括号缺失", getResult("{$list[0]]}"));
		Assert.assertEquals("不完整语法", getResult("{$string."), "{$string.");
		Assert.assertEquals("普通变量", getResult("{$string}"), "测试字符串");
		Assert.assertEquals("普通映射", getResult("{$map.int}"), "0");
		Assert.assertEquals("Bean映射", getResult("{$bean.number}"), "10");
		Assert.assertEquals("普通列表", getResult("{$list[0]}"), "true");
		Assert.assertEquals("数组列表", getResult("{$array[4]}"), "Dragon");
		Assert.assertEquals("字符串", getResult("{$array[4][2]}"), "a");
		Assert.assertEquals("列表表达式", getResult("{$array[3 + 1]}"), "Dragon");
		Assert.assertEquals("NULL数据", getResult("{$list2.status[2]}"), "null");
		Assert.assertEquals("加括号组合", getResult("{($map.`$list[3]`)}"), "bean");
		String name = this.getClass().getName().replace('.', '/') + "$Bean";
		Assert.assertEquals("bean输出", getResult("{declare var='bean' class='" + this.getClass().getName() + "\\$Bean'}{$bean.number}"), "10");
		Assert.assertEquals("bean输出", getResult("{$bean.AAA#" + name + "}"), "AAA");
		Assert.assertEquals("值的变量调节器", getResult("{assign var=test value=$bean.AAA#" + name + "|lower}{$test}"), "aaa");
	}

	@Test
	public void testOperation() throws Exception {
		Assert.assertEquals("表达式计算", getResult("{`$int + 1`}"), "0.0");
		Assert.assertEquals("表达式计算", getResult("{$int + 1}"), "0");
		Assert.assertEquals("表达式计算", getResult("{$int + -1}"), "-2");
		Assert.assertEquals("表达式计算", getResult("{'2' + '1'}"), "3");
		Assert.assertEquals("表达式计算", getResult("{true + 1}"), "2");
		Assert.assertEquals("表达式计算", getResult("{`true + 1.0`}"), "2.0");
		Assert.assertEquals("表达式计算", getResult("{false + 1}"), "1");
		Assert.assertEquals("表达式计算", getResult("{`false + 1.0`}"), "1.0");
		Assert.assertEquals("表达式计算", getResult("{2 + 1.1}"), "3.1");
		Assert.assertEquals("表达式计算", getResult("{(3 > 2) + 1}"), "2");
		Assert.assertEquals("表达式计算", getResult("{(3 > 2) + 1.1}"), "2.1");

		Assert.assertEquals("表达式计算优化", getResult("{0-2}"), "-2");
	}

	@Test
	public void testFunction() throws Exception {
		Assert
		    .assertNull(getResult("{foreach from=}kick\n{else}{/foreach}\n\n{if a+}{$c|indent:'1':1:1}"));
		ctx.set("name", null);
	}

	@Test
	public void testModifier() throws Exception {
		Assert.assertEquals("count$LIST", getResult("{if $list|@count == 5}true{/if}"), "true");
		Assert.assertEquals("b2s", getResult("{$bytes|from_charset:'UTF-8'}"), "测试字节数组");
		ctx.set("length", "kick");
		Assert.assertEquals("count$STRING", getResult("{$length|count}"), "4");
		Assert.assertEquals("count$LIST", getResult("{$list|@count}"), "5");
	}

	private String getResult(String data) throws Exception {
		try {
			Template tpl = new Template(engine, data);
			Writer writer = new SimpleStringWriter();
			tpl.merge(ctx, writer);
			return writer.toString();
		} catch (Exception e) {
			if (data
			    .equals("{assign var=test value=$bean.AAA@" + this.getClass().getName().replace('.', '/') + "$Bean|lower}")) {
				e.printStackTrace();
			}
			return null;
		}
	}
}