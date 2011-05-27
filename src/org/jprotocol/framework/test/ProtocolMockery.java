package org.jprotocol.framework.test;

import static org.jprotocol.util.Contract.check;
import static org.jprotocol.util.Contract.ensure;
import static org.jprotocol.util.Contract.isNotNull;
import static org.jprotocol.util.Contract.notNull;
import static java.util.Collections.unmodifiableList;
import static org.jprotocol.framework.test.StringProtocolUtil.argOf;
import static org.jprotocol.framework.test.StringProtocolUtil.argsOf;
import static org.jprotocol.framework.test.StringProtocolUtil.isMatchArgs;
import static org.jprotocol.framework.test.StringProtocolUtil.nextArgs;
import static org.jprotocol.framework.test.StringProtocolUtil.readableProtocol;
import static org.jprotocol.framework.test.StringProtocolUtil.requestOf;
import static org.jprotocol.framework.test.StringProtocolUtil.responseOf;
import static org.jprotocol.framework.test.StringProtocolUtil.setArgs;
import static org.jprotocol.framework.test.StringProtocolUtil.valueOf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jprotocol.framework.dsl.IArgumentType;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolLayoutType.Direction;
import org.jprotocol.framework.handler.Handler;
import org.jprotocol.framework.handler.IHandler;
import org.jprotocol.framework.handler.IProtocolSniffer;
import org.jprotocol.framework.handler.QualifiedName;
import org.jprotocol.framework.list.Expr;

import org.jprotocol.quantity.Quantity;
import org.jprotocol.quantity.Unit;

/**
 * This class provides the mock functionality of a protocol stack.
 * It also implements the type {@link IProtocolSniffer}, this means that it must be registered as a sniffer in the
 * protocol stack/handler hierarchy.
 * The class provides the following functionality for a test client
 * <li>Setup expectations on protocol requests</li>
 * <li>Add a protocol response to a request</li>
 * <li>Send a response, this can be a spontanious protocol</li>
 * To allow the client to specify all this at any level of the protocol stack a Lisp-like list syntax is used.
 * The syntax looks like this:
 * <pre> 
 * ("ProtocolName" ("a1" "v1") ("a2" "v2"))
 * </pre>
 * Where: 
 * <li>ProtocolName is the name of the protocol, the protocol must exist in the protocol stack to be tested</li>
 * <li>a1 and a2 are arguments the protocol ProtocolName</li>
 * <li>v1 is a value of argument a1 and v2 is a value of argument a2</li>
 * <br>
 * If this is used for example for an expect (see {@link #expect}), it means the following:
 * <br>
 * In english:<br>
 * Expect that protocol by the name of ProtocolName with arguments a1 set to v1 and a2 set to v1 will take place.
 * <br>
 * And in code:<br>
 * <code>
 * protocolMockery.expect(Expr.create("("ProtocolName" ("a1" "v1") ("a2" "v2"))"));
 * </code>
 *  
 * @see Handler
 * @see Expr
 * @see StringProtocolUtil
 */
public class ProtocolMockery implements IProtocolSniffer {
//	private final static Logger logger = Logger.getLogger(ProtocolMockery.class.getName());
    private static final String INHIBIT = "\"Inhibit\"";
    private final List<QualifiedRequest> allowedRequests = new ArrayList<QualifiedRequest>(); 
    private final List<QualifiedRequest> expectedRequests = new ArrayList<QualifiedRequest>(); 
    private final List<QualifiedRequestResponse> requestResponses = new ArrayList<QualifiedRequestResponse>();
    private final List<String> errorMessages = new ArrayList<String>();
    private boolean allowRequests;
	private final IProtocolLogger protocolLogger;
	private final List<QualifiedName> filter = new ArrayList<QualifiedName>();
	private final Map<String, List<IHandler>> handlerMap;
	private final boolean isServer;
	/**
	 * 
	 * @param root The protocol stack/handler hierarchy that is to be mocked
	 * @param protocolLogger
	 * @param filter
	 */
    public ProtocolMockery(IHandler root, IProtocolLogger protocolLogger, QualifiedName...filters) {
        this(root, protocolLogger, false, filters);
    }
    
    public ProtocolMockery(IHandler root, IProtocolLogger protocolLogger, boolean allowMode, QualifiedName...filters) {
    	this.allowRequests = allowMode;
    	this.protocolLogger = protocolLogger;
    	for (QualifiedName qf: filters) this.filter.add(qf);
    	isServer = root.isServer();
    	this.handlerMap = createHandlerMapOf(root);
    	
    }
    
