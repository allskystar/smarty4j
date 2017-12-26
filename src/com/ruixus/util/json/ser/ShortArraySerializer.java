package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONSerializer.Provider;

public class ShortArraySerializer implements Serializer {
	public static void $serialize(short[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (short item : o) {
			cb.append(item);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		ShortArraySerializer.$serialize((short[]) o, cb, provider);
	}
}
