package com.ruixus.util.json.ser;

import java.lang.reflect.Type;

import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public interface Generic {
	public Type getGeneric(Type type);

	public Object deserialize(Object o, JsonReader reader, Provider provider, Type generic) throws Exception;
}
