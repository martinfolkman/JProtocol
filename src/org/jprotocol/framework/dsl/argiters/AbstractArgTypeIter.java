package org.jprotocol.framework.dsl.argiters;

import org.jprotocol.framework.dsl.IArgumentType;
import org.jprotocol.framework.dsl.INameValuePair;


public abstract class AbstractArgTypeIter {
    
    public final void iterate() {
        iterate(getArgTypes());
    }

    protected abstract IArgumentType[] getArgTypes(); 

    abstract protected boolean iter(IArgumentType arg);
    
    private final void iterate(IArgumentType[] args) {
        //Recurse with bredth first, not depth
        for (IArgumentType arg : args) {
            boolean result = iter(arg);
            if (!result) return;
        }
        for (IArgumentType arg : args) {
            _iter(arg);
        }
    }


    private final void _iter(IArgumentType arg) {
        if (arg.isEnumType()) {
            for (INameValuePair nvp : arg.getValues()) {
                iterate(nvp.getArgTypes());
            }
        } else if (arg.isIndexedType()) {
            iterate(arg.getChildren());
        }
    }

}
