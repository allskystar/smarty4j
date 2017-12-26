package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public class DoubleArrayEncoder implements Encoder {
	public static void $stringify(double[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (double item : o) {
			cb.append(Double.toString(item));
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		DoubleArrayEncoder.$stringify((double[]) o, cb, provider);
	}
}
