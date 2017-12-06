package com.ruixus.smarty4j.statement.modifier;

import java.util.List;
import java.util.regex.Pattern;

import com.ruixus.smarty4j.ParseException;
import com.ruixus.smarty4j.Template;
import com.ruixus.smarty4j.expression.Expression;
import com.ruixus.smarty4j.expression.VoidExpression;
import com.ruixus.smarty4j.statement.Definition;
import com.ruixus.smarty4j.statement.Definition.Type;
import com.ruixus.smarty4j.statement.Modifier;

/**
 * A regular expression search and replace on a variable.
 * 
 * <table border="1">
 * <colgroup> <col align="center" class="param"> <col align="center" class="type"> <col
 * align="center" class="required"> <col align="center" class="default"> <col class="desc">
 * </colgroup> <thead>
 * <tr>
 * <th align="center">Parameter Position</th>
 * <th align="center">Type</th>
 * <th align="center">Required</th>
 * <th align="center">Default</th>
 * <th>Description</th>
 * </tr>
 * </thead> <tbody>
 * <tr>
 * <td align="center">1</td>
 * <td align="center">string</td>
 * <td align="center">Yes</td>
 * <td align="center"><span class="emphasis"><em>n/a</em></span></td>
 * <td>This is the regular expression to be replaced.</td>
 * </tr>
 * <tr>
 * <td align="center">2</td>
 * <td align="center">string</td>
 * <td align="center">Yes</td>
 * <td align="center"><span class="emphasis"><em>n/a</em></span></td>
 * <td>This is the string of text to replace with.</td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * <code>
 * <br>
 * <b>Example:</b><br>
 * context.set("articleTitle", "Infertility unlikely to\nbe passed on, experts say.");<br>
 * <br>
 * <b>Template:</b><br>
 * {$articleTitle}<br>
 * {$articleTitle|regex_replace:'[\r\t\n]':' '}<br>
 * <br>
 * <b>Output:</b><br>
 * Infertility unlikely to<br>
 * be passed on, experts say.<br>
 * Infertility unlikely to be passed on, experts say.<br>
 * </code>
 * 
 * @version 1.1.0, 2015/05/16
 * @author Ouyang Xianwei
 * @since Smarty 1.1
 */
public class $regex_replace extends Modifier {

	/** 参数定义 */
	private static final Definition[] definitions = {
	    Definition.forModifier(Type.STRING, null, ""),
	    Definition.forModifier(Type.STROBJ) };

	/** 正则转换的匹配规则 */
	private Pattern rule;

	public Object execute(Object obj, String value) {
		return rule.matcher(obj.toString()).replaceAll(value);
	}

	@Override
	public void createParameters(Template tpl, List<Expression> values) throws ParseException {
		super.createParameters(tpl, values);
		rule = Pattern.compile(PARAMETERS[0].toString());
		PARAMETERS[0] = VoidExpression.VALUE;
	}

	@Override
	public Definition[] getDefinitions() {
		return definitions;
	}
}