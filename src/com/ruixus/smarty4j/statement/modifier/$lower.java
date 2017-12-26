package com.ruixus.smarty4j.statement.modifier;

import static org.objectweb.asm.Opcodes.*;

import com.ruixus.smarty4j.VariableManager;
import com.ruixus.smarty4j.statement.Modifier;
import com.ruixus.util.MethodVisitorProxy;

/**
 * This is used to lowercase a variable.
 * 
 * <code>
 * <br>
 * <b>Example:</b><br>
 * context.set("articleTitle", "Two Convicts Evade Noose, Jury Hung.");<br>
 * <br>
 * <b>Template:</b><br>
 * {$articleTitle}<br>
 * {$articleTitle|lower}<br>
 * <br>
 * <b>Output:</b><br>
 * Two Convicts Evade Noose, Jury Hung.<br>
 * two convicts evade noose, jury hung.<br>
 * </code>
 * 
 * @version 1.1.0, 2015/05/16
 * @author Ouyang Xianwei
 * @since Smarty 1.1
 */
public class $lower extends Modifier {

	@Override
	public void parse(MethodVisitorProxy mv, int local, VariableManager vm) {
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "toLowerCase", "()Ljava/lang/String;");
	}
}