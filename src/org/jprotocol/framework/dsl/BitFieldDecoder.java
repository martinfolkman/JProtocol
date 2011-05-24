package org.jprotocol.framework.dsl;



class BitFieldDecoder extends Decoder {
    BitFieldDecoder(IProtocolMessage protocol) {
		super(protocol);
	}

	@Override
    INameValuePair decodeToNV(IArgumentType argType) {
        if (!isBitField(argType)) return null;
        try {
        	final int value = BitFilterUtil.intOf(protocol.getDataAsInts(), argType.getOffset(), argType.getSizeInBits());
            if (argType.isEnumType())  {
                return argType.nvpOf(value);
            }
            return new NameValuePair(argType.nameOf(value), value);
        } catch (AssertionError e) {
            throw new AssertionError("Protocol: " + protocol.getProtocolType().getName() + "\n data: " + protocol.readableData() + "\n" + e.getMessage());
        }
    }
    
}
