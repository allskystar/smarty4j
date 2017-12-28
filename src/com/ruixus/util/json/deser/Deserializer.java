package com.ruixus.util.json.deser;

public interface Deserializer {
	public Object create(String name);

	public Object deserialize(Object o, String name, Object value);
}
