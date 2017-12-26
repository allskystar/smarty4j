package com.ruixus.smarty4j.statement.function;

import com.ruixus.smarty4j.Analyzer;
import com.ruixus.smarty4j.MethodVisitorProxy;
import com.ruixus.smarty4j.ParseException;
import com.ruixus.smarty4j.VariableManager;
import com.ruixus.smarty4j.statement.Block;
import com.ruixus.smarty4j.statement.Definition;
import com.ruixus.smarty4j.statement.Definition.Type;
import com.ruixus.util.SimpleStack;
import com.ruixus.smarty4j.statement.Function;

/**
 * @version 1.1.0, 2015/05/16
 * @author Ouyang Xianwei
 * @since Smarty 1.1
 */
public class $declare extends Function {

	private static final Definition[] definitions = {
	    Definition.forFunction("var", Type.STRING),
	    Definition.forFunction("class", Type.STRING) };

	@Override
	public boolean setParent(Block parent) {
		return false;
	}

	@Override
	public void syntax(Analyzer analyzer, SimpleStack tokens) throws ParseException {
		super.syntax(analyzer, tokens);
		analyzer.setDeclared(PARAMETERS[0].toString(), PARAMETERS[1].toString());
	}

	@Override
	public Definition[] getDefinitions() {
		return definitions;
	}
	
	@Override
	public void parse(MethodVisitorProxy mv, int local, VariableManager vm) {
	}
}
