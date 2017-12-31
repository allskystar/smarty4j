package com.ruixus.util.json.ser;

import java.io.IOException;
import java.util.Arrays;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public class IntArraySerializer implements Serializer {

	public static final IntArraySerializer instance = new IntArraySerializer();
	
	private IntArraySerializer() {		
	}

	public static void $serialize(int[] o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (int item : o) {
			cb.append(item);
			cb.append(',');
		}
		cb.appendClose(']');
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		$serialize((int[]) o, cb, provider);
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
		int[] list = new int[16];
		int size = 0;
		if (reader.readIgnoreWhitespace() != ']') {
			reader.unread();
			while (true) {
				if (size == list.length) {
					list = Arrays.copyOf(list, size * 2);
				}
				list[size++] = reader.readInteger();
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
