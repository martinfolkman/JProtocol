package org.jprotocol.framework.dsl.argiters;

import org.jprotocol.framework.dsl.IArgumentType;
import org.jprotocol.framework.dsl.IProtocolMessage;


public class ArgOfIter extends AbstractArgIter {
    public IArgumentType foundArg;
    private final String name;
    public ArgOfIter(IProtocolMessage db, String name) {
        super(db);
        this.name = name;
        iterate();
    }

    @Override
    protected boolean iter(IArgumentType arg) {
        if (arg.getName().equals(name)) {
            foundArg = arg;
            return false;
        }
        return true;
    }
    
}
