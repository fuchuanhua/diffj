package org.incava.diffj.function;

import org.incava.diffj.*;

public class TestCtorAccess extends ItemsTest {
    public TestCtorAccess(String name) {
        super(name);
    }

    public void testAccessAdded() {
        evaluate(new Lines("class Test {",
                           "    Test() {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    public Test() {}",
                           "}"),
                 
                 makeAccessRef(null, "public", loc(2, 5), loc(2, 8), loc(3, 5), loc(3, 10)));
    }

    public void testAccessRemoved() {
        evaluate(new Lines("class Test {",
                           "    public Test() {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    Test() {}",
                           "}"),
                 
                 makeAccessRef("public", null, loc(2, 5), loc(2, 10), loc(3, 5), loc(3, 8)));
    }

    // public void xtestAccessRemoved() {
    //     Ref ref = removedAccess("public");
        
    //     evaluate(new Lines("class Test {",
    //                        "    " + str(ref, "public") + " Test() {}",
    //                        "",
    //                        "}"),

    //              new Lines("class Test {",
    //                        "",
    //                        "    " + str(ref) + "Test() {}",
    //                        "}"),
                 
    //              makeAccessRef("public", null, loc(2, 5), loc(2, 10), loc(3, 5), loc(3, 8)));
    // }

    public void testAccessChanged() {
        evaluate(new Lines("class Test {",
                           "    private Test() {}",
                           "",
                           "}"),

                 new Lines("class Test {",
                           "",
                           "    public Test() {}",
                           "}"),
                 
                 makeAccessRef("private", "public", loc(2, 5), loc(3, 5)));
    }
}
