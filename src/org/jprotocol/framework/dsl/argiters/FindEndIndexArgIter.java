package org.jprotocol.framework.dsl.argiters;

import org.jprotocol.framework.dsl.IArgumentType;
import org.jprotocol.framework.dsl.IProtocolLayoutType;


public class FindEndIndexArgIter extends ArgTypeIter {
    private IArgumentType foundArg;
    public FindEndIndexArgIter(IProtocolLayoutType type) {
        super(type);
        if (type.getArguments().length > 0) {
            foundArg = type.getArguments()[0];
            iterate();
        }
    }

    @Override
    protected boolean iter(IArgumentType arg) {
        if (arg.getOffset() + arg.getSizeInBits() > foundArg.getOffset() + foundArg.getSizeInBits()) {
            foundArg = arg;
        }
        return true;
    }
    
    public IArgumentType getFoundArg() {
        return foundArg;
    }
}
