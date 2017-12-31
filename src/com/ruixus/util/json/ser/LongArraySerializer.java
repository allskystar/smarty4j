package com.ruixus.util.json.ser;

import java.io.IOException;
import java.util.Arrays;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public class LongArraySerializer implements Serializer {

	public static final LongArraySerializer instance = new LongArraySerializer();
	
	private LongArraySerializer() {		
	}

	public static void $serialize(long[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (long item : o) {
			cb.append(item);
			cb.append(',');
		}
		cb.appendClose(']');
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		$serialize((long[]) o, cb, provider);
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
		long[] list = new long[16];
		int size = 0;
		if (reader.readIgnoreWhitespace() != ']') {
			reader.unread();
			while (true) {
				if (size == list.length) {
					list = Arrays.copyOf(list, size * 2);
				}
				list[size++] = reader.readLong();
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
