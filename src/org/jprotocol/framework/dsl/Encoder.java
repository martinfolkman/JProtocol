package org.jprotocol.framework.dsl;

abstract class Encoder extends EncoderDecoder {
	Encoder(IProtocolMessage protocol) {
		super(protocol);
	}
    abstract boolean encode(IArgumentType argType, String value);
}