    private static Map<String, List<IHandler>> createHandlerMapOf(IHandler root) {
    	Map<String, List<IHandler>> map = new HashMap<String, List<IHandler>>();
    	iterHandlers(root, map);
    	return map;
	}

	private static void iterHandlers(IHandler h, Map<String, List<IHandler>> map) {
		List<IHandler> l = map.get(h.getProtocolName());
		if (l == null) {
			l = new ArrayList<IHandler>();
			map.put(h.getProtocolName(), l);
		}
		l.add(h);
		for (IHandler uh: h.getUpperHandlers()) {
			iterHandlers(uh, map);
		}
	}

	private Direction getExpectDirection() {
		return isServer ? Direction.Request: Direction.Response;
	}
	
	public synchronized void setFilter(QualifiedName...filters) {
		clearFilters();
		for (QualifiedName qf: filters) this.filter.add(qf);
	}
	
	public synchronized List<QualifiedName> getFilters() {
		return Collections.unmodifiableList(filter);
	}
	
	public synchronized void clearFilters() {
		filter.clear();
	}
	
	/**
     * @note Only used by tools
     * @return
     */
    public synchronized List<QualifiedRequest> getExpectations() {
    	return unmodifiableList(expectedRequests);
    }

    /**
     * @note Only used by tools
     * @return
     */
    public synchronized List<QualifiedRequest> getAllows() {
    	return unmodifiableList(allowedRequests);
    }

    public List<QualifiedRequestResponse> getResponses() {
		return unmodifiableList(requestResponses);
	}
	public void expect(Expr request) {
		expect(request, new QualifiedName());
	}
	public synchronized void expect(Expr request, QualifiedName context) {
        if (checkIfRequestIsOk(request, context)) {
            expectedRequests.add(new QualifiedRequest(request, context));
            protocolLogger.writeSuccessfulExpect(request.toString());
        }
    }
	public void allow(Expr request) {
		allow(request, new QualifiedName());
	}
	public synchronized void allow(Expr request, QualifiedName context) {
        if (checkIfRequestIsOk(request, context)) {
            allowedRequests.add(new QualifiedRequest(request, context));
            protocolLogger.writeSuccessfulAllow(request.toString());
        }
    }
    private static final int WAIT_PERIOD = 500;

    public synchronized String getErrorMessages(Quantity timeout) {
    	long timeoutInMsLeft = Math.round(timeout.convert(Unit.ms).getValue());
        while (timeoutInMsLeft > 0) {
            try {
                if (expectedRequests.isEmpty()) {
                    return getErrorMessages();
                }
                if(timeoutInMsLeft > 0){
                    wait(Math.min(timeoutInMsLeft, WAIT_PERIOD));
                }
                timeoutInMsLeft -= WAIT_PERIOD;
            } catch (InterruptedException e) {
                timeoutInMsLeft = 0;
            }
        }
        return getErrorMessages();
    }
    
    public synchronized boolean hasError(Quantity timeout) {
    	protocolLogger.writePendingVerify(timeout);
        final boolean error = !getErrorMessages(timeout).isEmpty();
        if (error) {
        	protocolLogger.writeFailedVerify(getErrorMessages());
        } else {
        	protocolLogger.writeSuccessfulVerify();
        }
        return error;
    }
    
    public synchronized List<String> getErrorMessagesAsList() {
    	List<String> result = new ArrayList<String>();
        for (String em: errorMessages) {
        	result.add(em);
        }
        for (QualifiedRequest e: expectedRequests) {
        	
            result.add("The " + getExpectDirection() + " " + readableProtocol(e.request) + " was expected, but has not been received");
        }
        return result;
    }
    
    public synchronized String getErrorMessages() {
        StringBuffer buf = new StringBuffer();
    	for (String em: getErrorMessagesAsList()) {
            if (buf.length() > 0) {
                buf.append('\n');
            }
            buf.append(em);
    	}
        return buf.toString();
    }
    
    public synchronized boolean hasError() {
        return !getErrorMessages().isEmpty();
    }

    public synchronized void clear() {
        clearErrorMessages();
        clearExpectedRequests();
        clearAllowedRequests();
        clearResponses();
    }
    
    public synchronized void clearErrorMessages() {
    	errorMessages.clear();
    }
    public synchronized void clearExpectedRequests() {
    	expectedRequests.clear();
    }
    public synchronized void clearAllowedRequests() {
    	allowedRequests.clear();
    }
    public synchronized void clearResponses() {
    	requestResponses.clear();
    }
    
