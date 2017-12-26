package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public class IntArrayEncoder implements Encoder {
	public static void $stringify(int[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (int item : o) {
			cb.append(item);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		IntArrayEncoder.$stringify((int[]) o, cb, provider);
	}
}
