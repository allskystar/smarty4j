package com.ruixus.util.json.ser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.JsonSerializer;
import com.ruixus.util.json.Provider;

public class SetSerializer extends ListSerializer implements Serializer, Generic {
	private static final ListSerializer listSerializer = new ListSerializer();
	private static final MapSerializer mapSerializer = new MapSerializer();
	
	public static void $serialize(Set<?> o, SimpleCharBuffer cb, Provider provider, Class<?> generic) {
		Serializer serializer = provider.getSerializer(generic);
		cb.append('[');
		for (Object item : o) {
			serializer.serialize(item, cb, provider);
			cb.append(',');
		}
		cb.appendClose(']');
	}

	public static void $serialize(Set<?> o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (Object item : o) {
			JsonSerializer.serializeValue(item, cb, provider);
			cb.append(',');
		}
		cb.appendClose(']');
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		$serialize((Set<?>) o, cb, provider);
	}

	@Override
	public Object deserialize(Object o, JsonReader reader, Provider provider, Type generic) throws Exception {
		if (reader.readIgnoreWhitespace() != '[') {
			// TODO 出错
			throw new NullPointerException();
		}
		Set<Object> set = new HashSet<Object>();
		if (reader.readIgnoreWhitespace() == ']') {
			return set;
		}
		reader.unread();
		while (true) {
			if (generic instanceof Class) {
				Serializer serializer = provider.getSerializer((Class<?>) generic);
				set.add(serializer.deserialize(serializer.createObject(o), reader, provider));
			} else if (generic instanceof ParameterizedType) {
				Serializer serializer = provider.getSerializer((Class<?>) ((ParameterizedType) generic).getRawType());
				if (serializer instanceof Generic) {
					set.add(((Generic) serializer).deserialize(serializer.createObject(o), reader, provider,
							((Generic) serializer).getGeneric(generic)));
				} else {
					set.add(serializer.deserialize(serializer.createObject(o), reader, provider));
				}
			} else {
				int ch = reader.readIgnoreWhitespace();
				reader.unread();
				if (ch == '{') {
					set.add(mapSerializer.deserialize(mapSerializer.createObject(o), reader, provider));
				} else if (ch == '[') {
					set.add(listSerializer.deserialize(listSerializer.createObject(o), reader, provider));
				} else {
					set.add(reader.readObject());
				}
			}
			int ch = reader.readIgnoreWhitespace();
			if (ch == ']') {
				break;
			}
			if (ch != ',') {
				// TODO 出错
				throw new NullPointerException();
			}
		}
		return set;
	}
}