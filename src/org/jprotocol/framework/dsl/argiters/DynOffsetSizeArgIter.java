package org.jprotocol.framework.dsl.argiters;

import org.jprotocol.framework.dsl.AbstractArgumentType;
import org.jprotocol.framework.dsl.IArgumentType;
import org.jprotocol.framework.dsl.IProtocolMessage;


public class DynOffsetSizeArgIter extends AbstractArgIter {
    private int offset = -1;
    private int size;

    public DynOffsetSizeArgIter(IProtocolMessage protocol) {
        super(protocol);
        iterate();
        offset = Math.max(offset, 0);
    }

    @Override
    protected boolean iter(IArgumentType arg) {
        size = Math.max(size, arg.getOffset() + arg.getSizeInBits());
        if (offset < 0) {
            offset = arg.getOffset();
        } else {
            offset = Math.min(offset, arg.getOffset());
        }
        return true;
    }
    public int getOffsetInBits() {
        return offset;
    }

    public int getSizeInBytes() {
        return AbstractArgumentType.byteSizeOf(getSizeInBits());
    }
    public int getSizeInBits() {
        return size;
    }
}
