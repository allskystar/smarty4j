package com.ruixus.util.json.ser;

import java.io.IOException;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public class StringArraySerializer implements Serializer {
	public static void $serialize(String[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (String item : o) {
			cb.appendString(item);
			cb.append(',');
		}
		cb.appendClose(']');
	}

	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		$serialize((String[]) o, cb, provider);
	}

	@Override
	public Object createObject(Object parent, Class<?> cc) {
		return null;
	}

	@Override
	public Object deserialize(Object o, JsonReader reader, Provider provider) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
