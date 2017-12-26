package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONSerializer.Provider;

public class LongArraySerializer implements Serializer {
	public static void $serialize(long[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (long item : o) {
			cb.append(item);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void serialize(SimpleCharBuffer cb, Object o, Provider provider) {
		LongArraySerializer.$serialize((long[]) o, cb, provider);
	}
}
