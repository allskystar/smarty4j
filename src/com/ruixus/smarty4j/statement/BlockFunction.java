package com.ruixus.smarty4j.statement;

import static org.objectweb.asm.Opcodes.*;

import com.ruixus.smarty4j.Analyzer;
import com.ruixus.smarty4j.MethodVisitorProxy;
import com.ruixus.smarty4j.SafeContext;
import com.ruixus.smarty4j.TemplateReader;
import com.ruixus.smarty4j.TemplateWriter;
import com.ruixus.smarty4j.VariableManager;
import com.ruixus.smarty4j.util.NullWriter;

/**
 * 自定义区块函数，区块函数指的是函数内部包含其它函数或文本，所以必须拥有结束标签的函数， 区块函数在编译过程中将会在解析内部函数或文本之前调用start方法，
 * 在解析完内部函数或文本之后将调用end方法，在模板分析过程中，系统首先初始化函数节点， 然后解析函数的参数，然后设置函数的父函数，最后解析函数的内部数据。
 * 如果不希望进行jvm字节码开发，开发人员应该继承自这个类来实现自己的区块函数扩展节点。 如果需要向Context中写入数据，请参见Function的函数说明。
 * 
 * @see com.ruixus.smarty4j.statement.Function
 * 
 * @version 1.1.0, 2015/05/16
 * @author Ouyang Xianwei
 * @since Smarty 1.1
 */
public abstract class BlockFunction extends Block {

	/** 空输出 */
	protected static final TemplateWriter NULL = new TemplateWriter(new NullWriter());

	/** 函数在模板中的编号 */
	private int index;

	@Override
	public void analyzeContent(Analyzer analyzer, TemplateReader reader) {
		super.analyzeContent(analyzer, reader);
		this.index = analyzer.getTemplate().addNode(this);
	}

	@Override
	public void parse(MethodVisitorProxy mv, int local, VariableManager vm) {
		mv.visitVarInsn(ALOAD, WRITER);

		String className = parseNode(mv, local, index);
		mv.visitVarInsn(ALOAD, CONTEXT);
		mv.visitInsn(DUP2);
		mv.visitVarInsn(ALOAD, WRITER);
		parseAllParameters(mv, local, vm);
		mv.visitMethodInsn(INVOKEVIRTUAL, className, "start", "(L" + SafeContext.NAME + ";L"
		    + TemplateWriter.NAME + ';' + getDesc() + ")L" + TemplateWriter.NAME + ";");

		mv.visitVarInsn(ALOAD, WRITER);
		mv.visitInsn(SWAP);
		mv.visitVarInsn(ASTORE, WRITER);

		super.parse(mv, local, vm);

		mv.visitVarInsn(ALOAD, WRITER);
		mv.visitMethodInsn(INVOKEVIRTUAL, className, "end", "(L" + SafeContext.NAME + ";L"
		    + TemplateWriter.NAME + ";L" + TemplateWriter.NAME + ";)V");

		mv.visitVarInsn(ASTORE, WRITER);
	}
}