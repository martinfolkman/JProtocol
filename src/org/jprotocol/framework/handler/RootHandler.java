package org.jprotocol.framework.handler;

import static org.jprotocol.quantity.Quantity.quantity;
import static org.jprotocol.quantity.Unit.ms;

import org.jprotocol.framework.dsl.AbstractDecoratedProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolLayoutFactory;
import org.jprotocol.framework.realtime.RealtimeThread;


/**
 * The root handler is at the bottom of the handler hierarchy. 
 * Note that there can only be one root in a handler hierarchy
 * @author eliasa01
 *
 * @param <R>
 * @param <S>
 */
abstract public class RootHandler<R extends AbstractDecoratedProtocolMessage, S extends AbstractDecoratedProtocolMessage> extends Handler<R, S> {
    private final RealtimeThread rtThread;
	protected RootHandler(IProtocolLayoutFactory factory, Type type, boolean msbFirst, String upperHandlerFieldName, IProtocolState protocolState) {
        this(factory, type, msbFirst, upperHandlerFieldName, protocolState, null);
    }
    protected RootHandler(IProtocolLayoutFactory factory, Type type, boolean msbFirst, String upperHandlerFieldName, IProtocolState protocolState, IProtocolSniffer sniffer) {
        super(factory, type, msbFirst, upperHandlerFieldName, 0, 0, protocolState, sniffer);
        this.rtThread = new RealtimeThread(quantity(8, ms));

    }
    
    public RealtimeThread getRealtimeThread() {
    	return rtThread;
    }
    
    
}
