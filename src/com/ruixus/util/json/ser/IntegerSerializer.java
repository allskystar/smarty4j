package com.ruixus.util.json.ser;

import java.io.IOException;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public class IntegerSerializer implements Serializer {
	public static void $serialize(Integer o, SimpleCharBuffer cb, Provider provider) {
		cb.append(o.intValue());
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		$serialize((Integer) o, cb, provider);
	}

	@Override
	public Object deserialize(Class<?> cc, JsonReader reader, Provider provider) throws IOException {
		return Integer.valueOf(reader.readNumber());
	}
}
