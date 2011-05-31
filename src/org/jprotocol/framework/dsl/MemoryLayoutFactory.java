package org.jprotocol.framework.dsl;

import static org.jprotocol.util.Contract.notNull;
import static org.jprotocol.util.Contract.require;

import org.jprotocol.framework.dsl.IProtocolLayoutType.Direction;


/**
 * This class should be extended if the DSL is a layout instead of a request/response protocol
 * Here is an example that defines the layout of a marker: 
 * <pre><code>
 * public class HV_Therapy_II extends MemoryLayoutFactory  {
 *     public HV_Therapy_II() {
 *         super("HV Therapy II");
 * 
 *         layout(
 *             args(
 *                 arg(
 *                     "Marker ID", size(5), offset(19), values(
 *                         value("ID", 10)
 *                     )
 *                 ),
 *                 arg(
 *                     "Reason", size(3), offset(16), values(
 *                         value("TachA", 0),
 *                         value("TachB", 1),
 *                         value("Fib", 2),
 *                         value("DeviceBasedTesting", 3),
 *                         value("ShockOnTFibber", 4),
 *                         value("CmdShock", 5)
 *                     )
 *                 ),
 *                 arg(
 *                     "High Voltage Waveform Mode", size(1), offset(15), values(
 *                         valuePrefix(
 *                             "Auto", 0, args(
 *                                 arg("Ventricular CardioVersion Left", size(7), offset(8), 0.0, 0.78125, Unit.percent),
 *                                 arg("Ventricular Defibrillation Percentage", size(7), offset(1), 0.0, 0.78125, Unit.percent),
 *                                 arg(
 *                                     "HV Waveform Type", size(1), offset(0), values(
 *                                         value("Monophasic", 0),
 *                                         value("Biphasic", 1)
 *                                     )
 *                                 )
 *                             )
 *                         ),
 *                         valuePrefix(
 *                             "Fixed", 1, args(
 *                                 arg("Second Phase Shock Pulse Width", size(7), offset(8), 0.0, 0.12207, Unit.ms),
 *                                 arg("First Phase Shock Pulse Width", size(7), offset(1), 0.0, 0.12207, Unit.ms),
 *                                 arg(
 *                                     "HV Waveform Type", size(1), offset(0), values(
 *                                         value("Monophasic", 0),
 *                                         value("Biphasic", 1)
 *                                     )
 *                                 )
 *                             )
 *                         )
 *                    )
 *                 )
 *             )
 *         );
 *     }
 * }
 * <code></pre>
 * 
 * @author eliasa01
 *
 */
public class MemoryLayoutFactory extends AbstractMemoryLayoutFactory {

    private IProtocolLayoutType memLayoutType;
    private final Direction direction;
    private final int blockOffset;
	private final String protocolName;

    protected MemoryLayoutFactory(String name) {
        this(name, name, Direction.Layout);
    }
    protected MemoryLayoutFactory(String name, String protocolName, Direction direction) {
        this(name, protocolName, direction, 0);
    }
    protected MemoryLayoutFactory(String name, String protocolName, Direction direction, int blockOffset) {
        super(name, false);
        require(notNull(protocolName));
        require(notNull(direction));
        this.protocolName = protocolName;
        this.direction = direction;
        this.blockOffset = blockOffset;
    }

    protected void layout(IProtocolLayoutType targetType, int offset, IArgumentType...argTypes) {
        memLayoutType = new ProtocolLayoutType(getName(), protocolName, direction, targetType, offset, argTypes);
    }
    protected void layout(IArgumentType...argTypes) {
        memLayoutType = new ProtocolLayoutType(getName(), protocolName, direction, argTypes);
    }
    
    public IProtocolLayoutType getMemoryLayout() {
        require(notNull(memLayoutType));
        return memLayoutType;
    }
    
    public int getBlockOffset() {
        return blockOffset;
    }

    public String getProtocolName() {
    	return protocolName;
    }
    protected static int blockOffset(int x) {
        return x;
    }
    
}
