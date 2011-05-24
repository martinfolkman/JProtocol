package org.jprotocol.framework.dsl;


abstract class Decoder extends EncoderDecoder {
	Decoder(IProtocolMessage protocol) {
		super(protocol);
	}
    final String decode(IArgumentType argType) {
        INameValuePair nvp = decodeToNV(argType);
        if (nvp == null) return null;
        return nvp.getName();
    }
    abstract INameValuePair decodeToNV(IArgumentType argType);
}
