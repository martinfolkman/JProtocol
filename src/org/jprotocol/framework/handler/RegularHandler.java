package org.jprotocol.framework.handler;

import static org.jprotocol.util.Contract.check;
import static org.jprotocol.util.Contract.isNull;
import static org.jprotocol.util.Contract.notNull;
import static org.jprotocol.util.Contract.require;

import org.jprotocol.framework.dsl.AbstractDecoratedProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolLayoutFactory;



/**
 * This abstract handler is in between a lower handler and upper handlers
 * @author eliasa01
 *
 * @param <R>
 * @param <S>
 */
abstract public class RegularHandler<R extends AbstractDecoratedProtocolMessage, S extends AbstractDecoratedProtocolMessage> extends Handler<R, S> {

    protected RegularHandler(IProtocolLayoutFactory factory, 
            Type type, 
            boolean msbFirst, 
            String upperHandlerFieldName, 
            int headerReceiveValue, 
            int headerSendValue, 
            IProtocolState protocolState) {
        this(factory, type, msbFirst, upperHandlerFieldName, headerReceiveValue, headerSendValue, protocolState, null);
    }

    protected RegularHandler(IProtocolLayoutFactory factory, 
                             Type type, 
                             boolean msbFirst, 
                             String upperHandlerFieldName, 
                             int headerReceiveValue, 
                             int headerSendValue, 
                             IProtocolState protocolState,
                             IProtocolSniffer sniffer) {
        super(factory, type, msbFirst, upperHandlerFieldName, headerReceiveValue, headerSendValue, protocolState, sniffer);
    }

    public void setLowerHandler(ILowerHandler lowerHandler) {
        require(notNull(lowerHandler));
        check(isNull(this.lowerHandler));
        this.lowerHandler = lowerHandler;
        lowerHandler.register(headerReceiveValue, this);
    }

    @Override
    protected final void flush(IProtocolMessage p) {
        //Do nothing
    }
}
