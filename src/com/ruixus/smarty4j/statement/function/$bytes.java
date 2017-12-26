package com.ruixus.smarty4j.statement.function;

import static org.objectweb.asm.Opcodes.*;

import com.ruixus.smarty4j.Analyzer;
import com.ruixus.smarty4j.MessageFormat;
import com.ruixus.smarty4j.MethodVisitorProxy;
import com.ruixus.smarty4j.ParseException;
import com.ruixus.smarty4j.TemplateWriter;
import com.ruixus.smarty4j.VariableManager;
import com.ruixus.smarty4j.expression.Expression;
import com.ruixus.smarty4j.expression.VariableExpression;
import com.ruixus.smarty4j.statement.Function;
import com.ruixus.util.SimpleStack;

/**
 * 用于输出指定的字节数组。
 * 如果要使用它，在merge时必须使用OutputStream作为输出，而不是Writer。
 * 
 * <b>Syntax:</b>
 * 
 * <pre>
 * {bytes [Object(byte[])]}
 * </pre>
 * 
 * <b>Example:</b>
 * 
 * <pre>
 * {bytes $array}
 * </pre>
 * 
 * @see com.ruixus.smarty4j.Template#merge(com.ruixus.smarty4j.Context,
 *      java.io.OutputStream)
 * 
 * @version 1.1.0, 2015/05/16
 * @author Ouyang Xianwei
 * @since Smarty 1.1
 */
public class $bytes extends Function {

	/** 需要输出的表达式 */
	private Expression exp;

	@Override
	public void syntax(Analyzer analyzer, SimpleStack tokens) throws ParseException {
		Object value = tokens.get(1);
		if ((tokens.size() != 2) || !(value instanceof VariableExpression)) {
			throw new ParseException(String.format(MessageFormat.NOT_CORRECT, "Parameter format"));
		}
		exp = (Expression) value;
	}

	@Override
	public void parse(MethodVisitorProxy mv, int local, VariableManager vm) {

		/**
		 * out.write((byte[]) exp);
		 */
		mv.visitVarInsn(ALOAD, WRITER);
		mv.visitLdcInsn(null);
		exp.parse(mv, local, vm);
		mv.visitTypeInsn(CHECKCAST, "[B");
		mv.visitMethodInsn(INVOKEVIRTUAL, TemplateWriter.NAME, "write", "(Ljava/lang/String;[B)V");
	}
}