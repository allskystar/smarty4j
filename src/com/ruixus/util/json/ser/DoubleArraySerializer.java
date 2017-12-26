package com.ruixus.util.json.ser;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.Provider;

public class DoubleArraySerializer implements Serializer {
	public static void $serialize(double[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (double item : o) {
			cb.append(Double.toString(item));
			cb.append(',');
		}
		cb.appendClose(']');
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		DoubleArraySerializer.$serialize((double[]) o, cb, provider);
	}
}
