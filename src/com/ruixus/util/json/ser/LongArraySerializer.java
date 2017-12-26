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
		cb.appendClose(']');
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		LongArraySerializer.$serialize((long[]) o, cb, provider);
	}
}
