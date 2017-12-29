package com.ruixus.util.json.ser;

import java.util.HashMap;
import java.util.Map;

import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public abstract class AbstractBeanSerializer implements Serializer {
	public static final String NAME = AbstractBeanSerializer.class.getName().replace('.', '/');

	private Map<String, Integer> idxNames = new HashMap<String, Integer>();

	public abstract Class<?> getType(int index);

	public abstract void setValue(Object o, int index, Object value);

	public void setNameIndex(String name, int index) {
		idxNames.put(name, index);
	}

	@Override
	public Object deserialize(Object o, JsonReader reader, Provider provider) throws Exception {
		if (reader.read() != '{') {
			// TODO 异常
			throw new NullPointerException();
		}
		int ch = reader.read();
		if (ch == '}') {
			return o;
		}
		reader.unread();
		while (true) {
			int index = idxNames.get(reader.readString()).intValue();
			Class<?> type = getType(index);
			if (reader.readIgnoreWhitespace() != ':') {
				// TODO 异常
				throw new NullPointerException();
			}

			Serializer serializer = provider.getSerializer(type);
			setValue(o, index, serializer.deserialize(serializer.createObject(o), reader, provider));
			ch = reader.readIgnoreWhitespace();
			if (ch == '}') {
				return o;
			}
			if (ch != ',') {
				// TODO
				throw new NullPointerException();
			}
		}
	}
}
