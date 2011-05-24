package org.jprotocol.framework.test;

import static org.jprotocol.quantity.Quantity.quantity;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolLayoutFactory;
import org.jprotocol.framework.dsl.IProtocolLayoutType;
import org.jprotocol.framework.dsl.ProtocolMessage;
import org.jprotocol.framework.dsl.ProtocolLayoutFactory;
import org.jprotocol.framework.handler.IHandler;
import org.jprotocol.framework.handler.IProtocolSniffer.InhibitException;
import org.jprotocol.framework.handler.IProtocolState;
import org.jprotocol.framework.handler.IUpperHandler;
import org.jprotocol.framework.handler.QualifiedName;
import org.jprotocol.framework.list.Expr;

import org.jprotocol.quantity.Unit;
import org.jprotocol.util.Contract;
import org.jprotocol.util.Contract.ContractError;

public class ProtocolMockeryTest extends TestCase {
    public static final String CHILD1_PROTOCOL_NAME = "Child1";
	private final IHandler root;
	private IHandler child1;
    public ProtocolMockeryTest() {
        child1 = new Hndlr(new Child1Factory());
		root = new Hndlr(new RootFactory(), child1);
        //TODO write tests that use context too 
        
    }
    
    public void testInactivateLayers(){
    	ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
    	assertTrue(child1.isActive());
        
    	List<Pair<String, QualifiedName>> layers = new ArrayList<Pair<String,QualifiedName>>();
    	QualifiedName s = new QualifiedName();
    	layers.add(new Pair<String, QualifiedName>(CHILD1_PROTOCOL_NAME, s));

    	tc.inactivate(layers);
    	assertFalse(child1.isActive());

		tc.activate(layers);
		assertTrue(child1.isActive());
    }
    
    public void testRequestExpectation() {
        ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        tc.expect(Expr.create("(\"ProtocolName\" (\"a1\" \"v1\"))"));
        String firstErrorMsg = "The Request \"ProtocolName\" with arguments: \"a1\"=\"v1\" was expected, but has not been received";
        assertEquals(firstErrorMsg, tc.getErrorMessages());
        tc.expect(Expr.create("(\"Child1\" (\"c1\" \"cv1\"))"));
        assertEquals(firstErrorMsg + "\n" + "The Request \"Child1\" with arguments: \"c1\"=\"cv1\" was expected, but has not been received", tc.getErrorMessages());
    }
    public void testRequestExpectationWhenProtocolNameDontExist() {
        ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        try {
        	tc.expect(Expr.create("(\"ProtocolNam\" (\"a1\" \"v1\") (\"a2\" \"v3\"))"));
        } catch (ContractError e) {
        	return;
        }
        Contract.neverGetHere();
    }
    
    public void testCorrectSniff() throws InhibitException {
        ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        tc.expect(Expr.create("(\"ProtocolName\" (\"a1\" \"v1\"))"));
        IProtocolMessage p = new ProtocolMessage(new RootFactory().getRequestProtocol(), false);
        p.setValue("a1", "v1");
        assertNull(tc.sniff(p, root));
        assertEquals("", tc.getErrorMessages());
    }
    
    
    public void testCorrectSniffWithMatchingResponseAndRemove() throws InhibitException {
    	assertCorrectSniffWithMatchingResponse(true);
    }
    public void testCorrectSniffWithMatchingResponseAnd() throws InhibitException {
    	assertCorrectSniffWithMatchingResponse(false);
    }
    private void assertCorrectSniffWithMatchingResponse(boolean removeMatchedResponse) throws InhibitException {
        ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        tc.expect(Expr.create("(ProtocolName (a1 v1))"));
        tc.addResponse(Expr.create("(ProtocolName (a1 v1))"), Expr.create("(ProtocolName (r1 s2))"), removeMatchedResponse);
        IProtocolMessage p = new ProtocolMessage(new RootFactory().getRequestProtocol(), false);
        p.setValue("a1", "v1");
        IProtocolMessage res = tc.sniff(p, root);
        assertNotNull(res);
        
        assertEquals("s2", res.getValue("r1"));
        assertEquals("", tc.getErrorMessages());
        assertEquals(removeMatchedResponse ? 0 : 1, tc.getResponses().size());
        res = tc.sniff(p, root);
        if (removeMatchedResponse) {
        	assertNull(res);
        } else {
        	assertNotNull(res);
        }
    }

