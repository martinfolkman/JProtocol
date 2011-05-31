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
	public final String upperHeaderReceiveFieldName;
	public final String upperHeaderSendFieldName;
	public final int headerReceiveValue;
	public final int headerSendValue;
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
        if (type == Type.Server) {
            this.upperHeaderReceiveFieldName = upperHeaderRequestFieldName;
            this.upperHeaderSendFieldName = upperHeaderResponseFieldName;
            this.headerReceiveValue = lowerHeaderRequestValue;
            this.headerSendValue = lowerHeaderResponseValue;
        } else {
        	check(type == Type.Client);
            this.upperHeaderReceiveFieldName = upperHeaderResponseFieldName;
            this.upperHeaderSendFieldName = upperHeaderRequestFieldName;
            this.headerReceiveValue = lowerHeaderResponseValue;
            this.headerSendValue = lowerHeaderRequestValue;
        }
		this.protocolState = protocolState;
		this.sniffer = sniffer;
	}
}
