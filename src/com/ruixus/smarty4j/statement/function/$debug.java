package com.ruixus.smarty4j.statement.function;

import com.ruixus.smarty4j.Analyzer;
import com.ruixus.smarty4j.Engine;
import com.ruixus.smarty4j.MethodVisitorProxy;
import com.ruixus.smarty4j.ParseException;
import com.ruixus.smarty4j.SafeContext;
import com.ruixus.smarty4j.TemplateWriter;
import com.ruixus.smarty4j.VariableManager;
import com.ruixus.smarty4j.statement.BlockFunction;
import com.ruixus.util.SimpleStack;

/**
 * The tag redirects the output to the console.
 * 
 * @version 1.1.0, 2015/05/16
 * @author Ouyang Xianwei
 * @since Smarty 1.1
 */
public class $debug extends BlockFunction {

	private Engine engine;
	
	public TemplateWriter start(SafeContext ctx, TemplateWriter writer) throws Exception {
		return new TemplateWriter(System.out, engine.getCharset());
	}
	
	@Override
	public void syntax(Analyzer analyzer, SimpleStack tokens) throws ParseException {
		super.syntax(analyzer, tokens);
		engine = analyzer.getTemplate().getEngine();
	}

	@Override
	public void parse(MethodVisitorProxy mv, int local, VariableManager vm) {
		if (engine.isDebug()) {
			super.parse(mv, local, vm);
		}
	}
}