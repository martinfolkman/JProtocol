package org.jprotocol.framework.realtime;

import org.jprotocol.framework.dsl.AbstractDecoratedProtocolMessage;

public interface IRealtimeStream {
	
	/**
	 * Retreive the next message from the Stream.
	 * 
	 * @param frameCounter Increases for every message. The step size depends on
	 * the frequency. Same frameCounter is sent to all implementations and could
	 * be used to to create data as a function of frameCounter or to synchronize streams.
	 * 
	 * @return message
	 */
	AbstractDecoratedProtocolMessage nextMessage(int frameCounter);
}
