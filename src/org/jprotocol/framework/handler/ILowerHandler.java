package org.jprotocol.framework.handler;

import org.jprotocol.framework.dsl.IProtocolMessage;



/**
 * A lower handler is a handler that is closer to the root handler, or is the root handler. 
 * @author eliasa01
 *
 */
public interface ILowerHandler extends IHandler {
    void send(IProtocolMessage protocol, IUpperHandler headerValue);
    void register(int upperHandlerValue, IUpperHandler handler);
}
