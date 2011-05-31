package org.jprotocol.framework.dsl;

import static java.util.Arrays.asList;

import java.util.List;

public class ProtocolLayouts {
	private final List<IProtocolLayoutFactory> protocolLayouts;
	public ProtocolLayouts(IProtocolLayoutFactory...layouts) {
		System.out.println(layouts.length);
		this.protocolLayouts = asList(layouts);
		for (IProtocolLayoutFactory plf: layouts) {
			System.out.println(plf.getName());
		}
	}
	
	public List<IProtocolLayoutFactory> getProtocolLayouts() {
		return protocolLayouts;
	}
}
