package org.jprotocol.framework.handler;

import static org.jprotocol.util.Contract.implies;
import static org.jprotocol.util.Contract.notEmpty;
import static org.jprotocol.util.Contract.require;

public final class QualifiedName {
	public static final String SEPARATOR = "//"; 
	private final String qName;

	public QualifiedName() {
		this(SEPARATOR);
	}

	
	public QualifiedName(String qName) {
		this.qName = qName;
		require(notEmpty(qName));
		require(qName.length() >= SEPARATOR.length());
		require(qName.startsWith(SEPARATOR));
		implies(isRoot(), qName.equals(SEPARATOR));
		implies(qName.length() > SEPARATOR.length(), !qName.endsWith(SEPARATOR));
	}
	
	
	



	public boolean startsWith(QualifiedName otherQName) {
		return qName.startsWith(otherQName.qName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof QualifiedName) {
			return qName.equals(((QualifiedName)obj).qName);
		}
		return false;
	}
	@Override
	public int hashCode() {
		return qName.hashCode();
	}
	@Override
	public String toString() {
		return qName;
	}
	
	public String[] split() {
		return qName.split(SEPARATOR);
	}
	public QualifiedName append(String name) {
		require(name.indexOf(SEPARATOR) < 0, name);
		if (isRoot()) {
			return new QualifiedName(qName + name);
		}
		return new QualifiedName(qName + SEPARATOR + name);
	}
	
	private boolean isRoot() {
		return qName.length() == SEPARATOR.length();
	}
}
