package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONSerializer.Provider;

public class CharArraySerializer implements Serializer {
	public static void $serialize(char[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (char item : o) {
			cb.appendString(item);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void serialize(SimpleCharBuffer cb, Object o, Provider provider) {
		CharArraySerializer.$serialize((char[]) o, cb, provider);
	}
}
