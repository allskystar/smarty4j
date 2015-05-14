package com.ruixus.test.smarty4j;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ruixus.test.smarty4j.modifier.CapitalizeUnit;
import com.ruixus.test.smarty4j.modifier.CatUnit;
import com.ruixus.test.smarty4j.modifier.CountCharactersUnit;
import com.ruixus.test.smarty4j.modifier.CountParagraphsUnit;
import com.ruixus.test.smarty4j.modifier.CountSentencesUnit;
import com.ruixus.test.smarty4j.modifier.DateFormatUnit;
import com.ruixus.test.smarty4j.modifier.DefaultUnit;
import com.ruixus.test.smarty4j.modifier.EscapeUnit;
import com.ruixus.test.smarty4j.modifier.IndentUnit;
import com.ruixus.test.smarty4j.modifier.LowerUnit;
import com.ruixus.test.smarty4j.modifier.NL2BRUnit;
import com.ruixus.test.smarty4j.modifier.RegexReplaceUnit;
import com.ruixus.test.smarty4j.modifier.ReplaceUnit;
import com.ruixus.test.smarty4j.modifier.SpacifyUnit;
import com.ruixus.test.smarty4j.modifier.StringFormatUnit;
import com.ruixus.test.smarty4j.modifier.StripTagsUnit;
import com.ruixus.test.smarty4j.modifier.StripUnit;
import com.ruixus.test.smarty4j.modifier.TruncateUnit;
import com.ruixus.test.smarty4j.modifier.UnescapeUnit;
import com.ruixus.test.smarty4j.modifier.UpperUnit;
import com.ruixus.test.smarty4j.modifier.WordwrapUnit;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    CapitalizeUnit.class,
    CatUnit.class,
    CountCharactersUnit.class,
    CountParagraphsUnit.class,
    CountSentencesUnit.class,
    DateFormatUnit.class,
    DefaultUnit.class,
    EscapeUnit.class,
    IndentUnit.class,
    LowerUnit.class,
    NL2BRUnit.class,
    RegexReplaceUnit.class,
    ReplaceUnit.class,
    SpacifyUnit.class,
    StringFormatUnit.class,
    StripTagsUnit.class,
    StripUnit.class,
    TruncateUnit.class,
    UnescapeUnit.class,
    UpperUnit.class,
    WordwrapUnit.class })
public class ModifierUnit {
}
