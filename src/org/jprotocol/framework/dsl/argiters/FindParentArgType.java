package org.jprotocol.framework.dsl.argiters;

import org.jprotocol.framework.dsl.IArgumentType;
import org.jprotocol.framework.dsl.IProtocolLayoutType;


public class FindParentArgType extends ArgTypeIter {
    public IArgumentType parentArgType;
    private final IArgumentType argType;
    public FindParentArgType(IArgumentType argType, IProtocolLayoutType type) {
        super(type);
        this.argType = argType;
        iterate();
    }

    @Override
    protected boolean iter(IArgumentType arg) {
        if (arg.isIndexedType()) {
            for (IArgumentType subArg : arg.getChildren()) {
                if (subArg == argType) {
                    parentArgType = arg;
                    return false;
                }
            }
        }
        return true;
    }
}
