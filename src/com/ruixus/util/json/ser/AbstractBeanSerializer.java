package com.ruixus.util.json.ser;

import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public abstract class AbstractBeanSerializer implements Serializer {
	public static final String NAME = AbstractBeanSerializer.class.getName().replace('.', '/');

	public abstract Class<?> getType(String name);

	public abstract void setValue(Object o, String name, Object value);

	@Override
	public Object deserialize(Class<?> cc, JsonReader reader, Provider provider) throws Exception {
		Object o = cc.newInstance();
		if (reader.read() != '{') {
			//TODO 异常
		}
		while (reader.read() != '}') {
			reader.unread();
			String name = reader.readString();
			Class<?> type = getType(name);
			if (reader.read() != ':') {
				//TODO 异常
			}
			Object value = provider.getSerializer(type).deserialize(type, reader, provider);
			setValue(o, name, value);
		}
		return o;
	}
}
