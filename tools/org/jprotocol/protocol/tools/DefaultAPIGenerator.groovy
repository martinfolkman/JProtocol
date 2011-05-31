package org.jprotocol.protocol.tools
 
       
import org.jprotocol.codegen.JavaGenerator
import org.jprotocol.codegen.NameFormatter
import org.jprotocol.example.api.MyRootProtocol_Request_API;
import org.jprotocol.example.api.MyLeafProtocolA_Request_API.MyLeafProtocolA_Request_API_Test;
import org.jprotocol.example.dsl.MyLeafProtocolA;
import org.jprotocol.example.dsl.MyRootProtocol;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.dsl.StringBuilderProtocolMessage;
import org.jprotocol.framework.dsl.IProtocolLayoutType.Direction
import org.jprotocol.framework.handler.IProtocolSniffer;
import org.jprotocol.framework.handler.IProtocolState;
import org.jprotocol.framework.handler.Handler.Type;
 
public class DefaultAPIGenerator extends AbstractAPIGenerator {
	 
	final factory
	public static void create(protocolLayouts, pack, dir) {
		 protocolLayouts.each {
 			 new DefaultAPIGenerator(it, it.requestProtocol, pack, dir)
			 new DefaultAPIGenerator(it, it.responseProtocol, pack, dir)
			 new DefaultHandlerGenerator(it, pack, dir)
		 }
		 new DefaultAPIFactoryGenerator(protocolLayouts, Direction.Request, pack, dir)
		 new DefaultAPIFactoryGenerator(protocolLayouts, Direction.Response, pack, dir)
	}
	
	public DefaultAPIGenerator(factory, protocol, String pack, String dir) {
		super(protocol, pack + ".api")
		this.factory = factory
		println("Saving to ${new File(dir)}")
		generate()
		save(dir)
	}
	
	def additionalConstructors() {
		 
		block("static ${name}_Test createTest()") {
			line "return new ${name}_Test(new StringBuilderProtocolMessage(new ${factory.class.name}().getRequestProtocol()))"
		}
	
	} 

	public String getInterfaceType(String name) {
		println "============================================" + name
		""
	}
	
}

class DefaultAPIFactoryGenerator extends JavaGenerator {
	DefaultAPIFactoryGenerator(protocolLayouts, Direction direction, pack, dir) {
		super(pack + ".api", "${direction}APIFactory")
		stdPackage()
		stdJavaDoc()
		block("public class $name") {
			protocolLayouts.each {
				final classNameUtil = new ClassNameUtil(it)
				String className = classNameUtil.requestApiClass
				if (direction == Direction.Response) {
					className = classNameUtil.responseApiClass
				}
				block("public ${className} ${className}()") {
					 line "return ${className}.createTest()"
				}
			} 
		}
		save(dir)
	}
		
} 
class DefaultHandlerGenerator extends JavaGenerator {
	final layout 
	final classNameUtil
	DefaultHandlerGenerator(layout, pack, dir) { 
		super(pack + ".handler", "Default" + NameFormatter.formatName(layout.name) + "Handler")
		this.layout = layout
		this.classNameUtil = new ClassNameUtil(layout)
		stdPackage()
		line "import org.jprotocol.framework.dsl.*"
		line "import org.jprotocol.framework.handler.*"
		line "import ${pack}.api.*"
		stdJavaDoc()
		block("public class $name extends Handler<${requestApiClass}, ${responseApiClass}>") {
			
			block("protected ${name}(Type type, boolean msbFirst, String upperHeaderRequestFieldName, String upperHeaderResponseFieldName, int lowerHeaderRequestValue, int lowerHeaderResponseValue, IProtocolState protocolState, IProtocolSniffer sniffer)") {
				line "super(new ${layout.class.name}(), type, msbFirst, upperHeaderRequestFieldName, upperHeaderResponseFieldName, lowerHeaderRequestValue, lowerHeaderResponseValue, protocolState, sniffer)"
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
		classNameUtil.getRequestApiClass()
	}
	String getResponseApiClass() { 
		classNameUtil.getResponseApiClass()
	}
}
 
class ClassNameUtil {
	final layout
	ClassNameUtil(layout) {
		this.layout = layout
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