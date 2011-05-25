package org.jprotocol.example.dsl;

import org.jprotocol.framework.dsl.ProtocolLayoutFactory;

public class MyLeafProtocolA extends ProtocolLayoutFactory {
	protected MyLeafProtocolA() {
		super("MyLeafProtocolA");
		protocols(
		  request(
			argInt("LeafA", offset(0))
		  ), 
		  response()
		);
	}
	
}
