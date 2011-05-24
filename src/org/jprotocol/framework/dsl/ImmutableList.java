package org.jprotocol.framework.dsl;

import java.util.Iterator;
import java.util.List;

public class ImmutableList<T> implements Iterable<T> {
	private final List<T> target;
	private final int offset;
	public ImmutableList(List<T> target) {
		this(target, 0);
	}
	public ImmutableList(List<T> target, int offset) {
		this.target = target;
		this.offset = offset;
	}
	@Override
	public Iterator<T> iterator() {
		return new IteratorImpl<T>(target, offset);
	}
	
	public int size() {
		return target.size() - offset;
	}
	
	@Override public boolean equals(Object o) {
		if (o instanceof ImmutableList<?>) {
			ImmutableList<?> other = (ImmutableList<?>) o;
			return target.equals(other.target) && offset == other.offset;
		}
		return false;
	}
	@Override public int hashCode() {
		return target.hashCode() + offset << 16;
	}
	@Override public String toString() {
		StringBuffer buf = new StringBuffer();
		for (T value: this) {
			if (buf.length() != 0) {
				buf.append(", ");
			}
			buf.append(value);
		}
		return buf.toString();
	}
}

class IteratorImpl<T> implements Iterator<T> {
	private final List<T> list; 
	private int currIx;
	IteratorImpl(List<T> list, int offset) {
		this.list = list;
		this.currIx = offset;
	}
	@Override
	public boolean hasNext() {
		return currIx < list.size();
	}

	@Override
	public T next() {
		T value = list.get(currIx);
		currIx++;
		return value;
	}

	@Override
	public void remove() {
		throw new IllegalStateException("Not allowed in immutable list");
	}

}
