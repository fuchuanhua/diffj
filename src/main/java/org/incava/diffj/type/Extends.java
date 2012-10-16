package org.incava.diffj.type;

import net.sourceforge.pmd.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.ast.SimpleNode;
import org.incava.diffj.Messages;

/**
 * Compares extends.
 */
public class Extends extends Supers {
    public Extends(ASTClassOrInterfaceDeclaration decl) {
        super(decl);
    }

    protected Class<? extends SimpleNode> getPmdClass() {
        return net.sourceforge.pmd.ast.ASTExtendsList.class;
    }

    protected String getAddedMessage() {
        return Messages.EXTENDED_TYPE_ADDED;
    }

    protected String getChangedMessage() {
        return Messages.EXTENDED_TYPE_CHANGED;
    }

    protected String getRemovedMessage() {
        return Messages.EXTENDED_TYPE_REMOVED;
    }
}