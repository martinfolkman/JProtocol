package org.jprotocol.protocol.tools;
import org.jprotocol.framework.dsl.MemoryLayoutFactory;
import org.jprotocol.quantity.Quantity;
import org.jprotocol.quantity.Unit;
/**
* This class is generated by FactoryGenerator.groovy
* @author eliasa01
*/
@SuppressWarnings("all")
public class GeneratedTestFactory extends MemoryLayoutFactory  {
    public GeneratedTestFactory() {
        super("GeneratedTestFactory");

        layout(
            args(
                arg(
                    "Switch", size(8), offset(0), values(
                        value("Off", 0),
                        valuePrefix(
                            "On", 1, args(
                                arg("Count", size(8), offset(8), 0.0, 1.0, Unit.noUnit),
                                argStr("Str", Quantity.quantity(16, Unit.bitSize), Quantity.quantity(40, Unit.bitSize))
                            )
                        )
                    )
                ),
                iArg(
                    "Indexed", 10,(
                        args(
                            arg("iArg0", size(8), offset(80), 0.0, 1.0, Unit.noUnit),
                            arg(
                                "iArg1", size(8), offset(88), values(
                                    value("Good", 0),
                                    value("Bad", 1)
                                )
                            ),
                            iArg(
                                "Indexed2", 2,(
                                    args(
                                        arg("iArg3", size(8), offset(96), 0.0, 1.0, Unit.noUnit)
                                    )
                                )
                            )
                        )
                    )
                ),
                arg("Real", size(32), offset(800), 0.0, 1.0, Unit.mV),
                vArg(
                    "vArg", values(
                        value("vValue1", 0),
                        value("vValue2", 1)
                    )
                )
            )
        );
    }
}