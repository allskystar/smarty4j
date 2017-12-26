package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONSerializer.Provider;

public interface Serializer {
	public static final String NAME = Serializer.class.getName().replace('.', '/');

	public void serialize(Object o, SimpleCharBuffer cb, Provider provider);
}
