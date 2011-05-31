package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.notNull;
import static org.jprotocol.util.Contract.require;


abstract public class ArgumentIterator {
    private final IProtocolMessage protocol;

    ArgumentIterator(IProtocolMessage protocol) {
        require(notNull(protocol));
        this.protocol = protocol;
    }
    

    public void iterate() {
        iterate(null, protocol.getProtocolType().getArguments(), null);
    }

    private void iterate(IArgumentType parent, IArgumentType[] args, String value) {
        //Recurse with bredth first, not depth
        for (IArgumentType arg : args) {
            if (isValidArg(parent, value) && !arg.isIndexedType()) {
                boolean result = iter(arg);
                if (!result) return;
            }
        }
        for (IArgumentType arg : args) {
            if (isValidArg(parent, value)) {
                _iter(arg);
            }
        }
    }

    abstract protected boolean iter(IArgumentType arg);

    private void _iter(IArgumentType arg) {
        if (arg.isEnumType()) {
            for (INameValuePair nvp : arg.getValues()) {
                iterate(arg, nvp.getArgTypes(), nvp.getName());
            }
        } else if (arg.isIndexedType()) {
            iterateIndexedChildren(arg.getChildren());
        }
    }

    private void iterateIndexedChildren(IArgumentType[] args) {
        for (IArgumentType subArg : args) {
            if (!subArg.isIndexedType()) {
                iter(subArg);
            } else {
                iterateIndexedChildren(subArg.getChildren());
            }
        }
    }

    
    
    private boolean isValidArg(IArgumentType arg, String value) {
        if (arg == null) return true;
        return protocol.getProtocolType().getDiscriminator().isInUse(protocol, arg) && value.equals(protocol.getValue(arg));
    }
}
