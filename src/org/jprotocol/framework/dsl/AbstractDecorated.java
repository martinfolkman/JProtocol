package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.*;


public class AbstractDecorated {
    protected final IProtocolMessage protocol;

    protected AbstractDecorated(IProtocolMessage protocol) {
        require(notNull(protocol));
        this.protocol = protocol;
    }
    public String toString() {
        StringBuffer buf = new StringBuffer(protocol.getProtocolType().getName());
        for (IArgumentType a : protocol.getArguments()) {
            buf.append(", ");
            buf.append(a.getName());
            buf.append("=");
            buf.append(protocol.getValue(a)); 
        }
        return buf.toString();
    }
}
