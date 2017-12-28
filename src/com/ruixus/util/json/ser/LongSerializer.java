package com.ruixus.util.json.ser;

import java.io.IOException;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public class LongSerializer implements Serializer {
	public static void $serialize(Long o, SimpleCharBuffer cb, Provider provider) {
		cb.append(o.longValue());
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		$serialize((Long) o, cb, provider);
	}

	@Override
	public Object deserialize(Class<?> cc, JsonReader reader, Provider provider) throws IOException {
		char[] buf = new char[64];
		for (int i = 0; i < 64;) {
			int ch = reader.read();
			if (ch != ',' && ch != ']' && ch != '}' && ch != -1) {
				buf[i++] = (char) ch;
			} else {
				reader.unread();
				return Long.valueOf(new String(buf, 0, i));
			}
		}
		return null;
	}
}
