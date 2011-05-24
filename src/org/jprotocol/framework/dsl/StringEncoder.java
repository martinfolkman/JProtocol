package org.jprotocol.framework.dsl;

import java.io.UnsupportedEncodingException;

class StringEncoder extends Encoder {
    StringEncoder(IProtocolMessage protocol) {
		super(protocol);
	}

	@Override
    boolean encode(IArgumentType argType, String value) {
        if (!argType.isStr()) return false;
        try {
            protocol.setData(value.getBytes(EncoderDecoder.UTF_8), argType.getOffset() / 8);
        } catch (UnsupportedEncodingException e) {
            throw new ProtocolException(e.getMessage());
        }
        return true;
    }
}
