package org.jprotocol.framework.dsl.argiters;

import org.jprotocol.framework.dsl.IArgumentType;


public class ArgTypeArrayIter extends AbstractArgTypeIter {
    private final IArgumentType[] args;
    private IArgumentType greatestFoundArg;
    private IArgumentType smallestFoundArg;

    public ArgTypeArrayIter(IArgumentType[] args) {
        this.args = args;
        if (args.length > 0) {
            greatestFoundArg = args[0];
            smallestFoundArg = args[0];
            iterate();
        }
    }
    @Override
    protected IArgumentType[] getArgTypes() {
        return args;
    }

    @Override
    protected boolean iter(IArgumentType arg) {
        if (arg.getOffset() + arg.getSizeInBits() > greatestFoundArg.getOffset() + greatestFoundArg.getSizeInBits()) {
            greatestFoundArg = arg;
        }
        if (arg.getOffset() < smallestFoundArg.getOffset()) {
            smallestFoundArg = arg;
        }
        return true;
    }
    public IArgumentType getArgAtGreatestOffset() {
        return greatestFoundArg;
    }
    public IArgumentType getArgAtSmallestOffset() {
        return smallestFoundArg;
    }

}
