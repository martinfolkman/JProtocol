package org.jprotocol.example.handler;
import org.jprotocol.framework.dsl.*;
import org.jprotocol.framework.handler.*;
import org.jprotocol.example.api.*;
/**
* This class is generated by DefaultHandlerGenerator.groovy
* @author eliasa01
*/
public class DefaultMyRootProtocolHandler extends Handler<MyRootProtocol_Request_API, MyRootProtocol_Response_API> {
    protected DefaultMyRootProtocolHandler(HandlerContext context) {
        super(new org.jprotocol.example.dsl.MyRootProtocol(), context);
    }
    @Override public final MyRootProtocol_Request_API createRequest(IProtocolMessage p) {
        return new MyRootProtocol_Request_API(p);
    }
    @Override public final MyRootProtocol_Response_API createResponse(IProtocolMessage p) {
        return new MyRootProtocol_Response_API(p);
    }
}