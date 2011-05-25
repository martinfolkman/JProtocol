package org.jprotocol.protocol.tools

import org.jprotocol.example.dsl.MyProtocols

class ExampleGenerator {
	public static void main(String[] args) {
		DefaultAPIGenerator.create(new MyProtocols().getProtocolLayouts(), "org.jprotocol.example.api", "src")
	}
	 
	 

}
