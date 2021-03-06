package org.incava.diffj.code;

import java.text.MessageFormat;
import java.util.List;
import net.sourceforge.pmd.ast.Token;
import org.incava.analysis.FileDiff;
import org.incava.analysis.FileDiffChange;
import org.incava.analysis.FileDiffCodeAdded;
import org.incava.analysis.FileDiffCodeDeleted;
import org.incava.diffj.element.Differences;
import org.incava.ijdk.text.LocationRange;
import org.incava.ijdk.util.diff.Diff;
import org.incava.ijdk.util.diff.Difference;

public class Code {    
    public static final String CODE_CHANGED = "code changed in {0}";
    public static final String CODE_ADDED = "code added in {0}";
    public static final String CODE_REMOVED = "code removed in {0}";

    private final String name;
    private final TokenList tokenList;

    public Code(String name, List<Token> tokens) {
        this.name = name;
        this.tokenList = new TokenList(tokens);
    }

    public void diff(Code toCode, Differences differences) {
        TokenList toTokenList = toCode.tokenList;
        Diff<Token> tokenDiff = tokenList.diff(toTokenList);
        
        FileDiff currFileDiff = null;
        List<Difference> diffList = tokenDiff.execute();

        for (Difference diff : diffList) {
            currFileDiff = processDifference(diff, toTokenList, currFileDiff, differences);
            if (currFileDiff == null) {
                break;
            }
        }
    }

    protected FileDiff replaceReference(FileDiff fileDiff, LocationRange fromLocRg, LocationRange toLocRg, Differences differences) {
        String newMsg = MessageFormat.format(CODE_CHANGED, name);
        FileDiff newDiff = new FileDiffChange(newMsg, fileDiff, fromLocRg, toLocRg);
        differences.getFileDiffs().remove(fileDiff);
        return addFileDiff(newDiff, differences);
    }

    protected FileDiff addFileDiff(FileDiff fileDiff, Differences differences) {
        differences.add(fileDiff);
        return fileDiff;
    }

    protected FileDiff addReference(String msg, LocationRange fromLocRg, LocationRange toLocRg, Differences differences) {
        String str = MessageFormat.format(msg, name);

        if (msg.equals(CODE_ADDED)) {
            // this will show as add when highlighted, as change when not.
            return addFileDiff(new FileDiffCodeAdded(str, fromLocRg, toLocRg), differences);
        }
        else if (msg.equals(CODE_REMOVED)) {
            return addFileDiff(new FileDiffCodeDeleted(str, fromLocRg, toLocRg), differences);
        }
        else {
            return addFileDiff(new FileDiffChange(str, fromLocRg, toLocRg), differences);
        }
    }
    
    protected FileDiff processDifference(Difference diff, TokenList toTokenList, FileDiff currFileDiff, Differences differences) {
        int delStart = diff.getDeletedStart();
        int delEnd   = diff.getDeletedEnd();
        int addStart = diff.getAddedStart();
        int addEnd   = diff.getAddedEnd();

        if (delEnd == Difference.NONE && addEnd == Difference.NONE) {
            // WTF?
            return null;
        }

        LocationRange fromLocRg = tokenList.getLocationRange(delStart, delEnd);
        LocationRange toLocRg = toTokenList.getLocationRange(addStart, addEnd);

        if (currFileDiff != null && currFileDiff.isOnSameLine(fromLocRg)) {
            return replaceReference(currFileDiff, fromLocRg, toLocRg, differences);
        }
        else {
            String msg = delEnd == Difference.NONE ? CODE_ADDED : (addEnd == Difference.NONE ? CODE_REMOVED : CODE_CHANGED);
            return addReference(msg, fromLocRg, toLocRg, differences);
        }
    }
}
