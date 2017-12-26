package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public class ByteArrayEncoder implements Encoder {
	public static void $stringify(byte[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (byte item : o) {
			cb.append(item);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		ByteArrayEncoder.$stringify((byte[]) o, cb, provider);
	}
}