    public void testCorrectSniffWithMatchingResponse2AndRemove() throws InhibitException {
    	assertCorrectSniffWithMatchingResponse2(true);
    }
    public void testCorrectSniffWithMatchingResponse2() throws InhibitException {
    	assertCorrectSniffWithMatchingResponse2(false);
    }
    void assertCorrectSniffWithMatchingResponse2(boolean removeMatchedResponse) throws InhibitException {
        ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        tc.expect(Expr.create("(ProtocolName (a1 v1))"));
        Expr reqRes = Expr.create("(ProtocolName (a1 v1))").cons(Expr.create("(ProtocolName (r1 s2))"));
        tc.addResponse(reqRes, removeMatchedResponse);
        IProtocolMessage p = new ProtocolMessage(new RootFactory().getRequestProtocol(), false);
        p.setValue("a1", "v1");
        IProtocolMessage res = tc.sniff(p, root);
        assertNotNull(res);
        assertEquals("s2", res.getValue("r1"));
        assertEquals("", tc.getErrorMessages());
        assertEquals(removeMatchedResponse ? 0 : 1, tc.getResponses().size());
    }
    
    public void testSniffWithNoResponseDefined() throws InhibitException {
        ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        tc.expect(Expr.create("(ProtocolName (a1 v1))"));

        IProtocolMessage p = new ProtocolMessage(new RootFactory().getRequestProtocol(), false);
        p.setValue("a1", "v1");
        
        IProtocolMessage res = tc.sniff(p, root);
        assertNull(res);
    }
    
