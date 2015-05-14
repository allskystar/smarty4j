package com.ruixus.test.smarty4j;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ SyntaxUnit.class, FunctionUnit.class, ModifierUnit.class })
public class Smarty4jUnit {
	// {$map['A1']}
	// {$map[$bar]}
	// {$map->bar}
	// {$map->bar()}
	// {#foo#}
	// {$foo={counter}+3}
	// {$foo="this is message {counter}"}
	// {$foo=$bar+2}
	// {$foo = strlen($bar)}
	// {$foo = myfunct( ($x+$y)*3 )}
	// {$foo.bar=1}
	// {$foo.bar.baz=1}
	// {$foo[]=1}
	// {assign var=foo value=[1,2,3]}
	// {assign var=foo value=['y'=>'yellow','b'=>'blue']}
	// {assign var=foo value=[1,[9,8],3]}
	// {assign var=foo value=$x+$y}
	// $foo_{$bar}
	// $foo_{$x+$y}
	// $foo_{$bar}_buh_{$blar}
	// {$foo_{$x}}
}
