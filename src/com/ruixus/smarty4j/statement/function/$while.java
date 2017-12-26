package com.ruixus.smarty4j.statement.function;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.Label;

import com.ruixus.smarty4j.Analyzer;
import com.ruixus.smarty4j.MessageFormat;
import com.ruixus.smarty4j.Operator;
import com.ruixus.smarty4j.ParseException;
import com.ruixus.smarty4j.VariableManager;
import com.ruixus.smarty4j.expression.Expression;
import com.ruixus.smarty4j.statement.LoopFunction;
import com.ruixus.util.MethodVisitorProxy;
import com.ruixus.util.SimpleStack;

/**
 * The tag loops in Smarty have much the same flexibility as PHP while statements, with a few added
 * features for the template engine. Every {while} must be paired with a matching {/while}.
 * 
 * @version 1.1.0, 2015/05/16
 * @author Ouyang Xianwei
 * @since Smarty 1.1
 */
public class $while extends LoopFunction {

	/** 循环结构块中的条件表达式 */
	private Expression check;

	@Override
	public void restore(MethodVisitorProxy mv, VariableManager vm) {
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

		/* 循环语句块开始位置 */
		Label block = new Label();

		mv.visitLabel(lblContinue);
		check.parseCheck(mv, local, vm, block, lblBreak);
		mv.visitJumpInsn(IFEQ, lblBreak);

		mv.visitLabel(block);

		super.parse(mv, local, vm);
		mv.visitJumpInsn(GOTO, lblContinue);

		mv.visitLabel(lblBreak);
	}
}