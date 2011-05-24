package org.jprotocol.protocol.tools

import org.jprotocol.codegen.*
import com.sjm.protocol.framework.dsl.*
import org.jprotocol.codegen.NameFormatter

import org.jprotocol.framework.dsl.MemoryLayoutFactory
/**
 * @author eliasa01
 *
 */
public class FactoryGenerator extends JavaGenerator {
	final factory
	final enumClass
	FactoryGenerator(MemoryLayoutFactory factory, String pack, IEnumClass enumClass) {
		this(factory, NameFormatter.formatName(factory.name), pack, enumClass)
	}
	
	FactoryGenerator(MemoryLayoutFactory factory, String name, String pack, IEnumClass enumClass) {
		super(pack, name)
		this.factory = factory
		this.enumClass = enumClass
	}
	/**
	 * Override this to add a referred type
	 */
	def referredType() {
		
	}

	/**
	 * Override this to add hardcoded args
	 */
	def hardCodedArgs() {
		
	}

	
	def generate() {
        stdPackage()
		line "import com.sjm.protocol.framework.dsl.MemoryLayoutFactory"
		line "import com.sjm.protocol.tools.MemoryLayoutPanel"
		line "import com.sjm.tools.quantity.*"
		line "import com.sjm.protocol.framework.dsl.IProtocolType.Direction"
		line "import static ${enumClass.getPackageName()}.${enumClass.getClassName()}.*"
        stdJavaDoc()
        simpleline(/@SuppressWarnings("all")/)
        block("public class ${name} extends MemoryLayoutFactory ") {
			line(/public static final String NAME = "${factory.name}"/)
            block("public ${name}()") {
            	line(/this(NAME, Direction.Layout)/)
            }
            block("public ${name}(String protocolName, Direction dir)") {
                line(/super(NAME, protocolName, dir, blockOffset(${factory.blockOffset}))/)
                dslblock("layout") {
                	referredType()
        			args(factory.memoryLayout.arguments)
                }

            }
			block("public static void main(String[] args)") {
				line "create(new ${name}())"
			}
        }
        this
	}

	def args(args) {
		hardCodedArgs()
    	args.each { a ->
			if (a.isEnumType()) {
				enumArg(a)
			} else if (a.isReal()) {
				realArg(a)
			} else if (a.isStr()) {
				strArg(a)
			} else if (a.isIndexedType()) {
				indexedArg(a)
			}
		}	
	}
	
	def indexedArg(a) {
		dslblock("iArg") {
			dslblock(/"${a.name}", ${a.maxEntries},/) {
				dslblock("args") {
					args(a.children)
				}
			}
		}
	}
	
	def realArg(a) {
		dslline(/arg("${a.name}", size(${a.sizeInBits}), offset(${a.offset}), ${a.realOffset}, ${a.resolution}, Unit.${a.unit.name})/) {}
	}
	
	def strArg(a) {
		dslline(/argStr("${a.name}", Quantity.quantity(${a.offset}, Unit.bitSize), Quantity.quantity(${a.sizeInBits}, Unit.bitSize))/) {}
	}
	
	def enumArg(a) {
		if (a.isVirtual()) {
			dslblock("vArg") {
				dslblock(/"${a.name}", values/) {
					vals(a)
				}
			}
		} else {
			String key = enumClass.keyOf(a.getEnumeration())
			if (key == null) {
				dslblock("arg") {
					dslblock(/"${a.name}", size(${a.sizeInBits}), offset(${a.offset}), values/) {
						vals(a)
					}
				}
			} else {
				dslline(/arg("${a.name}", size(${a.sizeInBits}), offset(${a.offset}), ${key})/) {}
			}
		}
	}
	
	def vals(a) {
		a.values.each { v ->
			if (hasSubArgs(v)) {
				dslblock("valuePrefix") {
					dslblock(/"${v.name}", ${v.value}, args/) {
						args(v.argTypes)
					}
				}
			} else {
				dslline(/value("${v.name}", ${v.value})/) {}
			}
		}
	}
	
	def hasSubArgs(v) {
		v.argTypes.length > 0
	}
}
