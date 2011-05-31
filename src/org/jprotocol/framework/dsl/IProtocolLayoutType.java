package org.jprotocol.framework.dsl;


public interface IProtocolLayoutType {
    public static enum Direction {
        Request("Request"), 
        Response("Response"),
        Layout("Layout");
        private final String name;
        Direction(String name) {
            this.name = name;
        }
        @Override
        public String toString() {
            return name;
        }
    } 
    String getName();
    String getProtocolName();
    
    IArgumentType[] getArguments();
    
    IArgumentType argOf(String name);
    
    IArgumentType indexedArgOf(String argName, IArgumentType a, int index);

    int getSizeInBytes();
    int getSizeInBits();
    
    String valueNameOf(String argName, int value);

    boolean hasPayload();
    
    int getPayloadStartIndex();

    IDiscriminator getDiscriminator();
    
    Direction getDirection();
    
    int getTargetTypeOffset();
    boolean hasTargetType();
}
