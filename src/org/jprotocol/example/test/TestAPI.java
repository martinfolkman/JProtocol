package org.jprotocol.example.test;

import org.jprotocol.example.handler.client.ClientFacade;
import org.jprotocol.example.handler.server.ServerFacade;
import org.jprotocol.framework.handler.IFlushable;

public class TestAPI {
	public final ClientFacade client;
	public final ServerFacade server;

	TestAPI() {
		ClientFlushable cf = new ClientFlushable();
		ServerFlushable sf = new ServerFlushable();
		client = new ClientFacade(cf);
		server = new ServerFacade(sf);
		cf.setServer(server);
		sf.setClient(client);
		
	}
}

class ClientFlushable implements IFlushable {

	private ServerFacade server;

	@Override
	public void flush(byte[] data) {
		server.receive(data);
	}

	public void setServer(ServerFacade server) {
		this.server = server;
	}
	
}
class ServerFlushable implements IFlushable {

	private ClientFacade client;

	@Override
	public void flush(byte[] data) {
		client.receive(data);
	}

	public void setClient(ClientFacade client) {
		this.client = client;
	}
	
}