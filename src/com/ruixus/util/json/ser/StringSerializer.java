package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONSerializer.Provider;

public class StringSerializer implements Serializer {
	public static void $serialize(String o, SimpleCharBuffer cb, Provider provider) {
		cb.appendString(o);
	}

	@Override
	public void serialize(SimpleCharBuffer cb, Object o, Provider provider) {
		StringSerializer.$serialize((String) o, cb, provider);
	}
}