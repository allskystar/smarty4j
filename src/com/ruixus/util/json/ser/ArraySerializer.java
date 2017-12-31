package com.ruixus.util.json.ser;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public class ArraySerializer implements Serializer, Generic {

	private Class<?> type;
	private Serializer serializer;
	
	public ArraySerializer(Class<?> type, Serializer serializer) {
		this.type = type;
		this.serializer = serializer;
	}

	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		Object[] array = (Object[]) o;
		cb.append('[');
		int len = array.length;
		for (int i = 0; i < len; i++) {
			serializer.serialize(array[i], cb, provider);
			cb.append(',');
		}
		cb.appendClose(']');
	}

	@Override
	public Object createObject(Object parent) throws Exception {
		return parent;
	}

	@Override
	public Object deserialize(Object o, JsonReader reader, Provider provider) throws Exception {
		return deserialize(o, reader, provider, null);
	}

	@Override
	public Type getGeneric(Type type) {
		if (type instanceof GenericArrayType) {
			return ((GenericArrayType) type).getGenericComponentType();
		}
		return null;
	}

	@Override
	public Object deserialize(Object o, JsonReader reader, Provider provider, Type generic) throws Exception {
		if (reader.readIgnoreWhitespace() != '[') {
			// TODO json数据错误
			throw new NullPointerException();
		}
		Object[] list = (Object[]) Array.newInstance(type, 16);
		int size = 0;
		if (reader.readIgnoreWhitespace() != ']') {
			reader.unread();
			while (true) {
				if (size == list.length) {
					list = Arrays.copyOf(list, size * 2);
				}
				list[size++] = serializer.deserialize(o, reader, provider);
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
