package com.parabank.automation.context;

import com.parabank.automation.enums.ContextKey;

import java.util.EnumMap;
import java.util.Map;

public class ScenarioContext {

	private final Map<ContextKey, Object> contextData = new EnumMap<>(ContextKey.class);

	public void set(ContextKey key, Object value) {
		contextData.put(key, value);
	}

	public Object get(ContextKey key) {
		return contextData.get(key);
	}

	public boolean contains(ContextKey key) {
		return contextData.containsKey(key);
	}

	public void clear() {
		contextData.clear();
	}
}