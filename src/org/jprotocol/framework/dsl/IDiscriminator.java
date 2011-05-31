package org.jprotocol.framework.dsl;


public interface IDiscriminator {
    boolean isInUse(IProtocolMessage protocol, IArgumentType argType);
}
