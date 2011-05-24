package org.jprotocol.framework.handler;

import org.jprotocol.framework.dsl.IProtocolMessage;


@SuppressWarnings("serial")
public class UnsupportedProtocol extends RuntimeException {
    public UnsupportedProtocol(IProtocolMessage p, String headerFieldName, String value) {
        super(messageOf(p, headerFieldName, value));
    }

    private static String messageOf(IProtocolMessage p, String headerFieldName, String value) {
        String message = "Unsupported protocol: " + p.getProtocolType().getName();
        if (headerFieldName != null) {
            message += ", field: " + headerFieldName + ", value: " + value;
        }
        return message;
    }
}
