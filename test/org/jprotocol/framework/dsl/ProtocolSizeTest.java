package org.jprotocol.framework.dsl;

import junit.framework.TestCase;

import org.jprotocol.framework.dsl.IProtocolLayoutType.Direction;
import org.jprotocol.quantity.Unit;


public class ProtocolSizeTest extends TestCase {
    public void testSize() {
        IProtocolMessage p = new ProtocolMessage(new _Directory_Internal().getMemoryLayout(), false);
        assertEquals(601, p.getProtocolType().getSizeInBytes());
    }
}
class _Directory_Internal extends MemoryLayoutFactory  {
    public _Directory_Internal() {
        this(Direction.Layout);
    }
    public _Directory_Internal(Direction direction) {
        super(" Directory Internal", " Directory Internal", direction, blockOffset(18));

        layout(
            args(
                arg(
                    "__Compression", size(8), offset(0), values(
                        value("NOT_COMPRESSED", 0),
                        value("TWO_BIT", 1),
                        value("RUN_LENGTH", 2),
                        value("H", 3)
                    )
                ),
                iArg(
                    " Directory Internal Entry", 60, args(
                        args(
                            arg(
                                "_DirTriggerType _ValidFlag", size(1), offset(15), values(
                                    value("OFF", 0),
                                    value("ON", 1)
                                )
                            ),
                            arg(
                                "_DirTriggerType _TachBCutoff", size(1), offset(14), values(
                                    value("OFF", 0),
                                    value("ON", 1)
                                )
                            ),
                            arg(
                                "_DirTriggerType _Trigger", size(6), offset(8), values(
                                    value("A", 0),
                                    value("B", 1),
                                    value("C", 2),
                                    value("D", 3),
                                    value("E", 4),
                                    value("F", 5),
                                    value("G", 6),
                                    value("H", 7),
                                    value("I", 8),
                                    value("J", 9),
                                    value("K", 10),
                                    value("L", 11),
                                    value("M", 12),
                                    value("N", 13),
                                    value("O", 14),
                                    value("Q", 15),
                                    value("R", 16),
                                    value("S", 17),
                                    value("T", 18),
                                    value("U", 19)
                                )
                            ),
                            arg("_DirAddress", size(24), offset(16), realOffset(0.0), resolution(1.0), Unit.noUnit),
                            arg("_DirTotalSize", size(24), offset(40), realOffset(0.0), resolution(1.0), Unit.noUnit),
                            arg("_DirNumBlocks", size(8), offset(64), realOffset(0.0), resolution(1.0), Unit.noUnit),
                            arg(
                                "_DirInfo1 _SamplingRate", size(2), offset(77), values(
                                    value("A", 1),
                                    value("B", 2),
                                    value("C", 3),
                                    value("D", 0)
                                )
                            ),
                            arg(
                                "_DirInfo1 _Chan1", size(4), offset(72), values(
                                    value("A", 0),
                                    value("B", 1),
                                    value("C", 2),
                                    value("D", 3),
                                    value("E", 4),
                                    value("F", 5),
                                    value("G", 6),
                                    value("H", 7),
                                    value("I", 8),
                                    value("J", 9),
                                    value("K", 10),
                                    value("L", 11),
                                    value("M", 12),
                                    value("N", 13),
                                    value("O", 14)
                                )
                            ),
                            arg(
                                "_DirInfo2 _Chan3", size(4), offset(84), values(
                                    value("A", 0),
                                    value("B", 1),
                                    value("C", 2),
                                    value("D", 3),
                                    value("E", 4),
                                    value("F", 5),
                                    value("G", 6),
                                    value("H", 7),
                                    value("I", 8),
                                    value("J", 9),
                                    value("K", 10),
                                    value("L", 11),
                                    value("M", 12),
                                    value("N", 13),
                                    value("O", 14)
                                )
                            ),
                            arg(
                                "_DirInfo2 _Chan2", size(4), offset(80), values(
                                    value("A", 0),
                                    value("B", 1),
                                    value("C", 2),
                                    value("D", 3),
                                    value("E", 4),
                                    value("F", 5),
                                    value("G", 6),
                                    value("H", 7),
                                    value("I", 8),
                                    value("J", 9),
                                    value("K", 10),
                                    value("L", 11),
                                    value("M", 12),
                                    value("N", 13),
                                    value("O", 14)
                                )
                            )
                        )
                    )
                )
            )
        );
    }
}
