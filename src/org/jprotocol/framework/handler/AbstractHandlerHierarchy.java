package org.jprotocol.framework.handler;
import static org.jprotocol.framework.handler.HandlerDsl.root;

import org.jprotocol.framework.handler.Handler.Type;
import org.jprotocol.framework.handler.HandlerDsl.UpperHandler;
/**
* This class is generated by DefaultHierarchyGenerator.groovy
* @author eliasa01
*/
abstract public class AbstractHandlerHierarchy<R extends Handler<?, ?>> {
    protected final Type type;
    protected final boolean msbFirst;
    protected final IProtocolState protocolState;
    protected final IProtocolSniffer sniffer;
    private final R root;
    public AbstractHandlerHierarchy(Type type, final IFlushable flushable, IProtocolState protocolState, IProtocolSniffer sniffer) {
        this.type = type;
        this.msbFirst = false;
        this.sniffer = sniffer;
        this.protocolState = protocolState;
        this.root = createRoot(flushable);
        init();
    }
    private void init() {
        root(getRoot(), upperHandlers());
    }
    protected final R getRoot() {
        return root;
    }
    public void receive(byte[] data) {
        root.receive(data);
    }
    abstract protected R createRoot(IFlushable flushable);
    abstract protected UpperHandler[] upperHandlers();
}
