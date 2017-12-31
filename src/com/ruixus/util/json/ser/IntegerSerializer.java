package com.ruixus.util.json.ser;

import java.io.IOException;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public class IntegerSerializer implements Serializer {

	public static final IntegerSerializer instance = new IntegerSerializer();
	
	private IntegerSerializer() {		
	}

	public static void $serialize(Integer o, SimpleCharBuffer cb, Provider provider) {
		cb.append(o.intValue());
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		$serialize((Integer) o, cb, provider);
	}

	@Override
	public Object createObject(Object parent) {
		return null;
	}

	@Override
	public Object deserialize(Object o, JsonReader reader, Provider provider) throws IOException {
		return Integer.valueOf(reader.readInteger());
	}
}
