package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public class LongArrayEncoder implements Encoder {
	public static void $stringify(long[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (long item : o) {
			cb.append(item);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		LongArrayEncoder.$stringify((long[]) o, cb, provider);
	}
}
