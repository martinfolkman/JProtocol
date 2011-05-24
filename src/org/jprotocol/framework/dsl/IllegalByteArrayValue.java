package org.jprotocol.framework.dsl;


@SuppressWarnings("serial")
public class IllegalByteArrayValue extends RuntimeException {
    private final IArgumentType arg;

    public IllegalByteArrayValue(String msg, IArgumentType arg) { 
        super(msg);
        this.arg = arg;
    }
    public IArgumentType getArg() {
        return arg;
    }
}
