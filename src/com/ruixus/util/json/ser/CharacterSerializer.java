package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONSerializer.Provider;

public class CharacterSerializer implements Serializer {
	public static void $serialize(Character o, SimpleCharBuffer cb, Provider provider) {
		cb.appendString(o.charValue());
	}

	@Override
	public void serialize(SimpleCharBuffer cb, Object o, Provider provider) {
		CharacterSerializer.$serialize((Character) o, cb, provider);
	}
}
