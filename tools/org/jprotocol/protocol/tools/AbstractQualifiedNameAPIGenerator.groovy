package org.jprotocol.protocol.tools

import org.jprotocol.codegen.JavaGenerator
import org.jprotocol.codegen.NameFormatter
import org.jprotocol.framework.handler.IHandler
/**
 * Switch API Generator.
 * @param root root of a handler hierarchy
 * @param name of the java class to generate
 * @param pack package of the class to generate
 * @param dir the base directory of the class to generate
 */
class AbstractQualifiedNameAPIGenerator extends JavaGenerator {
	final names = []
	AbstractQualifiedNameAPIGenerator(IHandler root, String name, String pack, String dir) {
		super(pack, name)
		stdPackage()
		line 'import com.sjm.protocol.framework.handler.QualifiedName'
		stdJavaDoc()
		block("public class $name") {
			gen(root, null)
			names.each {
				if (it.length() > 0) {
					line(/public final static String ${NameFormatter.formatName(it)}_Simple = "${it}"/)
				}
			}
		}
		
		save(dir)
	}
	
	def gen(IHandler h, IHandler parent) {
		h.getSwitchValues().each {
			if (!names.contains(it)) {
				names.add(it)
			}
		}
		if (parent != null) {
			String sv = parent.switchValueStrOf(h)
			if (sv.length() > 0) {
				block("public static final class ${NameFormatter.formatName(sv)}") {
					line(/public static final QualifiedName NAME = new QualifiedName("${h.getQualifiedName()}")/)
					h.upperHandlers.each { uh ->
						gen(uh, h)
					}
				}
			} else {
				h.upperHandlers.each { uh ->
					gen(uh, h)
				}
			}
		} else {
			h.upperHandlers.each { uh ->
				gen(uh, h)
			}
		}
	}
}

