package org.jprotocol.framework.dsl;



/**
 * Base interface for all request/response protocol DSLs
 */
public interface IProtocolLayoutFactory { 
    public static final String ID = "Id";
    
    
    String getName();
    
    /**
     * All data block types of this factory
     * @return
     */
    IProtocolLayoutType[] getProtocolTypes();

    IProtocolLayoutType getResponseProtocol();
    IProtocolLayoutType getRequestProtocol();
    
}
