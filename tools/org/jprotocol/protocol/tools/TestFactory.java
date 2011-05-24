package org.jprotocol.protocol.tools;

import static org.jprotocol.quantity.Quantity.quantity;

import org.jprotocol.framework.dsl.MemoryLayoutFactory;
import org.jprotocol.quantity.Unit;

class TestFactory extends MemoryLayoutFactory {
	TestFactory() {
		this("TestFactory");
	}
	TestFactory(String name) {
		super(name);
		  layout(
		    argByte("Switch", size(1), offset(0),
		      value("Off", 0),
		      value("On", 1,
		        args(
		          argByte("Count", size(1), offset(1)),
		          argStr("Str", quantity(2, Unit.byteSize), quantity(5, Unit.byteSize))
		        )
		      )
		    ),
            iArg("Indexed", 10, 
		      argByte("iArg0", size(1), offset(10)),
		      argByte("iArg1", size(1), offset(11), 
		        value("Good", 0),
		        value("Bad", 1)
		      ),
		      iArg("Indexed2", 2,
		        argByte("iArg3", size(1), offset(12))
		      )
        	),
		    argInt("Real", offset(100), 0.0, 1.0, Unit.mV),
		    vArg("vArg", value("vValue1", 0), value("vValue2", 1))
		  );
	}
}