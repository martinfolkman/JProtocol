package org.jprotocol.framework.handler;


@SuppressWarnings("serial")
public class ProtocolTimeoutException extends RuntimeException {

    public ProtocolTimeoutException(String msg) {
        super(msg);
    }

}
