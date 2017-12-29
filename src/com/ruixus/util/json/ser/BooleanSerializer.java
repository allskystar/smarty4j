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
		$serialize((Boolean) o, cb, provider);
	}

	@Override
	public Object createObject(Object parent) {
		return null;
	}

	@Override
	public Object deserialize(Object o, JsonReader reader, Provider provider) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
