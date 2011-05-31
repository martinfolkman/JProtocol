package org.jprotocol.framework.handler;

import java.util.Map;

import junit.framework.TestCase;

import org.jprotocol.framework.dsl.INameValuePair;
import org.jprotocol.framework.dsl.IProtocolLayoutType;
import org.jprotocol.framework.dsl.ProtocolLayoutFactory;


public class ProtocolStateTest extends TestCase {
    public void testProtocolState() {
        IProtocolState ps = new ProtocolState();
        IProtocolLayoutType t1 = new TestFactory().getRequestProtocol();
        IProtocolLayoutType t2 = new TestFactory().getRequestProtocol();
        Map<String, INameValuePair> copy0 = ps.makeFlattenedSnapshot();
        assertNull(copy0.get("TF Request:a"));
        IProtocolState psCopy0 = ps.makeCopy();
        assertEquals("v1", psCopy0.getValue(t1, t1.argOf("a")).getName());
        ps.setValue(t1, t1.argOf("a"), "v2");
        assertEquals("v2", ps.getValue(t1, t1.argOf("a")).getName());
        Map<String, INameValuePair> copy1 = ps.makeFlattenedSnapshot();
        assertEquals("v1", psCopy0.getValue(t1, t1.argOf("a")).getName());
        assertEquals("v2", copy1.get("TF Request:a").getName());
        assertEquals("v2", ps.getValue(t2, t2.argOf("a")).getName());
        ps.setValue(t1, t1.argOf("a"), "v1");
        assertEquals("v1", ps.getValue(t1, t1.argOf("a")).getName());
        assertEquals("v2", copy1.get("TF Request:a").getName());
        Map<String, INameValuePair> copy2 = ps.makeFlattenedSnapshot();
        assertEquals("v1", copy2.get("TF Request:a").getName());

        
        
        
        
        
    }
    
}

class TestFactory extends ProtocolLayoutFactory {

    protected TestFactory() {
        super("TF", false);
        protocols(
          request(vArg("a", values(value("v1", 0), value("v2", 1)))),
          response()
        );
    }
    
}