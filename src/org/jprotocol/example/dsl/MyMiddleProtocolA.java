package org.jprotocol.example.dsl;

import org.jprotocol.framework.dsl.ProtocolLayoutFactory;
 
public class MyMiddleProtocolA extends ProtocolLayoutFactory {
	public MyMiddleProtocolA() {
		super("MyMiddleProtocolA");
		protocols(
		  request(
			argByte("MiddleHeader", offset(0), value("X", 1), value("Z", 2)),
			argByte("MiddleSwitch", offset(1), value("A", 1), value("B", 2))
		  ), 
		  response(
			argByte("MiddleHeader", offset(0), value("X", 1), value("Z", 2)),
			argByte("MiddleSwitch", offset(1), value("A", 1), value("B", 2))
		  )
		);
	}
	
}
