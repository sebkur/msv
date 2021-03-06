package com.sun.msv.schematron.reader;

import com.sun.msv.reader.trex.RootMergedGrammarState;
import com.sun.msv.reader.State;
import com.sun.msv.util.StartTagInfo;
import com.sun.msv.schematron.grammar.SRule;

/**
 * @author Kohsuke Kawaguchi
 */
public class SRootMergedGrammarState extends RootMergedGrammarState {
    protected State createChildState(StartTagInfo tag) {
        if(!tag.namespaceURI.equals(SRELAXNGReader.SchematronURI))
            return super.createChildState(tag);

        if( tag.localName.equals("rule") )
            return new SRuleState();
        if( tag.localName.equals("pattern") )
            return new SPatternState();
        return null;
    }

    public void onRule(SRule rule) {
        // TODO: what would be the semantics?
    }
}
