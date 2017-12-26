package com.ruixus.smarty4j.expression;

import com.ruixus.smarty4j.Node;
import com.ruixus.smarty4j.VariableManager;
import com.ruixus.smarty4j.statement.Modifier;
import com.ruixus.util.MethodVisitorProxy;

/**
 * 变量调节器扩展节点
 * 
 * @version 1.1.0, 2015/05/16
 * @author Ouyang Xianwei
 * @since Smarty 1.1
 */
public class ModifierExtended extends Node {

	/** 变量调节器 */
	private Modifier modifier;

	/**
	 * 创建变量调节器扩展节点
	 * 
	 * @param modifier
	 *          变量调节器
	 */
	public ModifierExtended(Modifier modifier) {
		this.modifier = modifier;
	}

	@Override
	public void parse(MethodVisitorProxy mv, int local, VariableManager vm) {
		modifier.parse(mv, local, vm);
	}
}