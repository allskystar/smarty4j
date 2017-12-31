package com.ruixus.util.json.ser;

import java.io.IOException;
import java.util.Arrays;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public class StringArraySerializer implements Serializer {

	public static final StringArraySerializer instance = new StringArraySerializer();
	
	private StringArraySerializer() {		
	}

	public static void $serialize(String[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (String item : o) {
			cb.appendString(item);
			cb.append(',');
		}
		cb.appendClose(']');
	}

	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		$serialize((String[]) o, cb, provider);
	}

	@Override
	public Object createObject(Object parent) {
		return null;
	}

	@Override
	public Object deserialize(Object o, JsonReader reader, Provider provider) throws IOException {
		if (reader.readIgnoreWhitespace() != '[') {
			// TODO json数据错误
			throw new NullPointerException();
		}
		String[] list = new String[16];
		int size = 0;
		if (reader.readIgnoreWhitespace() != ']') {
			reader.unread();
			while (true) {
				if (size == list.length) {
					list = Arrays.copyOf(list, size * 2);
				}
				list[size++] = reader.readString();
				int ch = reader.readIgnoreWhitespace();
				if (ch == ']') {
					break;
				}
				if (ch != ',') {
					// TODO 出错
				}
			}
		}
		return Arrays.copyOf(list, size);
	}
}
