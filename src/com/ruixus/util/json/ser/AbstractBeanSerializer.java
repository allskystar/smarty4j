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
	public Object deserialize(Class<?> cc, JsonReader reader, Provider provider) throws Exception {
		Object o = cc.newInstance();
		if (reader.read() != '{') {
			//TODO 异常
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
				//TODO 异常
				throw new NullPointerException();
			}
			Object value = provider.getSerializer(type).deserialize(type, reader, provider);
			setValue(o, index, value);
			ch = reader.readIgnoreWhitespace();
			if (ch == '}') {
				return o;
			}
			if (ch != ',') {
				//TODO
				throw new NullPointerException();
			}
		}
	}
	
	public Object test() {
		int c = 5;
		switch (c) {
		case 0:
			return new Integer(10);
		case 1:
			return new Integer(8);
		default:
			return null;
		}
	}
}
