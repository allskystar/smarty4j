package com.ruixus.util.json.ser;

import java.io.IOException;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public class BooleanSerializer implements Serializer {
	public static void $serialize(Boolean o, SimpleCharBuffer cb, Provider provider) {
		cb.append(o.booleanValue());
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		BooleanSerializer.$serialize((Boolean) o, cb, provider);
	}

	@Override
	public Object deserialize(Class<?> cc, JsonReader reader, Provider provider) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
