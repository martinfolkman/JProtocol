/**
 * This package contains the generic classes and interfaces of the protocol framework. This framework allows
 * a developer to specify protocols with the help of a protocol DSL. 
 * The framework for specifying protocols consists of a semantic model defined in interfaces:
 * {@link IProtocolType}, {@link IArgType},  {@link INameValuePair}, {@link IProtocol} and {@link IProtocolFactory}
 * The DSL is put on top of this semantic model and is defined and implemented in {@link ProtocolFactory} and super classes.
 * 
 */

package org.jprotocol.framework.dsl;