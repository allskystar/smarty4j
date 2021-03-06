package com.ruixus.smarty4j.statement.function;

import com.ruixus.smarty4j.MethodVisitorProxy;
import com.ruixus.smarty4j.VariableManager;
import com.ruixus.smarty4j.statement.Function;
import com.ruixus.smarty4j.statement.ParentType;

/**
 * 参见iterate函数。
 * 
 * @see com.ruixus.smarty4j.statement.function.$section
 * 
 * @version 1.1.0, 2015/05/16
 * @author Ouyang Xianwei
 * @since Smarty 1.1
 */
@ParentType(name = "iterate")
public class $iterateelse extends Function {

	@Override
	public void parse(MethodVisitorProxy mv, int local, VariableManager vm) {
	}
}
