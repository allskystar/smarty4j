package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public class BooleanArrayEncoder implements Encoder {
	public static void $stringify(boolean[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (boolean item : o) {
			cb.append(item);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		BooleanArrayEncoder.$stringify((boolean[]) o, cb, provider);
	}
}
