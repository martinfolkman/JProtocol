package org.jprotocol.framework.dsl;


/**
 * Decodes data in the protocols byte and decodes it to an integer.
 * @author eliasa01
 *
 */
class IntDecoder extends Decoder {
    IntDecoder(IProtocolMessage protocol) {
		super(protocol);
	}

	@Override
    INameValuePair decodeToNV(final IArgumentType argType) {
        if (!isInt(argType)) return null;
        int result = 0;
        int[] data = protocol.getDataAsInts();
        if (argType.getSizeInBytes() >= 4 && protocol.isMsbFirst()) {
            for (int i = 0; i < Math.min(4, argType.getSizeInBytes()); i++) {
                result += data[argType.getStartByteIndex() + i] << ((argType.getSizeInBytes() - 1 - i) * 8);
            }
        } else {
        	
            for (int i = 0; i < argType.getSizeInBytes(); i++) {
                result += data[argType.getStartByteIndex() + i] << (i * 8);
            }
        }
        if (argType.isEnumType())  {
            return argType.nvpOf(result);
        }
        return new NameValuePair(argType.nameOf(result), result);
    }
}
