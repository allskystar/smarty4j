package com.ruixus.smarty4j.statement.function;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import com.ruixus.smarty4j.Analyzer;
import com.ruixus.smarty4j.Engine;
import com.ruixus.smarty4j.MessageFormat;
import com.ruixus.smarty4j.ParseException;
import com.ruixus.smarty4j.SafeContext;
import com.ruixus.smarty4j.Template;
import com.ruixus.smarty4j.TemplateWriter;
import com.ruixus.smarty4j.expression.StringExpression;
import com.ruixus.smarty4j.expression.number.ConstInteger;
import com.ruixus.smarty4j.statement.Definition;
import com.ruixus.smarty4j.statement.Definition.Type;
import com.ruixus.smarty4j.statement.LineFunction;
import com.ruixus.smarty4j.util.SimpleStack;

/**
 * This is used for loading config #variables# from a configuration file into the template.
 * 
 * <table border="1">
 * <colgroup> <col align="center" class="param"> <col align="center" class="type"> <col
 * align="center" class="required"> <col align="center" class="default"> <col class="desc">
 * </colgroup> <thead>
 * <tr>
 * <th align="center">Attribute Name</th>
 * <th align="center">Type</th>
 * <th align="center">Required</th>
 * <th align="center">Default</th>
 * <th>Description</th>
 * </tr>
 * </thead> <tbody>
 * <tr>
 * <td align="center">file</td>
 * <td align="center">string</td>
 * <td align="center">Yes</td>
 * <td align="center"><span class="emphasis"><em>n/a</em></span></td>
 * <td>The name of the config file to include</td>
 * </tr>
 * <tr>
 * <td align="center">scope</td>
 * <td align="center">string</td>
 * <td align="center">no</td>
 * <td align="center"><span class="emphasis"><em>local</em></span></td>
 * <td>
 * How the scope of the loaded variables are treated, which must be one of local, parent or global.
 * local means variables are loaded into the local template context. parent means variables are
 * loaded into both the local context and the parent template that called it. global means variables
 * are available to all templates.</td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * @version 1.1.0, 2015/05/16
 * @author Ouyang Xianwei
 * @since Smarty 1.1
 */
public class $config_load extends LineFunction {

	/** 参数定义 */
	private static final Definition[] definitions = {
	    Definition.forFunction("file", Type.STRING),
	    Definition.forFunction("scope", Type.STRING, new StringExpression("local"), "I") };

	public Object execute(SafeContext ctx, TemplateWriter writer, String file, int scope) throws IOException {
		Template tpl = ctx.getTemplate();
		FileInputStream in = new FileInputStream(tpl.getEngine().getTemplatePath()
		    + tpl.getRelativePath(file));
		Properties prop = new Properties();
		try {
			prop.load(in);
		} finally {
			in.close();
		}

		// 取得父容器的配置信息
		Engine engine = tpl.getEngine();
		Map<String, Object> config = ctx.getConfig();
		SafeContext pctx = ctx.getParent();
		Map<String, Object> parent;
		if (pctx != null) {
			parent = pctx.getConfig();
		} else {
			parent = null;
		}

		// 根据配置加载的级别, 设置配置的内容范围
		for (Enumeration<?> i = prop.propertyNames(); i.hasMoreElements();) {
			String key = i.nextElement().toString();
			String value = prop.getProperty(key);
			switch (scope) {
			case 0:
				engine.addConfig(key, value);
			case 1:
				if (parent != null) {
					parent.put(key, value);
				}
			default:
				config.put(key, value);
			}
		}
		return null;
	}

	@Override
	public void syntax(Analyzer analyzer, SimpleStack tokens) throws ParseException {
		super.syntax(analyzer, tokens);
		String scope = PARAMETERS[1].toString();
		if (scope.equals("local")) {
			PARAMETERS[1] = new ConstInteger(2);
		} else if (scope.equals("parent")) {
			PARAMETERS[1] = new ConstInteger(1);
		} else if (scope.equals("global")) {
			PARAMETERS[1] = new ConstInteger(0);
		} else {
			throw new ParseException(String.format(MessageFormat.CANNOT_BE_RESOLVED_TO, "scope",
			    "either global, parent or local"));
		}
	}

	@Override
	public Definition[] getDefinitions() {
		return definitions;
	}
}