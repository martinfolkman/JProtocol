package org.jprotocol.framework.dsl;

import junit.framework.TestCase;


public class ProtocolFactoryTest extends TestCase {
    public void testConstruction() {
        ProtocolLayoutFactory factory = new TestProtocolFactory("Name", true);
        assertEquals("Name", factory.getName());
        assertEquals("Name Request", factory.getRequestProtocol().getName());
        assertEquals("Name Response", factory.getResponseProtocol().getName());
        assertEquals("ArgName1", factory.getRequestProtocol().getArguments()[0].getName());
        assertEquals("ArgName2", factory.getResponseProtocol().getArguments()[0].getName());
    }
}

class TestProtocolFactory extends ProtocolLayoutFactory {
    protected TestProtocolFactory(String name, boolean includePayload) {
        super(name, includePayload);
        protocols(
          request(
            arg("ArgName1", 1, 0)
          ), 
          response(
            arg("ArgName2", 1, 0)
          )
        );
    }
}