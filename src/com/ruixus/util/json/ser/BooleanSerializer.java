package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONSerializer.Provider;

public class BooleanSerializer implements Serializer {
	public static void $serialize(Boolean o, SimpleCharBuffer cb, Provider provider) {
		cb.append(o.booleanValue());
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		BooleanSerializer.$serialize((Boolean) o, cb, provider);
	}
}
