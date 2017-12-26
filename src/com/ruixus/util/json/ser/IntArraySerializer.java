package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONSerializer.Provider;

public class IntArraySerializer implements Serializer {
	public static void $serialize(int[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (int item : o) {
			cb.append(item);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void serialize(SimpleCharBuffer cb, Object o, Provider provider) {
		IntArraySerializer.$serialize((int[]) o, cb, provider);
	}
}