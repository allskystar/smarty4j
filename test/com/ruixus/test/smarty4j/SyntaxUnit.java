package com.ruixus.test.smarty4j;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ruixus.test.smarty4j.syntax.CommentUnit;
import com.ruixus.test.smarty4j.syntax.ExpressionUnit;
import com.ruixus.test.smarty4j.syntax.VariableUnit;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    CommentUnit.class,
    ExpressionUnit.class,
    VariableUnit.class })
public class SyntaxUnit {
}