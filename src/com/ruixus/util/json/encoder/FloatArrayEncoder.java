package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public class FloatArrayEncoder implements Encoder {
	public static void $stringify(float[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (float item : o) {
			cb.append(Float.toString(item));
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		FloatArrayEncoder.$stringify((float[]) o, cb, provider);
	}
}
