package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public class CharacterEncoder implements Encoder {
	public static void $stringify(Character o, SimpleCharBuffer cb, Provider provider) {
		cb.appendString(o.charValue());
	}

	@Override
	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		CharacterEncoder.$stringify((Character) o, cb, provider);
	}
}
