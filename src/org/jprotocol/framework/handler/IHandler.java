package org.jprotocol.framework.handler;

import java.util.List;

import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolLayoutFactory;
import org.jprotocol.framework.dsl.IProtocolLayoutType;


/**
 * The protocol stack is represented by this type
 * The handler is a composite consisting of a tree of protocol handlers.
 * Each handler has a {@link IProtocolLayoutFactory} which is a representation of the metadata of a protocol.
 * @author eliasa01
 *
 */
public interface IHandler {
    public enum Type {Client, Server}
    
    
    /**
     * 
     * @return All upper handlers of this handler
     */
    IHandler[] getUpperHandlers();
    
    
    
    /**
     * Send this protocol data to next lower level handler
     * @param p the protocol data
     * @param realtime If true the data is sent directly, if false there might be a delay
     */
    void sendToLower(IProtocolMessage p, boolean realtime);
    
    /**
     * Create a protocol data object for the receiving protocol  
     * @return
     */
    IProtocolMessage createReceive();
    /**
     * Create a protocol data object for the sedning protocol  
     * @return
     */
    IProtocolMessage createSend();
    /**
     * 
     * @return The meta data of the sending protocol
     */
    IProtocolLayoutType sendType();
    /**
     * 
     * @return The meta data of the receiving protocol
     */
    IProtocolLayoutType receiveType();
    
    /**
     * @return The name of the protocol
     */
    String getProtocolName();
    
    /**
     * The protocol dsl factory associated with the handler
     * @return
     */
    IProtocolLayoutFactory getFactory();
    
    /**
     * The state of the virtual arguments used in the {@link IProtocolLayoutFactory}
     * @return
     */
    IProtocolState getProtocolState();
    
    /**
     * 
     * @return The name of the field of this protocols receiving data
     */
    String getHeaderFieldName();
    
    /**
     * The qualified name is built up by this handlers lower handlers switch-names separated by / (slash)
     * '/RootSwitch/Switch1/Switch2'....
     * For example, the lower handler of this handler is the root handler and has a switch that is called 'On'. 
     * This is the switch that allows trafic to arrive at this handler. The qualifed name of this handler would then be
     * '/On'. 
     * 
     * @return The qualified name of this handler
     */
	QualifiedName getQualifiedName();
	
    void activate();
    void inactivate();
    boolean isActive();
    
    /**
     * 
     * @return The switches of this handlers protocol.
     */
	List<String> getSwitchValues();
    String switchValueStrOf(IUpperHandler uh);



	boolean isServer();



	boolean isClient();

}