    public synchronized void addResponse(Expr request, Expr response, boolean removeWhenMatched) {
    	addResponse(request, response, removeWhenMatched, new QualifiedName());
    }
    
    public synchronized void addResponse(Expr request, Expr response, boolean removeWhenMatched, QualifiedName context) {
        protocolLogger.writeAddResponse(request.toString(), response.toString());
        requestResponses.add(new QualifiedRequestResponse(request, response, removeWhenMatched, context));
    }
    
    public synchronized void addResponse(Expr requestResponse, boolean removeWhenMatched) {
    	addResponse(requestResponse, removeWhenMatched, new QualifiedName());
    }
    
    public synchronized void addResponse(Expr requestResponse, boolean removeWhenMatched, QualifiedName context) {
    	addResponse(requestOf(requestResponse), responseOf(requestResponse), removeWhenMatched, context);
    }
    
	public void send(Expr spontaniusProtocolData) {
		send(spontaniusProtocolData, new QualifiedName());
	}
	public void send(Expr spontaniusProtocolData, QualifiedName context) {
	    protocolLogger.writeInject(spontaniusProtocolData.toString());
		send(sendProtocolOf(spontaniusProtocolData, context), context); //TODO write unit test for this
	}
	
	public IProtocolMessage sendProtocolOf(Expr expr, QualifiedName context) {
		return setArgs(expr.cdr(), handlerOf(StringProtocolUtil.protocolNameOf(expr), context).createSend());
	}
	
    public synchronized void send(IProtocolMessage protocol) {
    	send(protocol, new QualifiedName());
    }
    public synchronized void send(IProtocolMessage protocol, QualifiedName context) {
        IHandler h = handlerOf(protocolNameOf(protocol), context);
        check(isNotNull(h));
        h.sendToLower(protocol, true);
    }
	
    public synchronized void allowRequests() {
    	clear();
    	protocolLogger.writeAllowRequests();
        allowRequests = true;
    }
	
    public synchronized boolean isAllowMode() {
    	return allowRequests;
    }
    
    
    public synchronized void specifyRequests() {
    	clear();
    	protocolLogger.writeSpecifyRequests();
        allowRequests = false;
    }
    
    @Override
    public synchronized final IProtocolMessage sniff(IProtocolMessage protocol, IHandler handler) throws InhibitException {
        if (isHandlerToBeTested(handler)) {
	        matchWithExpectedRequest(protocol, handler.getQualifiedName());
	        return matchingResponseOf(protocol, handler.getQualifiedName());
        }
        return null;
    }
    
	@Override
	public void sniffSend(IProtocolMessage protocol, IHandler handler) {
		//do nothing
	}

    private void matchWithExpectedRequest(IProtocolMessage protocol, QualifiedName context) {
        for (QualifiedRequest er: allowedRequests) {
            if (StringProtocolUtil.protocolNameOf(er.request).equals(protocolNameOf(protocol))) {
                addErrorMsg(er.request, matchArgs(nextArgs(er.request), protocol));
                return;
            }
        }
        for (QualifiedRequest er: expectedRequests) {
            if (StringProtocolUtil.protocolNameOf(er.request).equals(protocolNameOf(protocol))) {
                addErrorMsg(er.request, matchArgs(nextArgs(er.request), protocol));
                expectedRequests.remove(er);
                return;
            }
        }
        if (allowRequests) return;
        errorMessages.add("No expecations made for " + getExpectDirection() + " " + errorMsgOf(protocol));
        return;
    }

    private void addErrorMsg(Expr request, String argMsg) {
        if (!argMsg.isEmpty()) {
        	errorMessages.add("The " + getExpectDirection() + " \"" + StringProtocolUtil.protocolNameOf(request) + "\" arguments did not match:"+ argMsg);
        }
    }
	private String errorMsgOf(IProtocolMessage protocol) {
		StringBuilder msg = new StringBuilder();
		msg.append('"');
        msg.append(protocol.getProtocolType().getName());
		msg.append('"');
        boolean comma = false;
        try {
        	for (IArgumentType arg : protocol.getArguments()) {
        		if (!comma) {
        			comma = true;
        		} else {
        			msg.append(',');
        		}
        		msg.append(" \"");
        		msg.append(arg.getName());
        		msg.append("\"=\"");
        		msg.append(protocol.getValue(arg));
        		msg.append('"');
        	}
        }
        catch (Throwable t) {
        	// To handle protocol framework limitation with respect to size switch within a protocol
        	// TODO: remove the try catch when the protocol fw supports size switching in a protocol.
        }
		return msg.toString();
	}
    
    
    private boolean isHandlerToBeTested(IHandler handler) {
    	for (QualifiedName qName: filter) {
    		if (handler.getQualifiedName().startsWith(qName)) {
    			return true;
    		}
    	}
		return filter.size() == 0;
	}

