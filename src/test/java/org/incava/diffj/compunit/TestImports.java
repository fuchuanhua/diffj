package org.incava.diffj.compunit;

import java.io.StringWriter;
import org.incava.analysis.FileDiffAdd;
import org.incava.analysis.FileDiffDelete;
import org.incava.diffj.*;
import org.incava.ijdk.lang.StringExt;
import org.incava.java.Java;
import static org.incava.diffj.compunit.Imports.*;

public class TestImports extends ItemsTest {
    protected final static String[] IMPORT_SECTION_MSGS = new String[] {
        Imports.IMPORT_SECTION_REMOVED,
        null, 
        Imports.IMPORT_SECTION_ADDED,
    };

    public TestImports(String name) {
        super(name);
    }

    public void testImportsNoneNoChange() {
        evaluate(new Lines("class Test {",
                           "}"),
                           
                 new Lines("class Test {",
                           "}"),

                 NO_CHANGES);
    }

    public void testImportsOneNoChange() {
        evaluate(new Lines("import java.foo.*;",
                           "",
                           "class Test {",
                           "",
                           "}"),

                 new Lines("import java.foo.*;",
                           "class Test {",
                           "}"),

                 NO_CHANGES);
    }

    public void testImportsTwoNoChange() {
        evaluate(new Lines("import java.foo.*;",
                           "import org.incava.Bazr;",
                           "",
                           "class Test {",
                           "",
                           "}"),

                 new Lines("import java.foo.*;",
                           "import org.incava.Bazr;",
                           "class Test {",
                           "}"),

                 NO_CHANGES);
    }

    public void testImportsSectionRemovedOne() {
        evaluate(new Lines("import java.foo.*;",
                           "",
                           "class Test {",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "}"),
                 
                 new FileDiffDelete(IMPORT_SECTION_REMOVED, locrg(1, 1, 1, 18), locrg(1, 1, 1, 5)));
    }

    public void testImportsSectionRemovedTwo() {
        evaluate(new Lines("import java.foo.*;",
                           "import org.incava.Bazr;",
                           "",
                           "class Test {",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "}"),
                 
                 new FileDiffDelete(IMPORT_SECTION_REMOVED, locrg(1, 1, 2, 23), locrg(1, 1, 1, 5)));
    }

    public void testImportsSectionAddedOne() {
        evaluate(new Lines("class Test {",
                           "",
                           "}"),

                 new Lines("import java.foo.*;",
                           "",
                           "class Test {",
                           "}"),
                 
                 new FileDiffAdd(IMPORT_SECTION_ADDED, locrg(1, 1, 1, 5), locrg(1, 1, 1, 18)));
    }

    public void testImportsSectionAddedTwo() {
        evaluate(new Lines("class Test {",
                           "",
                           "}"),

                 new Lines("import java.foo.*;",
                           "import org.incava.Bazr;",
                           "",
                           "class Test {",
                           "}"),
                 
                 new FileDiffAdd(IMPORT_SECTION_ADDED, locrg(1, 1, 1, 5), locrg(1, 1, 2, 23)));
    }

    public void testImportsBlockAddedNoClassDefined() {
        StringWriter writer = new StringWriter();
        evaluate(new Lines("package org.incava.foo;",
                           ""),

                 new Lines("package org.incava.foo;",
                           "",
                           "import java.foo.*;",
                           "import org.incava.Bazr;",
                           ""),

                 Java.SOURCE_1_3,
                 makeDetailedReport(writer),
                 new FileDiffAdd(IMPORT_SECTION_ADDED, locrg(1, 1, 1, 7), locrg(3, 1, 4, 23)));
        
        tr.Ace.setVerbose(true);
        tr.Ace.red("*******************************************************");

        String[] lines = StringExt.split(writer.getBuffer().toString(), "\n");
        System.out.println("lines: " + lines);

        tr.Ace.log("lines", lines);
    }

    //$$$ todo: add tests for IMPORT_REMOVED, IMPORT_ADDED ... how did I miss this?
}
