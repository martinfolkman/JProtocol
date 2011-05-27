package org.jprotocol.framework.handler;

import static org.jprotocol.quantity.Quantity.quantity;
import static org.jprotocol.quantity.Unit.ms;
import static org.jprotocol.quantity.Unit.s;
import static org.jprotocol.util.Contract.check;
import static org.jprotocol.util.Contract.implies;
import static org.jprotocol.util.Contract.isNotNull;
import static org.jprotocol.util.Contract.isNull;
import static org.jprotocol.util.Contract.neverGetHere;
import static org.jprotocol.util.Contract.notNull;
import static org.jprotocol.util.Contract.require;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jprotocol.framework.dsl.AbstractDecoratedProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolLayoutFactory;
import org.jprotocol.framework.dsl.IProtocolLayoutType;
import org.jprotocol.framework.dsl.IllegalByteArrayValue;
import org.jprotocol.framework.dsl.ProtocolMessage;
import org.jprotocol.framework.dsl.ProtocolException;
import org.jprotocol.framework.handler.IProtocolSniffer.InhibitException;

import org.jprotocol.quantity.Quantity;


/**
 * Base functionality for handlers
 * @author eliasa01
 *
 * @param <R> the request type
 * @param <S> the response type
 */
abstract public class Handler<R extends AbstractDecoratedProtocolMessage, S extends AbstractDecoratedProtocolMessage> extends BaseHandler implements IUpperHandler, ILowerHandler {
    public enum Type {Client, Server}
    private static final Quantity DEFAULT_TIMEOUT = quantity(5, s);
    private final static int responseDelay;
    static {
        responseDelay = Integer.valueOf(System.getProperty("edsl.simmock.responsedelay", "20"));
    }
    private final static IProtocolSniffer nullSniffer = new NullProtocolSniffer(); 
    private final IProtocolSniffer sniffer;
    private final String headerFieldName;
    protected final IProtocolLayoutFactory factory;
    private final Type type;
    protected final boolean msbFirst;
    
    /**
     * Used to register an upper handler to a lower handler
     */
    protected final int headerReceiveValue;
    
    /**
     * Used to create the header when a protocol is sent
     */
    private final int headerSendValue;
    private final IProtocolState protocolState;
    protected ILowerHandler lowerHandler;
    private R lastRequest;
    private S lastResponse;
    private boolean active = true;
    
    
    /**
     * 
     * @param factory
     * @param type is it a server or a client
     * @param msbFirst
     * @param headerFieldName the name of the field of this protocols receiving data
     * @param headerReceiveValue the value of the field of this protocols receiving data
     * @param headerSendValue the value that is sent to the header of this protocol
     */
    protected Handler(IProtocolLayoutFactory factory, 
                      Type type, 
                      boolean msbFirst, 
                      String headerFieldName, 
                      int headerReceiveValue, 
                      int headerSendValue, 
                      IProtocolState protocolState,
                      IProtocolSniffer sniffer) {
        require(notNull(factory)); 
        require(notNull(type));
        this.factory = factory;
        this.type = type;
        this.msbFirst = msbFirst;
        this.headerFieldName = headerFieldName;
        if (isServer()) {
            this.headerReceiveValue = headerReceiveValue;
            this.headerSendValue = headerSendValue;
        } else {
        	check(isClient());
            this.headerReceiveValue = headerSendValue;
            this.headerSendValue = headerReceiveValue;
        }
        this.protocolState = protocolState;
        implies(isNotNull(headerFieldName), isNotNull(receiveType().argOf(headerFieldName)));
        if (sniffer == null) {
            this.sniffer = nullSniffer;
        } else {
            this.sniffer = sniffer;
        }
    }
    
    public void setLowerHandler(ILowerHandler lowerHandler) {
        require(notNull(lowerHandler));
        check(isNull(this.lowerHandler));
        this.lowerHandler = lowerHandler;
        lowerHandler.register(headerReceiveValue, this);
    }
    
    @Override
    public final String getHeaderFieldName() {
        return headerFieldName;
    }
    
    @Override public final IProtocolLayoutFactory getFactory() {
        return factory;
    }
    
    @Override public String getProtocolName() {
        return factory.getName();
    }

    @Override
    public final IProtocolState getProtocolState() {
        return protocolState;
    }
    protected void failure(boolean checkCondition) {
        pCheck(checkCondition, "");
    }
    protected void pCheck(boolean checkCondition, Object msg) {
        if (!checkCondition) throw new ProtocolException(msg.toString());
    }
    
    @Override
    public void resetState() {
        for (IUpperHandler uh : getUpperHandlerList()) {
            uh.resetState();
        }
    }
    @Override
    public boolean isServer() {
        return type == Type.Server;
    }
    @Override
    public boolean isClient() {
        return type == Type.Client;
    }
    final public void sendRequest(R request) {
        lastRequest = request;
        sendToLower(request);
    }
    
