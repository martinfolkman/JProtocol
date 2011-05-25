package org.jprotocol.protocol.tools

import org.jprotocol.example.dsl.MyProtocols

class ExampleGenerator {
	public static void main(String[] args) {
		generate(new MyProtocols().getProtocolLayouts(), "org.jprotocol.example", "src")
	}

	private static void generate(layouts, String pack, String dir) {
		DefaultAPIGenerator.create(layouts, pack, dir)
	}	 
	 

}
