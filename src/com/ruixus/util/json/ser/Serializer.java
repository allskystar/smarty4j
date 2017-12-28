package com.ruixus.util.json.ser;

import java.io.IOException;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public interface Serializer {
	public static final String NAME = Serializer.class.getName().replace('.', '/');

	public void serialize(Object o, SimpleCharBuffer cb, Provider provider);

	public Object deserialize(Class<?> cc, JsonReader reader, Provider provider) throws IOException;
}
