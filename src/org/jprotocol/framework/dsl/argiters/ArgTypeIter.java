package org.jprotocol.framework.dsl.argiters;

import java.util.ArrayList;
import java.util.List;

import org.jprotocol.framework.dsl.IArgumentType;
import org.jprotocol.framework.dsl.IProtocolLayoutType;


abstract public class ArgTypeIter extends AbstractArgTypeIter {
    private final IProtocolLayoutType type;

    
    protected ArgTypeIter(IProtocolLayoutType type) {
        this.type = type;
    }
    
    @Override
    protected final IArgumentType[] getArgTypes() {
        List<IArgumentType> l = new ArrayList<IArgumentType>();
        for (IArgumentType a: type.getArguments()) {
            l.add(a);
        }
        return l.toArray(new IArgumentType[l.size()]);
    }
}
