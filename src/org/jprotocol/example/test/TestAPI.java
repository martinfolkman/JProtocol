package org.jprotocol.example.test;

import org.jprotocol.example.handler.client.ClientFacade;
import org.jprotocol.example.handler.server.ServerFacade;
import org.jprotocol.framework.facade.AbstractFacade;
import org.jprotocol.framework.handler.IFlushable;

public class TestAPI {
	public final ClientFacade client;
	public final ServerFacade server;

	TestAPI() {
		FlushableProxy cf = new FlushableProxy();
		FlushableProxy sf = new FlushableProxy();
		client = new ClientFacade(cf).init();
		server = new ServerFacade(sf).init();
		cf.setTarget(server);
		sf.setTarget(client);
		
	}
}

class FlushableProxy implements IFlushable {

	private AbstractFacade server;

	@Override
	public void flush(byte[] data) {
		server.receive(data);
	}

	public void setTarget(AbstractFacade server) {
		this.server = server;
	}
	
}
