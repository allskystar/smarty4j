package com.ruixus.smarty4j.expression.number;

import com.ruixus.smarty4j.VariableManager;
import com.ruixus.smarty4j.expression.Expression;
import com.ruixus.util.MethodVisitorProxy;

/**
 * 浮点数表达式转换节点, 将表达式转换成浮点数表达式
 * 
 * @version 1.1.0, 2015/05/16
 * @author Ouyang Xianwei
 * @since Smarty 1.1
 */
public class DoubleAdapter extends DoubleExpression {

	/** 需要转换的表达式 */
	private Expression exp;

	/**
	 * 建立整数表达式转换节点
	 * 
	 * @param exp
	 *          需要转换的表达式
	 */
	public DoubleAdapter(Expression exp) {
		this.exp = exp;
	}

  @Override
	public void parseDouble(MethodVisitorProxy mv, int local, VariableManager vm) {
		exp.parseDouble(mv, local, vm);
	}
}