	private IHandler handlerOf(String protocolName, QualifiedName context) {
		List<IHandler> l = handlerMap.get(protocolName);
		check(notNull(l), "Cannot find protocol ", protocolName);
    	check(l.size() > 0, "Cannot find protocol ", protocolName);
    	if (l.size() == 1) {
    		return l.get(0);
    	}
    	IHandler result = null;
    	for (IHandler h: l) {
    		if (h.getQualifiedName().startsWith(context)) {
    			check(result == null, "Protocol ", protocolName, " is not unique");
    			result = h;
    		}
    	}
    	ensure(notNull(result));
    	return result;
    }
    
    private boolean checkIfRequestIsOk(Expr request, QualifiedName context) {
    	final String protocolName = StringProtocolUtil.protocolNameOf(request);
        IHandler h = handlerOf(protocolName, context);
        if (h == null) {
        	final String failureMessage = "Protocol " + protocolName + " doesn't exist";
            protocolLogger.writeFailedExpect(protocolName, failureMessage);
        	errorMessages.add(failureMessage);
            return false;
        }        
        return true;
    }
   
    
    private IProtocolMessage matchingResponseOf(IProtocolMessage protocol, QualifiedName context) throws InhibitException {
        IHandler h = handlerOf(protocolNameOf(protocol), context);
        for (QualifiedRequestResponse t: requestResponses) {
            if (StringProtocolUtil.protocolNameOf(t.request).equals(protocolNameOf(protocol)) && isMatchArgs(argsOf(t.request), protocol)) {
                checkInhibit(t);
                IProtocolMessage response = setArgs(argsOf(t.response), h.createSend());
                if (t.removeWhenMatched) {
                	requestResponses.remove(t);
                }
                return response;
            }
        }
        return null;
    }
    
    private void checkInhibit(QualifiedRequestResponse t) throws InhibitException{
        if(t.response.car().toString().equals(INHIBIT)) {
        	if (t.removeWhenMatched) {
        		requestResponses.remove(t);
        	}
            throw new InhibitException();
        }
    }

    
    
    private String matchArgs(Expr args, IProtocolMessage protocol) {
        if (args.isEmpty()) return "";
        String rValue = protocol.getValue(argOf(args));
        String msg = "";
        if (!valueOf(args).equals(rValue)) {
            msg = " expected \"" + argOf(args) + "\"=\"" + valueOf(args) + "\" but was \"" + rValue + "\"";
        }
        String nextMatchArgs = matchArgs(nextArgs(args), protocol);
        if (nextMatchArgs.isEmpty()) {
        	return msg;
        }
        return msg + "," + nextMatchArgs;
    }
    
    private static String protocolNameOf(IProtocolMessage p) {
        return p.getProtocolType().getProtocolName();
    }

    public void inactivate(List<Pair<String, QualifiedName>> layers)
    {
        for ( Pair<String, QualifiedName> layer : layers){
            
            handlerOf(layer.getFirst(), layer.getSecond()).inactivate();
            
        }        
    }
    
    public void activate(List<Pair<String, QualifiedName>> layers)
    {
        for ( Pair<String, QualifiedName> layer : layers){
            
            handlerOf(layer.getFirst(), layer.getSecond()).activate();
            
        }        
    }  
    
    public boolean isActive(List<Pair<String, QualifiedName>> layers){
    	boolean result = true;
        for ( Pair<String, QualifiedName> layer : layers){
            result = result && handlerOf(layer.getFirst(), layer.getSecond()).isActive();
        }        
        return result;
    }

	public void expect(String expr) {
		expect(Expr.create(expr));
	}

	public void allow(String expr) {
		allow(Expr.create(expr));
	}

	public void send(String expr) {
		send(Expr.create(expr));
	}
    
}



class ReqResTuple {
    final Expr request;
    final Expr response;
    final QualifiedName context;
    ReqResTuple(Expr request, Expr response, QualifiedName context) {
        this.request = request;
        this.response = response;
        this.context = context;
    }
    @Override
    public String toString() {
    	return context + " Req:" + request + ", Res:" + response;
    }
}

