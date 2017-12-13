package com.ruixus.test.smarty4j.syntax;

import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.ruixus.smarty4j.Context;
import com.ruixus.smarty4j.Engine;
import com.ruixus.smarty4j.Node;
import com.ruixus.smarty4j.Operator;
import com.ruixus.smarty4j.Template;
import com.ruixus.smarty4j.VariableManager;
import com.ruixus.smarty4j.expression.check.CheckAdapter;
import com.ruixus.smarty4j.expression.number.ConstInteger;
import com.ruixus.smarty4j.expression.number.DoubleAdapter;
import com.ruixus.smarty4j.statement.PrintStatement;
import com.ruixus.smarty4j.util.SimpleStack;

public class CoverageUnit {

	private Engine engine = new Engine();

	public String merge(Node node) throws Exception {
		VariableManager vm = new VariableManager(engine);
		Template tpl = new Template(engine, null, node, vm);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		tpl.merge(new Context(), bos);
		return new String(bos.toByteArray());
	}

	@Test
	public void i2d() throws Exception {
		//实现全覆盖
		SimpleStack tokens = new SimpleStack();
		tokens.push(new ConstInteger(1));
		tokens.push(Operator.ADD);
		tokens.push(new ConstInteger(1));

		Assert.assertEquals("2.0", merge(new PrintStatement(new DoubleAdapter(Operator.merge(tokens, 0, 3)))));
	}

	@Test
	public void i2b() throws Exception {
		//实现全覆盖
		SimpleStack tokens = new SimpleStack();
		tokens.push(new ConstInteger(1));
		tokens.push(Operator.ADD);
		tokens.push(new ConstInteger(1));

		Assert.assertEquals("true", merge(new PrintStatement(new CheckAdapter(Operator.merge(tokens, 0, 3)))));
	}
}
