package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public class IntegerEncoder implements Encoder {
	public static void $stringify(Number o, SimpleCharBuffer cb, Provider provider) {
		cb.append(o.intValue());
	}

	@Override
	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		IntegerEncoder.$stringify((Number) o, cb, provider);
	}
}
