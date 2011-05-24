package org.jprotocol.framework.dsl;

class IntEncoder extends Encoder {

    IntEncoder(IProtocolMessage protocol) {
		super(protocol);
	}

	@Override
    boolean encode(IArgumentType argType, String value) {
        if (!isInt(argType)) return false;
        int v = argType.valueOf(value);
        if (argType.getSizeInBytes() >= 4 && protocol.isMsbFirst()) {
            for (int i = Math.min(4, argType.getSizeInBytes()) - 1; i >= 0; i--) {
                protocol.setData(argType.getStartByteIndex() + i, (v & 0xff));
                v = v >> 8;
            }
        } else {
            for (int i = 0; i < argType.getSizeInBytes(); i++) {
                protocol.setData(argType.getStartByteIndex() + i, (v & 0xff));
                v = v >> 8;
            }
        }
        return true;
    }
}
