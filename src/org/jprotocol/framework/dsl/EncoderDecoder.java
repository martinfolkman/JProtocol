package org.jprotocol.framework.dsl;

abstract class EncoderDecoder {
    protected static final String UTF_8 = "UTF-8";
	protected final IProtocolMessage protocol;
    EncoderDecoder(IProtocolMessage protocol) {
    	this.protocol = protocol;
	}
	public boolean isStr(IArgumentType argType) {
        return argType.isStr();
    }
    public boolean isInt(IArgumentType argType) { 
        return !argType.isStr() && isByteAlligned(argType); 
    }
    public boolean isBitField(IArgumentType argType) { 
        return !argType.isStr() && !isByteAlligned(argType); 
    }
    
    private boolean isByteAlligned(IArgumentType argType) {
    	return (argType.getOffset() % 8 == 0) && (argType.getSizeInBits() % 8 == 0);    	
    }

}
