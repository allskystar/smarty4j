package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public class ShortArrayEncoder implements Encoder {
	public static void $stringify(short[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (short item : o) {
			cb.append(item);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		ShortArrayEncoder.$stringify((short[]) o, cb, provider);
	}
}
