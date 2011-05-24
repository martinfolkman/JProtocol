package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.notNull;
import static org.jprotocol.util.Contract.require;

import org.jprotocol.framework.dsl.IProtocolLayoutType.Direction;




/**
 * This class provides a DSL for specifying protocol data. It immutable. 
 * The class must be overridden to define request and response protocol data.
 * <br>
 * </code></pre>
 * @see IProtocolLayoutType
 * @see IArgumentType
 * @see INameValuePair
 * @see IProtocolMessage
 */
abstract public class ProtocolLayoutFactory extends AbstractMemoryLayoutFactory implements IProtocolLayoutFactory { 
    
    protected final static boolean INCLUDE_PAYLOAD = true;
    
    private IProtocolLayoutType request;
    private IProtocolLayoutType response;
    
    /**
     * @param includePayload include payload or sub protocols
     */
    protected ProtocolLayoutFactory(String name, boolean includePayload) {
        super(name, includePayload);
    }
    protected ProtocolLayoutFactory(String name, IProtocolLayoutType request, IProtocolLayoutType response) {
        super(name, !INCLUDE_PAYLOAD);
        check(request);
        check(response);
        this.request = request;
        this.response = response;
        
    }
    
    
    
    private void check(IProtocolLayoutType type) {
    	require(notNull(type));
    	require(notNull(type.getName()));
//    	require(type.getProtocolName().equals(getName()), "The protocol name is not consistant, expected: ", getName(), ", actual: ", type.getProtocolName());
//        require(type.getName().startsWith(getName()), "The protocol: ", getName(), ", is not a prefix of the ", type.getDirection(), ": ", type.getName());
        require(type.getName().endsWith(type.getDirection().toString()), "The " + type.getDirection() + ": ", type.getName(), " doesn't end with: ", type.getDirection());
    }
    
    final public IProtocolLayoutType getResponseProtocol() {
        return response;
    }
    final public IProtocolLayoutType getRequestProtocol() {
        return request;
    }

    
    public IProtocolLayoutType[] getProtocolTypes() {
        return new IProtocolLayoutType[]{getRequestProtocol(), getResponseProtocol()};
    }

    /**
     * The root of this DSL
     * @param protocols
     */
    protected void protocols(IRequestProtocolType hts, IResponseProtocolType sth) {
        request = hts;
        response = sth;
    }
    
    protected IRequestProtocolType request(IArgumentType...argTypes) {
        return new RequestProtocolType(getName() + " " + Direction.Request, getName(), argTypes);
    }
    protected IResponseProtocolType response(IArgumentType...argTypes) {
        return new ResponseProtocolType(getName() + " " + Direction.Response, getName(), argTypes);
    }


    public interface IRequestProtocolType extends IProtocolLayoutType {
//        
    }
    public interface IResponseProtocolType extends IProtocolLayoutType {
//        
    }
}

class RequestProtocolType extends ProtocolLayoutType implements ProtocolLayoutFactory.IRequestProtocolType {
    public RequestProtocolType(String name, String protocolName, IDiscriminator discriminator, IArgumentType... args) {
        super(name, protocolName, Direction.Request, discriminator, args);
    }
    public RequestProtocolType(String name, String protocolName, IArgumentType... args) {
        super(name, protocolName, Direction.Request, args);
    }    
    
}
class ResponseProtocolType extends ProtocolLayoutType implements ProtocolLayoutFactory.IResponseProtocolType {
    public ResponseProtocolType(String name, String protocolName, IDiscriminator discriminator, IArgumentType... args) {
        super(name, protocolName, Direction.Response, discriminator, args);
    }
    public ResponseProtocolType(String name, String protocolName, IArgumentType... args) {
        super(name, protocolName, Direction.Response, args);
    }    
}
