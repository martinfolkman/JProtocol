package org.jprotocol.framework.handler;

import static org.jprotocol.util.Contract.check;

import org.jprotocol.framework.dsl.IProtocolLayoutFactory;
import org.jprotocol.framework.handler.Handler.Type;

public class HandlerContext {
	public final Type type;
	public final IProtocolState protocolState;
	public final IProtocolSniffer sniffer;
	public final IProtocolLayoutFactory factory;
	public final boolean msbFirst;
	public final String upperHeaderRequestFieldName;
	public final String upperHeaderResponseFieldName;
	public final int lowerHeaderRequestValue;
	public final int lowerHeaderResponseValue;
	public HandlerContext(
			IProtocolLayoutFactory factory, 
            Type type, 
            boolean msbFirst, 
            String upperHeaderRequestFieldName, 
            String upperHeaderResponseFieldName,
            int lowerHeaderRequestValue, 
            int lowerHeaderResponseValue, 
            IProtocolState protocolState,
            IProtocolSniffer sniffer) {
		this.factory = factory;
		this.type = type;
		this.msbFirst = msbFirst;
        this.upperHeaderRequestFieldName = upperHeaderRequestFieldName;
        this.upperHeaderResponseFieldName = upperHeaderResponseFieldName;
        this.lowerHeaderRequestValue = lowerHeaderRequestValue;
        this.lowerHeaderResponseValue = lowerHeaderResponseValue;
		this.protocolState = protocolState;
		this.sniffer = sniffer;
	}
}
