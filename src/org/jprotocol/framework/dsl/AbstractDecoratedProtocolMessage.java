package org.jprotocol.framework.dsl;



public class AbstractDecoratedProtocolMessage extends AbstractDecorated {

    protected boolean strBuilder;
	protected AbstractDecoratedProtocolMessage(IProtocolMessage protocol) {
		this(protocol, false);
	}
	protected AbstractDecoratedProtocolMessage(IProtocolMessage protocol, boolean strBuilder) {
        super(protocol);
        this.strBuilder = strBuilder;
    }

    public byte[] getData() {
        return protocol.getData();
    }
    public byte[] getData(int startIndex) {
        return protocol.getData(startIndex);
    }
    public IProtocolMessage getProtocol() {
        return protocol;
    }
    
    public boolean equals(final Object o)
    {
        if (null == o) return false;
        if (o instanceof AbstractDecoratedProtocolMessage)
        {
            return getProtocol().equals(((AbstractDecoratedProtocolMessage)o).getProtocol());
        }
        return false;
    }
    public int hashCode() {
        return getProtocol().hashCode();
    }
    
    @Override 
    public String toString() {
        if (strBuilder) {
            return getProtocol().toString();
        }
        return super.toString();
    }

}
