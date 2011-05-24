package org.jprotocol.framework.dsl;



public interface INameValuePair {
    String getName();
    
    int getValue();
    
    IArgumentType[] getArgTypes();
    IArgumentType argOf(String name);
    
    boolean hasPayload();
    
    int getPayloadStartIndex();
}
