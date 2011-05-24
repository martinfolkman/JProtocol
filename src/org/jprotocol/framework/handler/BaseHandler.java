package org.jprotocol.framework.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The purpose of this is to isolate and protect upper handlers
 * @author eliasa01
 *
 */
class BaseHandler {
    private final Map<Integer, IUpperHandler> upperHandlersMap = new HashMap<Integer, IUpperHandler>();
    private final Map<IUpperHandler, Integer> upperReverseHandlersMap = new HashMap<IUpperHandler, Integer>();
    private final List<IUpperHandler> upperHandlers = new ArrayList<IUpperHandler>();
    private volatile boolean upperHandlersInitialized;
    
    synchronized public final IUpperHandler upperHandlerOf(int value) {
        init();
        return upperHandlersMap.get(value);
    }
    synchronized final Integer switchValueOf(IUpperHandler uh) {
        init();
        return upperReverseHandlersMap.get(uh);
    }
    
    synchronized public final void addHandler(int upperHandlerValue, IUpperHandler handler) {
        upperHandlersMap.put(upperHandlerValue, handler);
        upperReverseHandlersMap.put(handler, upperHandlerValue);
        upperHandlers.add(handler);
    }
    
    synchronized public final List<IUpperHandler> getUpperHandlerList() {
        init();
        return Collections.unmodifiableList(upperHandlers);
    }

    private void init() {
        if (!upperHandlersInitialized) {
            upperHandlersInitialized = true;
            _init();
        }
    }
    
    /**
     * Override this if lazy init of upper handlers are needed
     */
    protected void _init() {
        
    }

}
