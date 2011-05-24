package org.jprotocol.codegen


import groovy.util.BuilderSupport

/**
 * A groovy builder that generates java source code
 * It supports the following nodes:
 * line, comment, block, javadoc
 */
class JavaBuilder extends BuilderSupport {
	def result = ''<<''
	private indent = ' ' * 4
	private indentCount = 0
	private dslBlockCount = 0
	final dslLevelCount = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
	private jdoc = false
	def createNode(name) {
		if (name == "javadoc") {
			result << indentString() << "/**\n"
			jdoc = true
		}
	    return name
	}

	def createNode(name, value) {
		if (jdoc) {
			result << indentString() << "* " << value.toString() << "\n"
		} else if (name == 'comment') {
			result << indentString() << "//" << value.toString() << "\n"
		} else if (name == 'mainflow' || name == 'flow' || name == 'commaflow'){
		    result << indentString() << value.toString() << "(\n"
		} else if (name == 'simpleline') {
		    result << indentString() << value.toString() << "\n"
		} else if (name == "dslblock") {
		    writeComma()
			result << "\n" << indentString() << value.toString() << "("
			dslLevelCount[dslBlockCount] = true
			dslBlockCount++
		} else if (name == "iargblock") {
//		    assert false : "YES!!!!!!!!"
		    writeComma()
			result << "\n" << indentString() << value.toString() << "("
			dslLevelCount[dslBlockCount] = true
			dslBlockCount++ 
		} else if (name == "dslline") {
		    writeComma()
			result << "\n" << indentString() << value.toString()
		} else {
			result << indentString() << value.toString() << (name == "block" ? " {" : ";") << "\n"
		}
		indentCount++;
	    return "$name.${value.toString()}"
	}

	def writeComma() {
	    if (dslBlockCount == 0) return
        if (dslLevelCount[dslBlockCount]) {
            result << ","
        }
	}
	
	def createNode(name, Map attributes) {
	    return name
	}
	def createNode(name, Map attributes, value) {
	    return name
	    
	}
	void setParent(parent, child) {
	}
	
	void nodeCompleted(parent, node) {
		if (node == "javadoc") {
			result << indentString() << "*/\n"
			jdoc = false
			return
		}
	    indentCount--
	    if (node.startsWith("block.")) result << indentString() << "}\n"
	    if (node.startsWith('mainflow.')) result << indentString() << ");\n"
	    if (node.startsWith('commaflow.')) result << indentString() << "),\n"
	    if (node.startsWith('flow.')) result << indentString() << ")\n"
	    if (node.startsWith('dslblock.')) {
	        dslLevelCount[dslBlockCount] = false
	        dslBlockCount--;
	        if (dslBlockCount == 0) {
	            result << "\n" << indentString() << ");\n"
	        } else {
	            result << "\n" << indentString() << ")"
	        }
	    }
	    if (node.startsWith('iargblock.')) {
	        dslLevelCount[dslBlockCount] = false
	        dslBlockCount--;
	        if (dslBlockCount == 0) {
	            assert false : "NOT GO HERE STUPID"
	            result << "\n" << indentString() << ");\n"
	        } else {
	            result << "\n" << indentString() << "))"
	        }
	    }
	    if (node.startsWith('dslline.')) {
			dslLevelCount[dslBlockCount] = true
	    }
	}
	
	private indentString() {
		return indent * indentCount	
	}
}