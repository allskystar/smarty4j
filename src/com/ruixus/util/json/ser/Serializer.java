package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public interface Serializer {
	public static final String NAME = Serializer.class.getName().replace('.', '/');

	public void serialize(Object o, SimpleCharBuffer cb, Provider provider);

	public Object createObject(Object parent) throws Exception;

	public Object deserialize(Object o, JsonReader reader, Provider provider) throws Exception;
}
