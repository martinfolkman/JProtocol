package org.jprotocol.framework.handler;

import org.jprotocol.framework.dsl.AbstractDecoratedProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolLayoutFactory;



/**
 * A leaf handler is at the top of the handler hierarchy
 * @author eliasa01
 *
 * @param <R>
 * @param <S>
 */
abstract public class LeafHandler<R extends AbstractDecoratedProtocolMessage, S extends AbstractDecoratedProtocolMessage> extends RegularHandler<R, S> {

    protected LeafHandler(IProtocolLayoutFactory factory, Type type, boolean msbFirst, int lowerHandlerValue, int headerSendValue, IProtocolState protocolState) {
        this(factory, type, msbFirst, lowerHandlerValue, headerSendValue, protocolState, null);
    }
    protected LeafHandler(IProtocolLayoutFactory factory, Type type, boolean msbFirst, int lowerHandlerValue, int headerSendValue, IProtocolState protocolState, IProtocolSniffer sniffer) {
        super(factory, type, msbFirst, null, lowerHandlerValue, headerSendValue, protocolState, sniffer);
    }
    
    @Override
    final protected void makeHeader(IProtocolMessage p, IProtocolMessage payload, int headerValue) {
        //
    }
    protected void unsupportedProtocol(IProtocolMessage p) {
        //
    }
}
