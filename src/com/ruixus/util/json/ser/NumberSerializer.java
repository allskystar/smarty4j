package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONSerializer.Provider;

public class NumberSerializer implements Serializer {
	public static void $serialize(Number o, SimpleCharBuffer cb, Provider provider) {
		cb.append(o.toString());
	}

	@Override
	public void serialize(SimpleCharBuffer cb, Object o, Provider provider) {
		NumberSerializer.$serialize((Number) o, cb, provider);
	}
}