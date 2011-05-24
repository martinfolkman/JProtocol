package org.jprotocol.framework.dsl;

import java.util.List;

public class ImmutableByteList extends ImmutableList<Byte> {
	public ImmutableByteList(List<Byte> target) {
		super(target);
	}
	public ImmutableByteList(List<Byte> target, int offset) {
		super(target, offset);
	}
	
	public byte[] asArray() {
		byte[] r = new byte[size()];
		int i = 0;
		for (byte v: this) {
			r[i] = v;
			i++;
		}
		return r;
	}

}
