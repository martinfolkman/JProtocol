package org.jprotocol.util;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class LazyContainer<T extends IName> {
	private List<T> list;
	private Map<String, T> map;
	
	abstract protected List<T> createList();
	protected synchronized List<T> list() {
		if (list == null) {
			list = unmodifiableList(createList());
		}
		return list;
	}
	protected synchronized T get(String key) {
		if (map == null) {
			map = new HashMap<String, T>();
			for (T t: list()) {
				map.put(t.getName(), t);
			}
			map = unmodifiableMap(map);
		}
		return map.get(key);
	}
	
}
