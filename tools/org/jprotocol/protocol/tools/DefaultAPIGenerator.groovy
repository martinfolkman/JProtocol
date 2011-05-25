package org.jprotocol.protocol.tools

      
import org.jprotocol.codegen.JavaGenerator
import org.jprotocol.codegen.NameFormatter
import org.jprotocol.example.api.MyRootProtocol_Request_API;
import org.jprotocol.example.dsl.MyRootProtocol;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolLayoutType.Direction
import org.jprotocol.framework.handler.IProtocolSniffer;
import org.jprotocol.framework.handler.IProtocolState;
import org.jprotocol.framework.handler.Handler.Type;
 
public class DefaultAPIGenerator extends AbstractAPIGenerator {
	 
	public static void create(protocolLayouts, pack, dir) {
		 protocolLayouts.each {
 			 new DefaultAPIGenerator(it.requestProtocol, pack, dir)
			 new DefaultAPIGenerator(it.responseProtocol, pack, dir)
			 new DefaultHandlerGenerator(it, pack, dir)
		 }
	}
	
	public DefaultAPIGenerator(protocol, String pack, String dir) {
		super(protocol, pack + ".api")
		println("Saving to ${new File(dir)}")
		generate()
		save(dir)
	}
	
	 
	public String getInterfaceType(String name) {
		""
	}
	
}

class DefaultHandlerGenerator extends JavaGenerator {
	final layout 
	DefaultHandlerGenerator(layout, pack, dir) { 
		super(pack + ".handler", "Abstract" + NameFormatter.formatName(layout.name) + "Handler")
		this.layout = layout
		stdPackage()
		line "import org.jprotocol.framework.dsl.*"
		line "import org.jprotocol.framework.handler.*"
		line "import ${pack}.api.*"
		stdJavaDoc()
		block("abstract public class $name extends Handler<${requestApiClass}, ${responseApiClass}>") {
			
			block("protected ${name}(Type type, boolean msbFirst, String headerFieldName, int headerReceiveValue, int headerSendValue, IProtocolState protocolState, IProtocolSniffer sniffer)") {
				line "super(new ${layout.class.name}(), type, msbFirst, headerFieldName, headerReceiveValue, headerSendValue, protocolState, sniffer)"
			}
		
	
			
			block("@Override public final ${requestApiClass} createRequest(IProtocolMessage p)") {
				line "return new ${requestApiClass}(p)"
			}
			block("@Override public final ${responseApiClass} createResponse(IProtocolMessage p)") {
				line "return new ${responseApiClass}(p)"
			}

		}
		save(dir) 
	}
	
	String getRequestApiClass() {
		apiClassNameOf(Direction.Request)
	}
	String getResponseApiClass() {
		apiClassNameOf(Direction.Response)
	}
	String apiClassNameOf(Direction dir) {
		"${layout.name}_${dir}_API"
	}
}