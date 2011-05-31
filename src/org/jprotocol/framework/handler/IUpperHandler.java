package org.jprotocol.framework.handler;


/**
 * An upper handler is a handler closer to a leaf handler, or is a leaf handler
 * @author eliasa01
 *
 */
public interface IUpperHandler extends IHandler {
    int getHeaderSendValue();
    void receive(byte[] data);
    void resetState();
    
    @Deprecated
    void setFactory(String version);
}
