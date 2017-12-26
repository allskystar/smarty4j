package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public class CharArrayEncoder implements Encoder {
	public static void $stringify(char[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (char item : o) {
			cb.appendString(item);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		CharArrayEncoder.$stringify((char[]) o, cb, provider);
	}
}
