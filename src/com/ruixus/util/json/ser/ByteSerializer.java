package com.ruixus.util.json.ser;

import java.io.IOException;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public class ByteSerializer implements Serializer {
	public static void $serialize(Byte o, SimpleCharBuffer cb, Provider provider) {
		cb.append(o.intValue());
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		$serialize((Byte) o, cb, provider);
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
				return Byte.valueOf(new String(buf, 0, i));
			}
		}
		return null;
	}
}
