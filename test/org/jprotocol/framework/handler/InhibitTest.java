package org.jprotocol.framework.handler;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jprotocol.framework.dsl.AbstractDecoratedProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolLayoutFactory;
import org.jprotocol.framework.dsl.ProtocolMessage;
import org.jprotocol.framework.dsl.ProtocolLayoutFactory;
import org.jprotocol.framework.handler.Handler.Type;
import org.jprotocol.framework.handler.IProtocolSniffer.InhibitException;
import org.junit.Before;
import org.junit.Test;


public class InhibitTest{
    
    private IProtocolSniffer sniffer;
    Mockery context;
    public static final TestProtocolFactory testProtocolFactory = new TestProtocolFactory("", true);
    
    @Before
    public void before(){
        context = new Mockery();
        sniffer = context.mock(IProtocolSniffer.class);  
     
    }
    
    @Test
    public void doNotCallReciveRequestOnInhibit() throws InhibitException{
        
        final TestHandler<AbstractDecoratedProtocolMessage, AbstractDecoratedProtocolMessage> handler = new TestHandler<AbstractDecoratedProtocolMessage, AbstractDecoratedProtocolMessage>(testProtocolFactory, Type.Server, false, null, 0, 0, new ProtocolState(), sniffer, false);
        
        
        // expectations
        context.checking(new Expectations() {{
         allowing (sniffer).sniff(with(any(IProtocolMessage.class)), with(any(IHandler.class))); will(throwException(new IProtocolSniffer.InhibitException()));
         
        }});

        
        byte[] data = {0,0};
        handler.receive(data);
        context.assertIsSatisfied();
        
    }
    
    
    @Test
    public void doCallReciveRequestOnSpecifiedResponse() throws InhibitException{
        
        final TestHandler<AbstractDecoratedProtocolMessage, AbstractDecoratedProtocolMessage> handler = new TestHandler<AbstractDecoratedProtocolMessage, AbstractDecoratedProtocolMessage>(testProtocolFactory, Type.Server, false, null, 0, 0, new ProtocolState(), sniffer, true);
        
        
        // expectations
        context.checking(new Expectations() {{
         allowing (sniffer).sniff(with(any(IProtocolMessage.class)), with(any(IHandler.class))); will(returnValue(handler.createReceive()));
         
        }});

        
        byte[] data = {0,0};
        handler.receive(data);
        context.assertIsSatisfied();
        
    }
    


    @Test
    public void doCallReciveRequestOnNoSpecifiedResponse() throws InhibitException {

        final TestHandler<AbstractDecoratedProtocolMessage, AbstractDecoratedProtocolMessage> handler = new TestHandler<AbstractDecoratedProtocolMessage, AbstractDecoratedProtocolMessage>(testProtocolFactory, Type.Server, false, null, 0, 0,
                new ProtocolState(), sniffer, true);

        // expectations
        context.checking(new Expectations() {
            {
                allowing(sniffer).sniff(with(any(IProtocolMessage.class)), with(any(IHandler.class)));
                will(returnValue(null));

            }
        });

        byte[] data = { 0, 0 };
        handler.receive(data);
        context.assertIsSatisfied();

    }


}
class TestHandler<R extends AbstractDecoratedProtocolMessage, S extends AbstractDecoratedProtocolMessage> extends LeafHandler<R , S>{
 
    boolean callReceiveRequest = false;
    
    
    protected TestHandler(IProtocolLayoutFactory factory, Type type, boolean msbFirst, String headerFieldName,
            int headerReceiveValue, int headerSendValue, IProtocolState protocolState, IProtocolSniffer sniffer, boolean callReceiveRequest) {
        super(factory, type, msbFirst, headerReceiveValue, headerSendValue, protocolState, sniffer);
        this.callReceiveRequest = callReceiveRequest;
    }

    @Override
    public R createRequest(IProtocolMessage p) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public S createResponse(IProtocolMessage p) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    protected IProtocolMessage createReceive(byte[] data) {
        return new ProtocolMessage(InhibitTest.testProtocolFactory.getRequestProtocol(), msbFirst, 0, 0);
    }

    @Override
    protected void receiveRequest(AbstractDecoratedProtocolMessage request, AbstractDecoratedProtocolMessage response) {
        if(!callReceiveRequest){
            assert false: "receiveRequest should not be called";
        }
    }
    
}

class TestProtocolFactory extends ProtocolLayoutFactory {

    protected TestProtocolFactory(String name, boolean includePayload) {
        super(name, includePayload);
        
        protocols(
                request(
                        args(
                                argByte("a", 1, 
                                        values(
                                                value("v1", 0), 
                                                value("v2", 1)
                                        )
                                 )
                         )
                ),
                response()
        );
        
        
    }
    
}   

    


