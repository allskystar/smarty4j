package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.Provider;

public class FloatArraySerializer implements Serializer {
	public static void $serialize(float[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (float item : o) {
			cb.append(Float.toString(item));
			cb.append(',');
		}
		cb.appendClose(']');
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		FloatArraySerializer.$serialize((float[]) o, cb, provider);
	}
}
