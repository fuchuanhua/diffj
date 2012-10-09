package org.incava.diffj;

import net.sourceforge.pmd.ast.ASTMethodDeclaration;
import org.incava.analysis.FileDiffs;
import org.incava.pmdx.MethodUtil;
import org.incava.pmdx.SimpleNodeUtil;

public class Methods extends Items<ASTMethodDeclaration> {
    private final MethodUtil methodUtil;

    public Methods(FileDiffs fileDiffs) {
        super(fileDiffs, "net.sourceforge.pmd.ast.ASTMethodDeclaration");
        methodUtil = new MethodUtil();
    }    

    public void doCompare(ASTMethodDeclaration fromMethod, ASTMethodDeclaration toMethod) {
        Method method = new Method(fromMethod);
        method.compareAccess(SimpleNodeUtil.getParent(fromMethod), SimpleNodeUtil.getParent(toMethod), differences);
        method.diff(toMethod, differences);
    }

    public double getScore(ASTMethodDeclaration a, ASTMethodDeclaration b) {
        return MethodUtil.getMatchScore(a, b);
    }

    public String getName(ASTMethodDeclaration md) {
        return MethodUtil.getFullName(md);
    }

    public String getAddedMessage(ASTMethodDeclaration md) {
        return Messages.METHOD_ADDED;
    }

    public String getRemovedMessage(ASTMethodDeclaration md) {
        return Messages.METHOD_REMOVED;
    }
}