package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public class StringArrayEncoder implements Encoder {
	public static void $stringify(String[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (String item : o) {
			cb.appendString(item);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		StringArrayEncoder.$stringify((String[]) o, cb, provider);
	}
}
