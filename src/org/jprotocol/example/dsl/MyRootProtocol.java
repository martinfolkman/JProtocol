package org.jprotocol.example.dsl;

import org.jprotocol.framework.dsl.ProtocolLayoutFactory;

public class MyRootProtocol extends ProtocolLayoutFactory {
	public MyRootProtocol() {
		super("MyRootProtocol");
		protocols(
		  request(
			argInt("RootHeaderA", offset(0)),
			argByte("RootSwitch", 4, value("A", 0), value("B", 1))
		  ), 
		  response(
			argInt("RootHeaderA", offset(0)),
			argByte("RootSwitchResp", 4, value("A", 0), value("B", 1))
		  )
		);
	}
	
}
