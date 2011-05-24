package org.jprotocol.framework.handler;

import java.util.Map;

import org.jprotocol.framework.dsl.IArgumentType;
import org.jprotocol.framework.dsl.INameValuePair;
import org.jprotocol.framework.dsl.IProtocolLayoutType;


/**
 * The protocol state is used in conjunction with virtual arguments.
 * @see com.sjm.protocol.framework.dsl.AbstractMemoryLayout
 * @author eliasa01
 *
 */
public interface IProtocolState {
    INameValuePair getValue(IProtocolLayoutType type, IArgumentType arg);
    void setValue(IProtocolLayoutType type, IArgumentType arg, String value);
    Map<String, INameValuePair> makeFlattenedSnapshot();
    String flattenNameOf(IProtocolLayoutType type, IArgumentType arg);
    IProtocolState makeCopy();
}
