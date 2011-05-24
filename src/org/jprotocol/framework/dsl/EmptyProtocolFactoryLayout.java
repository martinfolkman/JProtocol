package org.jprotocol.framework.dsl;


public class EmptyProtocolFactoryLayout extends ProtocolLayoutFactory {

    private final static IProtocolLayoutFactory self = new EmptyProtocolFactoryLayout();
    public static IProtocolLayoutFactory instance() {
        return self;
    }
    private EmptyProtocolFactoryLayout() {
        super("Empty", false);
        protocols(
          request(),
          response()
        );
    }

}