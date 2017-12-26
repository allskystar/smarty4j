package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public class StringEncoder implements Encoder {
	public static void $stringify(String o, SimpleCharBuffer cb, Provider provider) {
		cb.appendString(o);
	}

	@Override
	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		StringEncoder.$stringify((String) o, cb, provider);
	}
}
