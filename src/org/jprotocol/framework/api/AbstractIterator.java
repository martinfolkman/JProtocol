package org.jprotocol.framework.api;

import java.util.Iterator;


public abstract class AbstractIterator<T> implements Iterator<T> {
    
    protected AbstractIterator() {
    }
    

    @Override public final void remove() {
        assert false: "remove is not supported";
    }
    
	public static int[] indexArrayOf(int[] indexes, int index) {
    	int[] result = new int[indexes.length + 1];
    	System.arraycopy(indexes, 0, result, 0, indexes.length);
    	result[indexes.length] = index;
    	return result;
	}

    
}

