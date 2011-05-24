package org.jprotocol.framework.dsl;


@SuppressWarnings("serial")
public class ProtocolException extends RuntimeException {

    public ProtocolException() {
        //
    }
    public ProtocolException(String msg) {
        super(msg);
    }
}
