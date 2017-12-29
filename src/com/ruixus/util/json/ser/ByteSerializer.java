package com.ruixus.util.json.ser;

import java.io.IOException;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public class ByteSerializer implements Serializer {
	public static void $serialize(Byte o, SimpleCharBuffer cb, Provider provider) {
		cb.append(o.intValue());
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		$serialize((Byte) o, cb, provider);
	}

	@Override
	public Object createObject(Object parent, Class<?> cc) {
		return null;
	}

	@Override
	public Object deserialize(Object o, JsonReader reader, Provider provider) throws IOException {
		return Byte.valueOf(reader.readNumber());
	}
}