    public void sendRealtimeResponse(S response) {
    	if (active) {
        	sendToLower(response, true);
        }
    }
    public void sendResponse(S response) {
        sendToLower(response);
    }
    
    final public R createRequest() {
        return createRequest(new ProtocolMessage(factory.getRequestProtocol(), msbFirst, getProtocolState()));
    }
    
    abstract public R createRequest(IProtocolMessage p);
    
    public S createResponse() {
        return createResponse(new ProtocolMessage(factory.getResponseProtocol(), msbFirst, getProtocolState()));
    }
    
    abstract public S createResponse(IProtocolMessage p);
    
    /**
     * Override
     * @param request
     */
    protected void receiveRequest(R request, S response) {
        //
    }
    /**
     * Override
     * @param request
     */
    protected void receiveResponse(S response) {
        //
    }
    
    protected void setLastResponse(S response) {
        synchronized (this) {
            this.lastResponse = response;
            notifyAll();
        }

    }
    protected R getLastRequest() {
        synchronized (this) {
            return lastRequest;
        }
    }
    protected S getLastResponse() {
        synchronized (this) {
            return lastResponse;
        }
    }
    public final S sendBlockingRequest(R request) {
        long timeout = (long) getTimeout().convert(ms).getValue();
        synchronized (this) {
            sendRequest(request);
            lastResponse = null;
            for (int i = 0; i < getNrOfRetries(); i++) {
                try {
                    wait(timeout);
                    if (lastResponse != null) {
                        return lastResponse;
                    }
                } catch (InterruptedException e) {
                    //
                }
            }
        }
        throw new ProtocolTimeoutException("Timeout occurred waiting for " + factory.getResponseProtocol().getName());
    }


    
    /**
     * Override to if needed
     * @return
     */
    protected Quantity getTimeout() {
        return DEFAULT_TIMEOUT;
    }
    
    protected int getNrOfRetries() {
        return 1;
    }
    
    public void register(int upperHandlerValue, IUpperHandler handler) {
        require(notNull(handler));
        try {
            if (headerFieldName != null) {
                receiveType().argOf(headerFieldName).nvpOf(upperHandlerValue);
            }
        } catch (IllegalByteArrayValue e) {
            neverGetHere(e.getMessage());
        }
        check(isNull(upperHandlerOf(upperHandlerValue)), "Protocol: ", factory, " field : ", headerFieldName, "value: 0x", Integer.toHexString(upperHandlerValue));
        addHandler(upperHandlerValue, handler);
    }

    
    public final IHandler[] getUpperHandlers() {
        return getUpperHandlerList().toArray(new IHandler[getUpperHandlerList().size()]);
    }
    
    
    

    
    @Override
    public void receive(byte[] data) {
        if(active){
            require(notNull(data));
            IProtocolMessage p = createReceive(data);
            IProtocolMessage r;
            try {
                r = sniff(p);
                check(p.hasPayload());
                if (isServer()) {
                    receiveRequest(createRequest(p), createRes(r));
                } else {
                    check(isClient());
                    receiveResponse(createResponse(p));
                }
            } catch (InhibitException e) {
                // On inhibit Exception receiveRequest should not be called
            }
            if (headerFieldName != null) {
                notifyUpperHandler(p, upperHandlerOf(p.getValueAsNameValuePair(headerFieldName).getValue()));
            } else {
                notifyUpperHandler(p, upperHandlerOf(0));
            }
        }
    }
    
    @Override
    public final QualifiedName getQualifiedName() {
    	if (lowerHandler == null) {
    		return new QualifiedName();
    	}
    	QualifiedName lhqn = lowerHandler.getQualifiedName();
    	String sv = lowerHandler.switchValueStrOf(this);
    	if (sv.isEmpty()) {
    		return lhqn;
    	}
    	return lhqn.append(sv);
    }
    
    @Override
    public final String switchValueStrOf(IUpperHandler uh) {
    	check(getUpperHandlerList().contains(uh));
    	if (headerFieldName == null) return "";
    	Integer v = switchValueOf(uh);
    	if (v == null) return "";
    	return receiveType().argOf(headerFieldName).nvpOf(v).getName();
    }
    
    @Override
    public final List<String> getSwitchValues() {
    	List<String> l = new ArrayList<String>();
    	for (IUpperHandler uh: getUpperHandlerList()) {
    		String v = switchValueStrOf(uh);
    		if (!v.isEmpty()) {
    			l.add(v);
    		}
    	}
    	return l;
    }

    private S createRes(IProtocolMessage r) {
        if (r == null) return null;
        return createResponse(r);
    }

    private IProtocolMessage sniff(IProtocolMessage p) throws InhibitException {
            return sniffer.sniff(p, this);
    }

    protected void notifyUpperHandler(IProtocolMessage p, IUpperHandler uh) {
        if (uh != null) {
            uh.receive(payloadOf(p));
        } else {
            unsupportedProtocol(p);
        }
    }
    
