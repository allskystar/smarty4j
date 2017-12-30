package com.ruixus.util.json.ser;

import java.util.ArrayList;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonSerializer;
import com.ruixus.util.json.Provider;

public class ArrayListSerializer extends ListSerializer implements Serializer, Generic {
	public static void $serialize(ArrayList<?> o, SimpleCharBuffer cb, Provider provider, Class<?> generic) {
		Serializer serializer = provider.getSerializer(generic);
		cb.append('[');
		for (int i = 0, len = o.size(); i < len; i++) {
			serializer.serialize(o.get(i), cb, provider);
			cb.append(',');
		}
		cb.appendClose(']');
	}

	public static void $serialize(ArrayList<?> o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (int i = 0, len = o.size(); i < len; i++) {
			JsonSerializer.serializeValue(o.get(i), cb, provider);
			cb.append(',');
		}
		cb.appendClose(']');
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		$serialize((ArrayList<?>) o, cb, provider);
	}
}
