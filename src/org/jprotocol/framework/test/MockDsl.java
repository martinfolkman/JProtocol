package org.jprotocol.framework.test;

import org.jprotocol.framework.dsl.AbstractDecoratedProtocolMessage;
import org.jprotocol.framework.handler.QualifiedName;
import org.jprotocol.framework.list.Expr;


public class MockDsl {
	private final String name;
	private final ProtocolMockery mockery;
	private final MockDsl parent;
	public MockDsl(ProtocolMockery mockery) {
		this(mockery, null, null);
	}
	public MockDsl(ProtocolMockery mockery, String name, MockDsl parent) {
		this.mockery = mockery;
		this.name = name;
		this.parent = parent;
	}
	public MockDsl allows(AbstractDecoratedProtocolMessage...protocols) {
		for (AbstractDecoratedProtocolMessage p: protocols) {
			mockery.allow(Expr.create(p.toString()), getQualifiedName());
		}
		return this;
	}
	public MockDsl expects(AbstractDecoratedProtocolMessage...protocols) {
		for (AbstractDecoratedProtocolMessage p: protocols) {
			mockery.expect(Expr.create(p.toString()), getQualifiedName());
		}
		return this;
	}
	public MockDsl send(AbstractDecoratedProtocolMessage protocol) {
		mockery.send(protocol.getProtocol(), getQualifiedName());
		return this;
	}
	private QualifiedName getQualifiedName() {
		QualifiedName qn = new QualifiedName();
		if (parent != null) {
			qn = parent.getQualifiedName();
		}
		if (name == null) {
			return qn;
		}
		return qn.append(name);
	}
	public MockDsl context(String name) {
		return new MockDsl(mockery, name, this);
	}
}

