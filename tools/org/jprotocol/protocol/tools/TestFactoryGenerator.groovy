package org.jprotocol.protocol.tools;

public class TestFactoryGenerator {
	public static void main(String[] arg) {
		new FactoryGenerator(new TestFactory("GeneratedTestFactory"), "com.sjm.protocol.tools").generate().save("tools")
	}
}
