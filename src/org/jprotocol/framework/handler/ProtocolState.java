package org.jprotocol.framework.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jprotocol.framework.dsl.IArgumentType;
import org.jprotocol.framework.dsl.INameValuePair;
import org.jprotocol.framework.dsl.IProtocolLayoutType;

 
public class ProtocolState implements IProtocolState {
 
    private final Map<String, ArgMap> typeMap;
    public ProtocolState() {
        this(new HashMap<String, ArgMap>());
    }
    public ProtocolState(Map<String, ArgMap> vMap) {
        this.typeMap = vMap;
    }
    
    private ArgMap typeOf(IProtocolLayoutType p) {
        ArgMap va = typeMap.get(p.getName());
        if (va == null) {
            va = new ArgMap(); 
            typeMap.put(p.getName(), va);
        }
        return va;
    }

    @Override
    public synchronized INameValuePair getValue(IProtocolLayoutType type, IArgumentType arg) {
        return typeOf(type).getNameValuePair(arg);
    }

    @Override
    public synchronized void setValue(IProtocolLayoutType type, IArgumentType arg, String value) {
        typeOf(type).setValue(arg, value);
    }

    @Override
    public Map<String, INameValuePair> makeFlattenedSnapshot() {
        final Map<String, INameValuePair> snapshot = new HashMap<String, INameValuePair>();
        Iterator<String> iter = typeMap.keySet().iterator();
        while (iter.hasNext()) {
            String type = iter.next();
            ArgMap am = typeMap.get(type);
            final Iterator<String> iterArgMap = am.map.keySet().iterator();
            while (iterArgMap.hasNext()) {
                String argName = iterArgMap.next();
                snapshot.put(flattenNameOf(type, argName), am.map.get(argName));
            }
        }
        return snapshot;
    }
    
    
    @Override public String toString() {
        StringBuffer buf = new StringBuffer();
        Iterator<String> iter = typeMap.keySet().iterator();
        while (iter.hasNext()) {
             String type = iter.next();
             buf.append("Type: ");
             buf.append(type);
             buf.append("\n");
             ArgMap am = typeMap.get(type);
             buf.append(am.toString());
             buf.append('\n');
        }
        return buf.toString();
    }
    @Override
    public String flattenNameOf(IProtocolLayoutType type, IArgumentType arg) {
        return flattenNameOf(type.getName(), arg.getName());
    }
    public String flattenNameOf(String type, String arg) {
        return type + ":" + arg;
    }
    @Override
    public IProtocolState makeCopy() {
        return new ProtocolState(makeCopyOfTypeMap()); 
    }
    private Map<String, ArgMap> makeCopyOfTypeMap() {
        Map<String, ArgMap> copy = new HashMap<String, ArgMap>();
        Iterator<String> iter = typeMap.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            copy.put(key, makeCopyOfArgMap(typeMap.get(key)));
            
        }
        return copy;
    }
    private ArgMap makeCopyOfArgMap(ArgMap argMap) {
        return argMap.makeCopy();
    }

}

class ArgMap {
    final Map<String, INameValuePair> map;
    ArgMap() {
        this(new HashMap<String, INameValuePair>());
    }
    ArgMap(Map<String, INameValuePair> map) {
        this.map = map;
    }
    public ArgMap makeCopy() {
        return new ArgMap(makeCopyOfMap());
    }
    private Map<String, INameValuePair> makeCopyOfMap() {
        Map<String, INameValuePair> copy = new HashMap<String, INameValuePair>();
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            copy.put(key, map.get(key));
        }
        return copy;
    }
    public INameValuePair getNameValuePair(IArgumentType arg) {
        INameValuePair nvp = map.get(arg.getName());
        if (nvp == null) {
            nvp = defaultOf(arg);
            map.put(arg.getName(), nvp);
        }
        return nvp;
    }

    
    private INameValuePair defaultOf(IArgumentType arg) {
        return arg.getValues()[0];
    }
    public void setValue(IArgumentType arg, String value) {
        INameValuePair nvp = map.get(arg.getName());
        if (nvp != null) {
            map.remove(arg.getName());
        }
        map.put(arg.getName(), arg.nvpOf(arg.valueOf(value)));
    }
    
    @Override public String toString() {
        StringBuffer buf = new StringBuffer();
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
             String key = iter.next();
             INameValuePair nvp = map.get(key);
             buf.append(key);
             buf.append("=");
             buf.append(nvp.toString());
             buf.append('\n');
        }
        return buf.toString();
    }
    
}