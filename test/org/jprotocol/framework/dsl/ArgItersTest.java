package org.jprotocol.framework.dsl;

import junit.framework.TestCase;

import org.jprotocol.framework.dsl.argiters.GetAllArgsIter;





public class ArgItersTest extends TestCase {
    public void testGetAllArgs() {
        IProtocolLayoutFactory factory = new TestGetAllArgsFactory();
        IProtocolMessage p = new ProtocolMessage(factory.getRequestProtocol(), false);
        GetAllArgsIter iter;
        iter = new GetAllArgsIter(p);
        assertEquals(1, iter.getArgs().length);
        
        p.setValue("arg1", "On"); 
        iter = new GetAllArgsIter(p);
        assertEquals(14, iter.getArgs().length);
        assertEquals("arg1", iter.getArgs()[0].getName());
        assertEquals("arg2", iter.getArgs()[1].getName());

        for (int i = 0; i < 6; i++) {
            testIndexed(iter.getArgs(), i);
        }
        
    }
    private void testIndexed(IArgumentType[] args, int index) {
        int arrayIndex = 2 * index;
        assertEquals("arg3", args[2 + arrayIndex].getName());
        assertEquals(16 + index * 40, args[2 + arrayIndex].getOffset());
        assertEquals("arg4", args[3 + arrayIndex].getName());
        assertEquals(48 + index * 40, args[3 + arrayIndex].getOffset());
    }
}

class TestGetAllArgsFactory extends ProtocolLayoutFactory {

    protected TestGetAllArgsFactory() {
        super("Name", true);
        protocols(
          request(
            argByte("arg1", 1, 0, 
              values(
                value("Off", 0x0),
                value("On", 0x1, 
                  args(
                    argByte("arg2", 1, 1),
                    iArg("iArg1", 2,
                      iArg("iArg2", 3,
                        args(
                          argInt("arg3", 2),
                          argByte("arg4", 1, 6, 
                            values(
                              value("v1", 0x0)
                            )
                          )
                        )
                      )
                    )
                  )
                )
              )
            )
          ),
          response()
        );
    }
    
}