package org.jprotocol.framework.dsl.argiters;

import org.jprotocol.framework.dsl.IArgumentType;
import org.jprotocol.framework.dsl.IProtocolMessage;


public class FindEndIndexIter extends AbstractArgIter {
    public int endIndex;
    public FindEndIndexIter(IProtocolMessage p) {
        super(p);
        iterate();
    }

    @Override
    protected boolean iter(IArgumentType arg) {
        endIndex = Math.max(endIndex, arg.getEndByteIndex());
        return true;
    }
    
}
