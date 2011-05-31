package org.jprotocol.framework.handler;

import org.apache.commons.lang.builder.HashCodeBuilder;


public class HandlerKey {
    private final String protocolName;
    private final String[] context;

    public HandlerKey(String protocolName, String...context) {
        this.protocolName = protocolName;
        this.context = context;
    }
    
    @Override public boolean equals(Object o) {
        if (o instanceof HandlerKey) {
            HandlerKey hk = (HandlerKey) o;
            return protocolName.equals(hk.protocolName) && isContextEqual(hk);
        }
        return false;
    }
    private boolean isContextEqual(HandlerKey hk) {
        if (context.length != hk.context.length) return false;
        for (int i = 0; i < context.length; i++) {
            if (!context[i].equals(hk.context[i])) {
                return false;
            }
        }
        return true;
    }

    @Override public int hashCode() {
        return new HashCodeBuilder(17, 37).append(protocolName).append(context).toHashCode();
    }

    public String getProtocolName() {
        return protocolName;
    }
}
