package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public class LongEncoder implements Encoder {
	public static void $stringify(Long o, SimpleCharBuffer cb, Provider provider) {
		cb.append(o.longValue());
	}

	@Override
	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		LongEncoder.$stringify((Long) o, cb, provider);
	}
}
