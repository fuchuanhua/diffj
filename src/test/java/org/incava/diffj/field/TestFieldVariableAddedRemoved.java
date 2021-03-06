package org.incava.diffj.field;

import org.incava.analysis.FileDiff;
import org.incava.analysis.FileDiffChange;
import org.incava.diffj.*;
import org.incava.java.Java;
import static org.incava.diffj.field.Variable.*;
import static org.incava.diffj.field.Variables.*;

public class TestFieldVariableAddedRemoved extends ItemsTest {
    protected final static String[] VARIABLE_MSGS = new String[] {
        VARIABLE_REMOVED,
        VARIABLE_CHANGED, 
        VARIABLE_ADDED,
    };

    public TestFieldVariableAddedRemoved(String name) {
        super(name);
    }
    
    public FileDiff makeVariableTypeChangedRef(String varName, String fromType, String toType, 
                                               int fromLine, int fromCol, // this assumes that type doesn't span lines
                                               int toLine,   int toCol) {
        return makeVariableTypeChangedRef(varName, fromType, toType,
                                          fromLine, fromCol, fromCol + fromType.length() - 1,
                                          toLine,   toCol,   toCol   + toType.length() - 1);
    }
    
    public FileDiff makeVariableTypeChangedRef(String varName, String fromType, String toType, 
                                               int fromLine, int fromFromCol, int fromToCol, // this assumes that type doesn't span lines
                                               int toLine,   int toFromCol,   int toToCol) {
        return new FileDiffChange(getFromToMessage(VARIABLE_TYPE_CHANGED, varName, fromType, toType),
                                  loc(fromLine, fromFromCol), loc(fromLine, fromToCol), 
                                  loc(toLine,   toFromCol),   loc(toLine, toToCol));
    }

    public FileDiff makeVariableRemovedRef(String varName, 
                                           int fromLine, int fromCol, 
                                           int toLine,   int toCol) {
        return makeCodeChangedRef(VARIABLE_REMOVED, varName,  
                                  loc(fromLine, fromCol), loc(fromLine, fromCol, varName), 
                                  loc(toLine, toCol),     loc(toLine,   toCol,   varName));
    }
    
    public void testVariableAddedRemoved() {
        evaluate(new Lines("public class Collections {",
                           "",
                           "    private static class SingletonMap",
                           "                                      implements Serializable {",
                           "        private final Object k, v;",
                           "    }",
                           "}"),

                 new Lines("public class Collections {",
                           "",
                           "    private static class SingletonMap<K,V>",
                           "	  implements Serializable {",
                           "",
                           "        private final K k;",
                           "        private final V v;",
                           "    }",
                           "}"),

                 Java.SOURCE_1_5,
                 
                 // wrong!
                 // these should be:
                 
                 // variable type changed for k from Object to K
                 // variable type changed for v from Object to V
                 
                 makeVariableTypeChangedRef("k", "Object", "K", 5, 23, 6, 23),
                 makeVariableTypeChangedRef("v", "Object", "V", 5, 23, 7, 23),
                 
                 makeVariableRemovedRef("k", 5, 30, 7, 25),
                 makeVariableRemovedRef("v", 5, 33, 6, 25));
    }
}
