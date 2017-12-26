package com.ruixus.smarty4j.statement.function;

import com.ruixus.smarty4j.Analyzer;
import com.ruixus.smarty4j.MessageFormat;
import com.ruixus.smarty4j.Operator;
import com.ruixus.smarty4j.ParseException;
import com.ruixus.smarty4j.VariableManager;
import com.ruixus.smarty4j.expression.Expression;
import com.ruixus.smarty4j.statement.Function;
import com.ruixus.smarty4j.statement.ParentType;
import com.ruixus.util.MethodVisitorProxy;
import com.ruixus.util.SimpleStack;

/**
 * @see com.ruixus.smarty4j.statement.function.$if
 * 
 * @version 1.1.0, 2015/05/16
 * @author Ouyang Xianwei
 * @since Smarty 1.1
 */
@ParentType(name = "if")
public class $elseif extends Function {

	/** 条件表达式 */
	private Expression check;

	/**
	 * 获取条件表达式
	 * 
	 * @return 条件表达式
	 */
	public Expression getCheckExpression() {
		return check;
	}

	@Override
	public void syntax(Analyzer analyzer, SimpleStack tokens) throws ParseException {
		if (tokens.size() == 1) {
			throw new ParseException(String.format(MessageFormat.NOT_CORRECT, "Parameter format"));
		}
		check = Operator.merge(tokens, 1, tokens.size(), Operator.FLOAT | Operator.BOOLEAN);
	}

	@Override
	public void parse(MethodVisitorProxy mv, int local, VariableManager vm) {
	}
}
