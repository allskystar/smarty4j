package com.ruixus.smarty4j.statement;

import com.ruixus.smarty4j.Node;
import com.ruixus.smarty4j.VariableManager;
import com.ruixus.smarty4j.expression.Expression;
import com.ruixus.smarty4j.expression.VariableExpression;
import com.ruixus.util.MethodVisitorProxy;

public class SetStatement extends Node {

	private VariableExpression var;
	private Expression value;

	public SetStatement(VariableExpression var, Expression value) {
		this.var = var;
		this.value = value;
	}

	@Override
	public void parse(MethodVisitorProxy mv, int local, VariableManager vm) {
		value.parseObject(mv, local, vm);
		var.parseSet(mv, local, vm);
	}
}
