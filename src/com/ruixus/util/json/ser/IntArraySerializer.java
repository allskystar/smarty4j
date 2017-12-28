package com.ruixus.util.json.ser;

import java.io.IOException;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public class IntArraySerializer implements Serializer {
	public static void $serialize(int[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (int item : o) {
			cb.append(item);
			cb.append(',');
		}
		cb.appendClose(']');
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		IntArraySerializer.$serialize((int[]) o, cb, provider);
	}

	@Override
	public Object deserialize(Class<?> cc, JsonReader reader, Provider provider) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
