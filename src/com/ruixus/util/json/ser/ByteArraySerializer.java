package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONSerializer.Provider;

public class ByteArraySerializer implements Serializer {
	public static void $serialize(byte[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (byte item : o) {
			cb.append(item);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void serialize(SimpleCharBuffer cb, Object o, Provider provider) {
		ByteArraySerializer.$serialize((byte[]) o, cb, provider);
	}
}