    protected void unsupportedProtocol(IProtocolMessage p) {
        String value = null;
        if (headerFieldName != null) {
            value = p.getValueAsNameValuePair(headerFieldName).getName();
        }
        throw new UnsupportedProtocol(p, headerFieldName, value);
    }

    protected byte[] headerOf(final IProtocolMessage p, final boolean isHts) {
        return Arrays.copyOf(p.getData(), this.getPayloadStartIndex(p, isHts));
    }
    
    protected boolean hasPayload(IProtocolMessage p) {
        return getPayloadStartIndex(p, isServer()) <= p.getSize(); 
    }
    protected byte[] payloadOf(IProtocolMessage p) {
        return p.getData(getPayloadStartIndex(p, isServer()));
    }
    private void setPayload(IProtocolMessage p, IProtocolMessage payload) {
        p.setData(payload.getData(), getPayloadStartIndex(p, isClient()));
    }
    
    protected int getPayloadStartIndex(IProtocolMessage p, boolean hts) {
        return p.getHeaderEndIndex() + 1;
    }
    
    public int getHeaderSendValue() {
        return headerSendValue;
    }
    
    public void send(IProtocolMessage payload, IUpperHandler upperHandler) {
//        require(notNull(payload));
        if(active){
            require(notNull(upperHandler));
            IProtocolMessage p = createSend(); 
            check(p.hasPayload());
            setPayload(p, payload);
            makeHeader(p, payload, upperHandler.getHeaderSendValue());
            sniffer.sniffSend(p, this);
            if (isSendStateOk(p)) {
                if (lowerHandler == null) {
                    flush(p);
                } else {
                    lowerHandler.send(p, this);
                }
            }
        }
    }

    /**
     * Override this method to prevent a response to be sent to next lower level. 
     * Returns true as default 
     * @param p
     * @return
     */
    protected boolean isSendStateOk(IProtocolMessage p) {
        return true;
    }

    protected IProtocolMessage createSend(byte[] data) {
        require(notNull(data));
        return new ProtocolMessage(sendType(), msbFirst, getProtocolState(), data);
    }
    protected IProtocolMessage createSend(int noOfBytes) {
        return createSend(new byte[noOfBytes]);
    }
    public IProtocolMessage createSend() {
        return new ProtocolMessage(sendType(), msbFirst, getProtocolState());
    }
    @Override public IProtocolLayoutType sendType() {
        return isClient() ? factory.getRequestProtocol() : factory.getResponseProtocol();
    }
    
    protected IProtocolMessage createReceive(byte[] data) {
        require(notNull(data));
        return new ProtocolMessage(receiveType(), msbFirst, getProtocolState(), data);
    }


    public IProtocolMessage createReceive() {
        return new ProtocolMessage(receiveType(), msbFirst, getProtocolState());
    }
    @Override public IProtocolLayoutType receiveType() {
        return isClient() ? factory.getResponseProtocol() : factory.getRequestProtocol();
    }


    public void sendToLower(AbstractDecoratedProtocolMessage dp) {
        sendToLower(dp.getProtocol(), false);
    }
    public void sendToLower(AbstractDecoratedProtocolMessage dp, boolean realtime) {
        sendToLower(dp.getProtocol(), realtime);
    }
    protected void sendToLower(IProtocolMessage p) {
        sendToLower(p, false);
    }
    
    public void sendToLower(IProtocolMessage p, boolean realtime) {
        require(notNull(p));
        require(notNull(lowerHandler));
//        if (!realtime) {
//            doResponseDelay();
//        }
        sniffer.sniffSend(p, this);
        lowerHandler.send(p, this);
    }
    
    private void doResponseDelay() {
//        if (responseDelay <=0) {
//            return;
//        }
//        try {
//            Thread.sleep(responseDelay);
//        } catch (InterruptedException e) {
//            neverGetHere();
//        }
    }

    abstract protected void flush(IProtocolMessage p);
    abstract protected void makeHeader(IProtocolMessage header, IProtocolMessage payload, int headerValue);
    
    @Override public String toString() {
    	if (lowerHandler != null) {
    		String str = lowerHandler.switchValueStrOf(this);
    		if (str != null && !str.isEmpty()) {
    			return str;
    		}
    	}
        return factory.getName();
    }
    
    public void activate(){
        active = true;
    }
    
    public void inactivate(){
        active = false;
    }

    public boolean isActive() { 
    	return active; 
    }
    @Deprecated
    public void setFactory(String version) {
    	
    }
}

final class NullProtocolSniffer implements IProtocolSniffer {
    @Override
    public IProtocolMessage sniff(IProtocolMessage protocol, IHandler handler) {
        return null;
    }
	@Override
	public void sniffSend(IProtocolMessage protocol, IHandler handler) {
		//do nothing
	}

}