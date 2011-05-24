package org.jprotocol.framework.dsl;

import java.io.UnsupportedEncodingException;

class StringDecoder extends Decoder {
    StringDecoder(IProtocolMessage protocol) {
		super(protocol);
	}
	@Override
    NameValuePair decodeToNV(IArgumentType argType) {
        if (!isStr(argType)) return null;
        byte[] d = new byte[argType.getSizeInBytes()];
        System.arraycopy(protocol.getData(), argType.getOffset() / 8, d, 0, argType.getSizeInBytes());
        try {
            return new NameValuePair(readUtf8String(d), 0);
        } catch (UnsupportedEncodingException e) {
            throw new ProtocolException(e.getMessage());
        }
    }
    private String readUtf8String (byte[] d) throws UnsupportedEncodingException {
        for (int i = 0; i < d.length; ++i) {
            if (d[i] == 0) {
                setRestToNull(d, i);             
            }
        }
        return new String (d, UTF_8).trim();
    }

    private void setRestToNull(byte[] d, int i) {
        for(int j = i; j < d.length; ++j){
            d[j] = 0;
        }
    }

}
