package org.jprotocol.framework.dsl;

import static org.jprotocol.framework.dsl.BitFilterUtil.arrayOf;


class BitFieldEncoder extends Encoder {

    BitFieldEncoder(IProtocolMessage protocol) {
		super(protocol);
	}

	@Override
    boolean encode(IArgumentType argType, String value) {
        if (!isBitField(argType)) return false;
        protocol.setData(arrayOf(argType.valueOf(value), protocol.getDataAsInts(), argType.getOffset(), argType.getSizeInBits()), 0);
        return true;
    }
}
