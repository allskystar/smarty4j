package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public class NumberEncoder implements Encoder {
	public static void $stringify(Number o, SimpleCharBuffer cb, Provider provider) {
		cb.append(o.toString());
	}

	@Override
	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		NumberEncoder.$stringify((Number) o, cb, provider);
	}
}
