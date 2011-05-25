package org.jprotocol.protocol.tools
      
import org.jprotocol.example.dsl.MyProtocols
 
public class DefaultAPIGenerator extends AbstractAPIGenerator {
	 
	public static void create(protocolLayouts, pack, dir) {
		 protocolLayouts.each {
 			 new DefaultAPIGenerator(it.requestProtocol, pack, dir)
			 new DefaultAPIGenerator(it.responseProtocol, pack, dir)
		 }
	}
	
	public DefaultAPIGenerator(protocol, String pack, String dir) {
		super(protocol, pack)
		println("Saving to ${new File(dir)}")
		generate()
		save(dir)
	}
	
	
	public String getInterfaceType(String name) {
		""
	}
	
}
