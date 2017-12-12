package com.ruixus.test.smarty4j.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.test.smarty4j.BaseUnit;

public class IfUnit extends BaseUnit {

	@Test
	public void basic() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("boolean", false);
		data.put("string", "test a string");
		data.put("int", new Integer(-1));
		data.put("double", new Double(0.0));
		data.put("list", new ArrayList<Object>());

		Assert.assertEquals("全等于", getString("{if $null===$null}1{/if}", data), "1");
		Assert.assertEquals("全等于", getString("{if $null===$double}1{/if}", data), "");
		Assert.assertEquals("全等于", getString("{if $int===$null}1{/if}", data), "");
		Assert.assertEquals("全等于", getString("{if $null===null}1{/if}", data), "1");
		Assert.assertEquals("全等于", getString("{if $null===0}1{/if}", data), "");
		Assert.assertEquals("全等于", getString("{if $null===0.0}1{/if}", data), "");
		Assert.assertEquals("全等于", getString("{if $null===''}1{/if}", data), "");
		Assert.assertEquals("全等于", getString("{if 0===null}1{/if}", data), "");
		Assert.assertEquals("全等于", getString("{if 0===0}1{/if}", data), "1");
		Assert.assertEquals("全等于", getString("{if 0===0.0}1{/if}", data), "");
		Assert.assertEquals("全等于", getString("{if 0===''}1{/if}", data), "");
		Assert.assertEquals("全等于", getString("{if 0===$double}1{/if}", data), "");
		Assert.assertEquals("全等于", getString("{if 0.0===0.0}1{/if}", data), "1");
		Assert.assertEquals("全等于", getString("{if 0.0===$double}1{/if}", data), "1");
		Assert.assertEquals("全等于", getString("{if 0 + 0===$double}1{/if}", data), "1");
		Assert.assertEquals("全等于", getString("{if 1==='1'}1{/if}", data), "");
		Assert.assertEquals("全等于", getString("{if 1===-$int}1{/if}", data), "");
		Assert.assertEquals("全等于", getString("{if 1===$double-$int}1{/if}", data), "");
		Assert.assertEquals("全等于", getString("{if 1===$double+(-$int)}1{/if}", data), "");
		Assert.assertEquals("全等于", getString("{if -1===$int}1{/if}", data), "1");
		Assert.assertEquals("全等于", getString("{if 0-1===$int}1{/if}", data), "");
		Assert.assertEquals("全等于", getString("{if $double-1===$int}1{/if}", data), "");
		Assert.assertEquals("全等于", getString("{if -$int===1}1{/if}", data), "");

		Assert.assertEquals("全不等于", getString("{if $null!==$null}1{/if}", data), "");
		Assert.assertEquals("全不等于", getString("{if $null!==$double}1{/if}", data), "1");
		Assert.assertEquals("全不等于", getString("{if $int!==$null}1{/if}", data), "1");
		Assert.assertEquals("全不等于", getString("{if $null!==null}1{/if}", data), "");
		Assert.assertEquals("全不等于", getString("{if $null!==0}1{/if}", data), "1");
		Assert.assertEquals("全不等于", getString("{if $null!==0.0}1{/if}", data), "1");
		Assert.assertEquals("全不等于", getString("{if $null!==''}1{/if}", data), "1");
		Assert.assertEquals("全不等于", getString("{if 0!==null}1{/if}", data), "1");
		Assert.assertEquals("全不等于", getString("{if 0!==0}1{/if}", data), "");
		Assert.assertEquals("全不等于", getString("{if 0!==0.0}1{/if}", data), "1");
		Assert.assertEquals("全不等于", getString("{if 0!==''}1{/if}", data), "1");
		Assert.assertEquals("全不等于", getString("{if 0!==$double}1{/if}", data), "1");
		Assert.assertEquals("全不等于", getString("{if 0.0!==0.0}1{/if}", data), "");
		Assert.assertEquals("全不等于", getString("{if 0.0!==$double}1{/if}", data), "");
		Assert.assertEquals("全不等于", getString("{if 0 + 0!==$double}1{/if}", data), "");
		Assert.assertEquals("全不等于", getString("{if 1!=='1'}1{/if}", data), "1");
		Assert.assertEquals("全不等于", getString("{if 1!==-$int}1{/if}", data), "1");
		Assert.assertEquals("全不等于", getString("{if 1!==$double-$int}1{/if}", data), "1");
		Assert.assertEquals("全不等于", getString("{if 1!==$double+(-$int)}1{/if}", data), "1");
		Assert.assertEquals("全不等于", getString("{if 0-1!==$int}1{/if}", data), "1");
		Assert.assertEquals("全不等于", getString("{if $double-1!==$int}1{/if}", data), "1");
		Assert.assertEquals("全不等于", getString("{if -$int!==1}1{/if}", data), "1");

		Assert.assertEquals("等于", getString("{if $null==$null}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if $null==$double}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if $int==$null}1{/if}", data), "");
		Assert.assertEquals("等于", getString("{if $null==null}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if $null==0}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if $null==0.0}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if $null==''}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if 0==null}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if 0==0}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if 0==0.0}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if 0==''}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if 0==$double}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if 0.0==0.0}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if 0.0==$double}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if 0 + 0==$double}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if 1=='1'}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if 1==-$int}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if 1==$double-$int}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if 1==$double+(-$int)}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if -1==$int}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if 0-1==$int}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if $double-1==$int}1{/if}", data), "1");
		Assert.assertEquals("等于", getString("{if -$int==1}1{/if}", data), "1");

