package org.jprotocol.framework.dsl.argiters;

import static org.jprotocol.util.Contract.require;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jprotocol.framework.dsl.IArgumentType;
import org.jprotocol.framework.dsl.IProtocolMessage;
import org.jprotocol.framework.dsl.IndexArgumentType;


public class GetAllArgsIter extends AbstractArgIter {
    private final List<IArgumentType> _args = new ArrayList<IArgumentType>();
    
    public GetAllArgsIter(IProtocolMessage p) {
        super(p);
        iterate();
    }

    @Override
    protected void iterateIndexed(IArgumentType arg, int...indexes) {
        require(arg.isIndexedType());
        int[] newIndexes = Arrays.copyOf(indexes, indexes.length + 1);
        for (int i = 0; i < protocol.noOfEntriesOf(arg); i++) {
            newIndexes[indexes.length] = i;
            iterateIndexedChildren(arg.getChildren(), newIndexes);
        }
    }
    private void iterateIndexedChildren(IArgumentType[] subArgs, int...indexes) {
        for (IArgumentType subArg : subArgs) {
            if (subArg.isIndexedType()) {
                iterateIndexed(subArg, indexes);
            } else {
                store(IndexArgumentType.indexedArgTypeOf(protocol.getProtocolType(), subArg, indexes));
//                iter(protocol.argOf(subArg.getName(), indexes));
            }
        }
    }

    
    

    protected boolean iter(IArgumentType arg) {
        _args.add(arg);
        return true;
    }
    public IArgumentType[] getArgs() {
        return _args.toArray(new IArgumentType[_args.size()]);
    }
    
}
