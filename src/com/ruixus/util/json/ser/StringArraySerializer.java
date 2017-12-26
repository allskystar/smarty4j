package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONSerializer.Provider;

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
		StringArraySerializer.$serialize((String[]) o, cb, provider);
	}
}
