package org.jprotocol.protocol.tools;

import java.util.Set;

import org.jprotocol.framework.dsl.IEnumeration;


public interface IEnumClass {
	String getClassName();
	String getPackageName();
	Set<IEnumeration> getEnums();
	IEnumeration enumOf(String key);
	String keyOf(IEnumeration e);
}
