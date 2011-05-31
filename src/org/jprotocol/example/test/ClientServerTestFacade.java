package org.jprotocol.example.test;

import org.jprotocol.example.handler.client.ClientFacade;
import org.jprotocol.example.handler.server.ServerFacade;
import org.jprotocol.framework.facade.AbstractFacade;
import org.jprotocol.framework.handler.IFlushable;

public class ClientServerTestFacade {
	public final ClientFacade client;
	public final ServerFacade server;

	public ClientServerTestFacade() {
		FlushableClientServer cf = new FlushableClientServer();
		FlushableClientServer sf = new FlushableClientServer();
		client = createClientFacade(cf);
		server = createServerFacade(sf);
		cf.setTarget(server);
		sf.setTarget(client);
		
	}

	/**
	 * Override to provide a specialized version of ClientFacade
	 * @param flushable
	 * @return
	 */
	protected ClientFacade createClientFacade(IFlushable flushable) {
		return new ClientFacade(flushable);
	}
	/**
	 * Override to provide a specialized version of ServerFacade
	 * @param flushable
	 * @return
	 */
	protected ServerFacade createServerFacade(IFlushable flushable) {
		return new ServerFacade(flushable);
	}
}

class FlushableClientServer implements IFlushable {

	private AbstractFacade server;

	@Override
	public void flush(byte[] data) {
		server.receive(data);
	}

	public void setTarget(AbstractFacade server) {
		this.server = server;
	}
	
}
