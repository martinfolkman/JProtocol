package org.jprotocol.framework.handler;

import org.jprotocol.framework.dsl.IProtocolMessage;


/**
 * All handlers ({@link Handler}) should delegate all trafic to a instance of this class.
 * The purpose of this is that all trafic can be sniffed. 
 * This can be used in a mock for example.
 * @author eliasa01
 * @see IHandler
 * @see Handler
 */
public interface IProtocolSniffer {
	/**
	 * 
	 * @param protocol ´The receiving protocol. If the handler hierarchy is a server then this is a request. If the handler hierarcy is client then this is a response 
	 * @param handler The handler that delegated to this sniffer
	 * @return If this is server impl. then a response can be returned. Null can also be returned.  
	 * @throws InhibitException
	 */
    IProtocolMessage sniff(IProtocolMessage protocol, IHandler handler) throws InhibitException;
    
    /**
     * 
     * @param protocol The sending protocol. If the handler hierarchy is a server then this is a response. If the handler hierarcy is client then this is a request
	 * @param handler The handler that delegated to this sniffer
     */
    void sniffSend(IProtocolMessage protocol, IHandler handler);
    
    @SuppressWarnings("serial")
	class InhibitException extends Exception{
        // No impl
    }
}