		Assert.assertEquals("不等于", getString("{if $null!=$null}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if $null!=$double}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if $int!=$null}1{/if}", data), "1");
		Assert.assertEquals("不等于", getString("{if $null!=null}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if $null!=0}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if $null!=0.0}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if $null!=''}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if 0!=null}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if 0!=0}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if 0!=0.0}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if 0!=''}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if 0!=$double}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if 0.0!=0.0}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if 0.0!=$double}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if 0 + 0!=$double}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if 1!='1'}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if 1!=-$int}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if 1!=$double-$int}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if 1!=$double+(-$int)}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if -1!=$int}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if 0-1!=$int}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if $double-1!=$int}1{/if}", data), "");
		Assert.assertEquals("不等于", getString("{if -$int!=1}1{/if}", data), "");

		Assert.assertEquals("一元检测", getString("{if $null}1{/if}", data), "");
		Assert.assertEquals("一元检测", getString("{if $list}1{/if}", data), "1");
		Assert.assertEquals("一元检测", getString("{if $double+0}1{/if}", data), "");
		Assert.assertEquals("一元检测", getString("{if null}1{/if}", data), "");
		Assert.assertEquals("一元检测", getString("{if 0}1{/if}", data), "");
		Assert.assertEquals("一元检测", getString("{if 0.0}1{/if}", data), "");
		Assert.assertEquals("一元检测", getString("{if ''}1{/if}", data), "");
		Assert.assertEquals("一元检测", getString("{if '0'}1{/if}", data), "1");

		Assert.assertEquals("小于", getString("{if 11<11.0}1{/if}", data), "");
		Assert.assertEquals("小于", getString("{if '11'<11.0}1{/if}", data), "");
		Assert.assertEquals("小于", getString("{if 11<'11.0'}1{/if}", data), "");
		Assert.assertEquals("小于", getString("{if '11'<'11.0'}1{/if}", data), "1");
		Assert.assertEquals("小于等于", getString("{if 11<=11.0}1{/if}", data), "1");
		Assert.assertEquals("小于等于", getString("{if '11'<=11.0}1{/if}", data), "1");
		Assert.assertEquals("小于等于", getString("{if 11<='11.0'}1{/if}", data), "1");
		Assert.assertEquals("小于等于", getString("{if '11'<='11.0'}1{/if}", data), "1");
		Assert.assertEquals("大于", getString("{if 11>11.0}1{/if}", data), "");
		Assert.assertEquals("大于", getString("{if '11'>11.0}1{/if}", data), "");
		Assert.assertEquals("大于", getString("{if 11>'11.0'}1{/if}", data), "");
		Assert.assertEquals("大于", getString("{if '11'>'11.0'}1{/if}", data), "");
		Assert.assertEquals("大于等于", getString("{if 11>=11.0}1{/if}", data), "1");
		Assert.assertEquals("大于等于", getString("{if '11'>=11.0}1{/if}", data), "1");
		Assert.assertEquals("大于等于", getString("{if 11>='11.0'}1{/if}", data), "1");
		Assert.assertEquals("大于等于", getString("{if '11'>='11.0'}1{/if}", data), "");
		Assert.assertEquals("与", getString("{if '' && null}1{/if}", data), "");
		Assert.assertEquals("与", getString("{if '' && 1.1}1{/if}", data), "");
		Assert.assertEquals("与", getString("{if 1.1 && ''}1{/if}", data), "");
		Assert.assertEquals("与", getString("{if '0.1' && 1.1}1{/if}", data), "1");
		Assert.assertEquals("或", getString("{if '' || null}1{/if}", data), "");
		Assert.assertEquals("或", getString("{if '' || 1.1}1{/if}", data), "1");
		Assert.assertEquals("或", getString("{if 1.1 || ''}1{/if}", data), "1");
		Assert.assertEquals("或", getString("{if '0.1' || 1.1}1{/if}", data), "1");
		Assert.assertEquals("非", getString("{if !''}1{/if}", data), "1");
		Assert.assertEquals("非", getString("{if !$null}1{/if}", data), "1");
		Assert.assertEquals("非", getString("{if !0}1{/if}", data), "1");
		Assert.assertEquals("表达式计算", getString("{if 1.1+1>2}1{/if}", data), "1");
		Assert.assertEquals("表达式计算", getString("{if 1.1+'1'>2}1{/if}", data), "1");
		Assert.assertEquals("表达式计算", getString("{if (1.1+!'1')>2}1{/if}", data), "");
		Assert.assertEquals("表达式计算", getString("{if (1.1+'1')>2}1{/if}", data), "1");
		Assert.assertEquals("表达式计算", getString("{if 1.1+null<1.11}1{/if}", data), "1");

		Assert.assertEquals("条件表达式短路处理", getString("{if 0 || 1}1{else}2{/if}", data), "1");
		Assert.assertEquals("条件表达式短路处理", getString("{if 1 && 0}1{else}2{/if}", data), "2");

		Assert.assertEquals("else语句", getString("{if 0}1{else}2{/if}", data), "2");
		Assert.assertEquals("elseif语句", getString("{if 0}1{elseif 1}2{/if}", data), "2");

		Assert.assertEquals("else不能重复出现", getException("{if 0}1{else}2{else}3{/if}", data),
		    "\"else\" is already defined");
		Assert.assertEquals("elseif不能在else之后出现", getException("{if 0}1{else}3{elseif 1}2{/if}", data),
		    "\"elseif\" not correct");
	}
}
