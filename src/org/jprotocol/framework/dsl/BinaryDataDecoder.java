package org.jprotocol.framework.dsl;


class BinaryDataDecoder extends Decoder {
    BinaryDataDecoder(IProtocolMessage protocol) {
		super(protocol);
	}

	@Override
    NameValuePair decodeToNV(IArgumentType argType) {
        return null;
    }
}