    public void testSniffWithInhbitResponseAndRemove(){
    	assertSniffWithInhbitResponse(true);
    }
    public void testSniffWithInhbitResponse(){
    	assertSniffWithInhbitResponse(false);
    }
    private void assertSniffWithInhbitResponse(boolean removeMatchedResponse) {
        ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        Expr reqRes = Expr.create("(ProtocolName (a1 v1))").cons(Expr.create("(\"Inhibit\")"));
        tc.addResponse(reqRes, removeMatchedResponse);
        
        IProtocolMessage p = new ProtocolMessage(new RootFactory().getRequestProtocol(), false);
        p.setValue("a1", "v1");
        try {
            tc.sniff(p, root);
            fail("An InhibitException was expected");
        } catch (InhibitException e) {
        	try {
                tc.sniff(p, root);
                assertEquals(removeMatchedResponse ? 0 : 1, tc.getResponses().size());
            } catch (InhibitException unexpectedException) {
            	if (removeMatchedResponse) {
            		fail("Should not inhibit a response more than once");
            	}
            }	
        }
    }
    
    
    public void testProtcolNotPartOfTest() throws InhibitException {
        ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger(), new QualifiedName().append("adsf"));
        IProtocolMessage p = new ProtocolMessage(new RootFactory().getRequestProtocol(), false);
        assertNull(tc.sniff(p, root));
        assertEquals("", tc.getErrorMessages());
    }
    public void testMissingExpect() throws InhibitException {
        ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        IProtocolMessage p = new ProtocolMessage(new RootFactory().getRequestProtocol(), false);
        assertNull(tc.sniff(p, root));
        assertEquals("No expecations made for Request \"ProtocolName Request\" \"a1\"=\"v1\", \"a2\"=\"v3\"", tc.getErrorMessages());
    }
    public void testNotMatchingArgument() throws InhibitException {
        ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        tc.expect(Expr.create("(ProtocolName (a1 v1))"));
        IProtocolMessage p = new ProtocolMessage(new RootFactory().getRequestProtocol(), false);
        p.setValue("a1", "v2");
        assertNull(tc.sniff(p, root));
        assertEquals("The Request \"ProtocolName\" arguments did not match: expected \"a1\"=\"v1\" but was \"v2\"", tc.getErrorMessages());
    }
    public void testNotMatchingArguments() throws InhibitException {
        ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        tc.expect(Expr.create("(ProtocolName (a1 v1) (a2 v3))"));
        IProtocolMessage p = new ProtocolMessage(new RootFactory().getRequestProtocol(), false);
        p.setValue("a1", "v2");
        p.setValue("a2", "v4");
        assertNull(tc.sniff(p, root));
        assertEquals("The Request \"ProtocolName\" arguments did not match: expected \"a1\"=\"v1\" but was \"v2\", expected \"a2\"=\"v3\" but was \"v4\"", tc.getErrorMessages());
    }
    public void testNotMatchingArgument2() throws InhibitException {
        ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        tc.expect(Expr.create("(ProtocolName (a1 v1) (a2 v3))"));
        IProtocolMessage p = new ProtocolMessage(new RootFactory().getRequestProtocol(), false);
        p.setValue("a1", "v2");
        p.setValue("a2", "v3");
        assertNull(tc.sniff(p, root));
        assertTrue(tc.hasError());
        assertEquals("The Request \"ProtocolName\" arguments did not match: expected \"a1\"=\"v1\" but was \"v2\"", tc.getErrorMessages());
    }
    
    
    
    public void testHasError() {
        ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        tc.expect(Expr.create("(ProtocolName (a1 v1) (a2 v3))"));
        assertTrue(tc.hasError());
    }
    public void testClear() {
        ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        tc.expect(Expr.create("(ProtocolName (a1 v1) (a2 v3))"));
        tc.clear();
        assertFalse(tc.hasError());
    }
    
    public void testHasErrorWithTimeoutError() {
        ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        tc.expect(Expr.create("(ProtocolName (a1 v1) (a2 v3))"));
        assertTrue(tc.hasError(quantity(100, Unit.ms)));
        assertEquals("The Request \"ProtocolName\" with arguments: \"a1\"=\"v1\", \"a2\"=\"v3\" was expected, but has not been received", tc.getErrorMessages());
    }
    public void testHasErrorWithTimeoutError2() {
        final ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        tc.expect(Expr.create("(ProtocolName (a1 v1) (a2 v3))"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                    IProtocolMessage p = new ProtocolMessage(new RootFactory().getRequestProtocol(), false);
                    p.setValue("a1", "v2");
                    p.setValue("a2", "v4");
                    tc.sniff(p, root);
                } catch (InterruptedException e) {
                    //
                } catch (InhibitException e) {
                    fail();
                }
            }
            
        }).start();
        assertTrue(tc.hasError(quantity(10, Unit.s)));
        assertEquals("The Request \"ProtocolName\" arguments did not match: expected \"a1\"=\"v1\" but was \"v2\", expected \"a2\"=\"v3\" but was \"v4\"", tc.getErrorMessages());
    }
    public void testHasErrorWithTimeout() {
        final ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        tc.expect(Expr.create("(ProtocolName (a1 v1) (a2 v3))"));
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    IProtocolMessage p = new ProtocolMessage(new RootFactory().getRequestProtocol(), false);
                    p.setValue("a1", "v1");
                    tc.sniff(p, root);
                } catch (InterruptedException e) {
                    //
                } catch (InhibitException e) {
                    fail();
                }
            }
            
        }).start();
        assertFalse(tc.hasError(quantity(500, Unit.ms)));
        assertEquals("", tc.getErrorMessages());
    }
    public void testAllowMode() throws InhibitException {
        final ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger(), true);
        IProtocolMessage p = new ProtocolMessage(new RootFactory().getRequestProtocol(), false);
        p.setValue("a1", "v2");
        p.setValue("a2", "v3");
        
        assertNull(tc.sniff(p, root));
        assertFalse(tc.hasError());
        assertEquals("", tc.getErrorMessages());
        
        tc.clear();

        tc.expect(Expr.create("(ProtocolName (a1 v2) (a2 v3))"));
        assertNull(tc.sniff(p, root));
        assertFalse(tc.hasError());
        assertEquals("", tc.getErrorMessages());
        
        tc.clear();
        
        tc.addResponse(Expr.create("(ProtocolName (a1 v2) (a2 v3))").cons(Expr.create("(ProtocolName (r1 s2))")), true);
        assertNotNull(tc.sniff(p, root));
    }

    public void testAllowRequest() throws InhibitException {
        final ProtocolMockery tc = new ProtocolMockery(root, new IProtocolLogger.NullProtocolLogger());
        assertEquals(0, tc.getAllows().size());
        tc.allow(Expr.create("(ProtocolName)"));
        assertEquals(1, tc.getAllows().size());
        
        assertFalse(tc.hasError());
        assertEquals("", tc.getErrorMessages());
        
        
        IProtocolMessage p = new ProtocolMessage(new RootFactory().getRequestProtocol(), false);
        p.setValue("a1", "v2");
        p.setValue("a2", "v3");
        tc.sniff(p, root);
        assertEquals(1, tc.getAllows().size());
        assertFalse(tc.getErrorMessages(), tc.hasError());
        assertEquals("", tc.getErrorMessages());

        tc.clear();
        assertEquals(0, tc.getAllows().size());

        tc.allow(Expr.create("(ProtocolName (a1 v2) (a2 v3))"));
        assertEquals(1, tc.getAllows().size());
        tc.sniff(p, root);
        assertEquals(1, tc.getAllows().size());
        assertFalse(tc.getErrorMessages(), tc.hasError());
        assertEquals("", tc.getErrorMessages());
        tc.sniff(p, root);
        assertEquals(1, tc.getAllows().size());
        assertFalse(tc.getErrorMessages(), tc.hasError());
        assertEquals("", tc.getErrorMessages());

        p.setValue("a1", "v1");
        tc.sniff(p, root);
        assertTrue(tc.getErrorMessages(), tc.hasError());
        assertEquals(1, tc.getAllows().size());
        
        
    }
    
}



