package org.jprotocol.framework;

import java.lang.annotation.*;


/**
 * Annotation used to track the protocol version used to create DSL and handlers
 * for a protocol.
 * @author bergop01
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtocolSource {
	String protocolName();
	String protocolSpecificationName();
	String version();
	String date();
	String docNumber();
}
