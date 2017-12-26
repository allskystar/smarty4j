package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.Provider;

public class ByteArraySerializer implements Serializer {
	public static void $serialize(byte[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (byte item : o) {
			cb.append(item);
			cb.append(',');
		}
		cb.appendClose(']');
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		ByteArraySerializer.$serialize((byte[]) o, cb, provider);
	}
}