class Hndlr implements IHandler {  
    private final IProtocolLayoutFactory factory;
    private final IHandler[] upperHandlers;
    private boolean active = true;

    Hndlr(IProtocolLayoutFactory f, IHandler...upperHandlers) {
        this.factory = f;
        this.upperHandlers = upperHandlers;
    }
    @Override
    public IProtocolMessage createReceive() {
        return new ProtocolMessage(receiveType(), false);
    }

    @Override
    public IProtocolMessage createSend() {
        return new ProtocolMessage(sendType(), false);
    }

    @Override
    public String getProtocolName() {
        return factory.getName();
    }

    @Override
    public IHandler[] getUpperHandlers() {
        return upperHandlers;
    }

    @Override
    public IProtocolLayoutType receiveType() {
        return factory.getRequestProtocol();
    }

    @Override
    public void sendToLower(IProtocolMessage p, boolean realtime) {
        // TODO Auto-generated method stub
    }

    @Override
    public IProtocolLayoutType sendType() {
        return factory.getResponseProtocol();
    }
    @Override
    public IProtocolLayoutFactory getFactory()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public IProtocolState getProtocolState()
    {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String getHeaderFieldName()
    {
        // TODO Auto-generated method stub
        return null;
    }
	@Override
	public QualifiedName getQualifiedName() {
		return new QualifiedName();
	}
    @Override
    public void activate()
    {
    	active = true;
    }
    @Override
    public void inactivate()
    {
    	active = false;
    }
	@Override
	public boolean isActive() {
		return active;
	}
	@Override
	public List<String> getSwitchValues() {
		return new ArrayList<String>();
	}
	@Override
	public String switchValueStrOf(IUpperHandler uh) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isServer() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isClient() {
		// TODO Auto-generated method stub
		return false;
	}

}

class RootFactory extends ProtocolLayoutFactory {
    protected RootFactory() {
        super("ProtocolName", false);
        protocols(
          request(
            args(
              argByte("a1", 0, 
                values(
                  value("v1", 0x0),
                  value("v2", 0x1)
                )
              ),
              argByte("a2", 1, 
                values(
                  value("v3", 0x0),
                  value("v4", 0x1)
                )
              )
            )
          ),
          response(
            args(
              argByte("r1", 0,
                values(
                  value("s1", 0x0),
                  value("s2", 0x1)
                )
              )
            )
          )
        );
    }
    
}
class Child1Factory extends ProtocolLayoutFactory {
    protected Child1Factory() {
        super(ProtocolMockeryTest.CHILD1_PROTOCOL_NAME, false);
        protocols(
          request(
            args(
              argByte("c1", 0, 
                values(
                  value("cv1", 0x0),
                  value("cv2", 0x1)
                )
              ),
              argByte("c2", 0, 
                values(
                  value("cv3", 0x0),
                  value("cv4", 0x1)
                )
              )
            )
          ),
          response()
        );
    }
    
}