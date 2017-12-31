package com.ruixus.util.json.ser;

import java.lang.reflect.Array;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public class ArraySerializer implements Serializer {

	public static final ArraySerializer instance = new ArraySerializer();
	
	private ArraySerializer() {		
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
	}

	@Override
	public Object createObject(Object parent) throws Exception {
		return Array.newInstance((Class<?>) parent, 16);
	}

	@Override
	public Object deserialize(Object o, JsonReader reader, Provider provider) throws Exception {
		return null;
	}
}
