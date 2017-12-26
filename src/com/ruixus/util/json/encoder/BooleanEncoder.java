package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public class BooleanEncoder implements Encoder {
	public static void $stringify(Boolean o, SimpleCharBuffer cb, Provider provider) {
		cb.append(o.booleanValue());
	}

	@Override
	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		BooleanEncoder.$stringify((Boolean) o, cb, provider);
	}
}
