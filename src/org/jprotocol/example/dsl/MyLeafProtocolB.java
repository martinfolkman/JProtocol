package org.jprotocol.example.dsl;

import org.jprotocol.framework.dsl.ProtocolLayoutFactory;

public class MyLeafProtocolB extends ProtocolLayoutFactory {
	public MyLeafProtocolB() {
		super("MyLeafProtocolB");
		protocols(
		  request(
			argInt("Leaf", offset(0))
		  ), 
		  response()
		);
	}
	
}
