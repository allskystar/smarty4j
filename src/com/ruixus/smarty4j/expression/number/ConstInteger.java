package com.ruixus.smarty4j.expression.number;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.Label;

import com.ruixus.smarty4j.MethodVisitorProxy;
import com.ruixus.smarty4j.VariableManager;

/**
 * 整数常数表达式节点, 向JVM语句栈内放入一个整数常量值
 * 
 * @version 1.1.0, 2015/05/16
 * @author Ouyang Xianwei
 * @since Smarty 1.1
 */
public class ConstInteger extends IntegerExpression {

	public static final ConstInteger ZERO = new ConstInteger(0);  
	public static final ConstInteger M1 = new ConstInteger(-1);  
	
	/** 常量值 */
	private int value = 0;

	/**
	 * 创建整数常数表达式节点
	 * 
	 * @param value
	 *          常量值
	 */
	public ConstInteger(int value) {
		this.value = value;
	}

	/**
	 * 获取常量值
	 * 
	 * @return 值
	 */
	public int getValue() {
		return value;
	}

	/**
	 * 设置成相反数
	 */
	public void inverse() {
		value = -value;
	}

	@Override
	public void parseCheck(MethodVisitorProxy mv, int local, VariableManager vm, Label lblTrue,
	    Label lblFalse) {
		if (value == 0) {
			if (lblFalse == null) {
				mv.visitLdcInsn(false);
			} else {
				mv.visitJumpInsn(GOTO, lblFalse);
			}
		} else {
			if (lblTrue == null) {
				mv.visitLdcInsn(true);
			} else {
				mv.visitJumpInsn(GOTO, lblTrue);
			}
		}
	}

	@Override
	public void parseInteger(MethodVisitorProxy mv, int local, VariableManager vm) {
		mv.visitLdcInsn(value);
	}

	@Override
	public void parseDouble(MethodVisitorProxy mv, int local, VariableManager vm) {
		mv.visitLdcInsn((double) value);
	}

	@Override
	public void parseString(MethodVisitorProxy mv, int local, VariableManager vm) {
		mv.visitLdcInsn(Integer.toString(value));
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}
}