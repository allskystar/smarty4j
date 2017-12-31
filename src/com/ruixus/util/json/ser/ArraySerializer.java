package com.ruixus.util.json.ser;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
		cb.append('[');
		int len = Array.getLength(o);
		for (int i = 0; i < len; i++) {
			serializer.serialize(Array.get(o, i), cb, provider);
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
		List<Object> list = new ArrayList<Object>();
		if (reader.readIgnoreWhitespace() != ']') {
			reader.unread();
			while (true) {
				list.add(serializer.deserialize(o, reader, provider));
				int ch = reader.readIgnoreWhitespace();
				if (ch == ']') {
					break;
				}
				if (ch != ',') {
					// TODO 出错
				}
			}
		}
		int len = list.size();
		Object ret = Array.newInstance(type, len);
		for (int i = 0; i < len; i++) {
			Array.set(ret, i, list.get(i));
		}
		return ret;
	}
}
