package org.jprotocol.example.dsl;

import org.jprotocol.framework.dsl.ProtocolLayouts;

public class MyProtocols extends ProtocolLayouts {
	public MyProtocols() {
		super(new MyLeafProtocolA(), 
			  new MyLeafProtocolB(), 
			  new MyMiddleProtocolA(), 
			  new MyMiddleProtocolB(), 
			  new MyRootProtocol());
	}
	
	
	public static void main(String[] args) {
		System.out.println(new MyProtocols().getProtocolLayouts().size());
	}
}
