package org.jprotocol.framework.dsl;

import java.util.List;

public class ImmutableIntegerList extends ImmutableList<Integer> {

	public ImmutableIntegerList(List<Integer> target) {
		super(target);
	}
	public ImmutableIntegerList(List<Integer> target, int offset) {
		super(target, offset);
	}
	
	public int[] asArray() {
		int[] r = new int[size()];
		int i = 0;
		for (int v: this) {
			r[i] = v;
			i++;
		}
		return r;
	}

}
