package com.ruixus.test.smarty4j;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ruixus.test.smarty4j.function.AppendUnit;
import com.ruixus.test.smarty4j.function.AssignUnit;
import com.ruixus.test.smarty4j.function.BlockUnit;
import com.ruixus.test.smarty4j.function.BreakUnit;
import com.ruixus.test.smarty4j.function.BytesUnit;
import com.ruixus.test.smarty4j.function.CallFunctionUnit;
import com.ruixus.test.smarty4j.function.CaptureUnit;
import com.ruixus.test.smarty4j.function.ConfigLoadUnit;
import com.ruixus.test.smarty4j.function.ContinueUnit;
import com.ruixus.test.smarty4j.function.CounterUnit;
import com.ruixus.test.smarty4j.function.CycleUnit;
import com.ruixus.test.smarty4j.function.DebugUnit;
import com.ruixus.test.smarty4j.function.EvalUnit;
import com.ruixus.test.smarty4j.function.ForUnit;
import com.ruixus.test.smarty4j.function.ForeachUnit;
import com.ruixus.test.smarty4j.function.IfUnit;
import com.ruixus.test.smarty4j.function.IncludeUnit;
import com.ruixus.test.smarty4j.function.IterateUnit;
import com.ruixus.test.smarty4j.function.LdelimUnit;
import com.ruixus.test.smarty4j.function.LiteralUnit;
import com.ruixus.test.smarty4j.function.MacroUnit;
import com.ruixus.test.smarty4j.function.MathUnit;
import com.ruixus.test.smarty4j.function.RdelimUnit;
import com.ruixus.test.smarty4j.function.SectionUnit;
import com.ruixus.test.smarty4j.function.StripUnit;
import com.ruixus.test.smarty4j.function.WhileUnit;

//bytes,declare,call return,iterate/iterateelse

//{fetch}
//{html_checkboxes}
//{html_image}
//{html_options}
//{html_radios}
//{html_select_date}
//{html_select_time}
//{html_table}
//{mailto}
//{textformat}

@RunWith(Suite.class)
@Suite.SuiteClasses({
    AppendUnit.class,
    AssignUnit.class,
    BlockUnit.class,
    BreakUnit.class,
    BytesUnit.class,
    CallFunctionUnit.class,
    CaptureUnit.class,
    ConfigLoadUnit.class,
    ContinueUnit.class,
    CounterUnit.class,
    CycleUnit.class,
    DebugUnit.class,
    EvalUnit.class,
    ForeachUnit.class,
    ForUnit.class,
    IfUnit.class,
    IncludeUnit.class,
    IterateUnit.class,
    LdelimUnit.class,
    LiteralUnit.class,
    MacroUnit.class,
    MathUnit.class,
    RdelimUnit.class,
    SectionUnit.class,
    StripUnit.class,
    WhileUnit.class })
public class FunctionUnit {
}
