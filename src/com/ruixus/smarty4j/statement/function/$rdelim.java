package com.ruixus.smarty4j.statement.function;

import static org.objectweb.asm.Opcodes.*;

import com.ruixus.smarty4j.Engine;
import com.ruixus.smarty4j.MethodVisitorProxy;
import com.ruixus.smarty4j.VariableManager;
import com.ruixus.smarty4j.statement.Function;

/**
 * The tag is used for escaping template right-delimiters.
 * 
 * @version 1.1.0, 2015/05/16
 * @author Ouyang Xianwei
 * @since Smarty 1.1
 */
public class $rdelim extends Function {

	@Override
	public void parse(MethodVisitorProxy mv, int local, VariableManager vm) {
		mv.visitVarInsn(ALOAD, WRITER);
		mv.visitVarInsn(ALOAD, ENGINE);
		mv.visitMethodInsn(INVOKEVIRTUAL, Engine.NAME, "getRightDelimiter", "()Ljava/lang/String;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/Writer", "write", "(Ljava/lang/String;)V");
	}
}