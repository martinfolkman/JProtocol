package org.jprotocol.example.dsl;

import org.jprotocol.framework.dsl.ProtocolLayoutFactory;

public class MyLeafProtocolA extends ProtocolLayoutFactory {
	public MyLeafProtocolA() {
		super("MyLeafProtocolA");
		protocols(
		  request(
			argInt("Leaf", offset(0))
		  ), 
		  response()
		);
	}
	
}
