package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONSerializer.Provider;

public class BooleanArraySerializer implements Serializer {
	public static void $serialize(boolean[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (boolean item : o) {
			cb.append(item);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void serialize(SimpleCharBuffer cb, Object o, Provider provider) {
		BooleanArraySerializer.$serialize((boolean[]) o, cb, provider);
	}
}
