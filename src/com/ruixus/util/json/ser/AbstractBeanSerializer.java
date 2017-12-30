package com.ruixus.util.json.ser;

import java.util.HashMap;
import java.util.Map;

import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public abstract class AbstractBeanSerializer implements Serializer {
	public static class BeanItem {
		private int index;
		private Serializer serializer;
		
		public BeanItem(int index, Serializer serializer) {
			this.index = index;
			this.serializer = serializer;
		}
	}

	public static final String NAME = AbstractBeanSerializer.class.getName().replace('.', '/');

	private Map<String, BeanItem> items = new HashMap<String, BeanItem>();

	public abstract void setValue(Object o, int index, Object value);

	public void setNameIndex(String name, BeanItem item) {
		items.put(name, item);
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
			BeanItem item = items.get(reader.readString());
			if (reader.readIgnoreWhitespace() != ':') {
				// TODO 异常
				throw new NullPointerException();
			}

			setValue(o, item.index, item.serializer.deserialize(item.serializer.createObject(o), reader, provider));
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
