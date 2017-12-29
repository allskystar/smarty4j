package com.ruixus.util.json.ser;

import java.io.IOException;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public class FloatSerializer implements Serializer {
	public static void $serialize(Float o, SimpleCharBuffer cb, Provider provider) {
		cb.append(o.toString());
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		$serialize((Float) o, cb, provider);
	}

	@Override
	public Object createObject(Object parent) {
		return null;
	}

	@Override
	public Object deserialize(Object o, JsonReader reader, Provider provider) throws IOException {
		return Float.valueOf(reader.readNumber());
	}
}
