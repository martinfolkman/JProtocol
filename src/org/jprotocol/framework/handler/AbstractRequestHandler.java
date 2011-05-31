package org.jprotocol.framework.handler;


abstract public class AbstractRequestHandler<R> {
    abstract public void handle(R request);
}
