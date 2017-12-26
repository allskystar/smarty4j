package com.ruixus.util.json.ser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.objectweb.asm.MethodVisitor;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONSerializer;
import com.ruixus.util.json.JSONSerializer.Provider;

public class MapSerializer implements Serializer, Generic {
	public static void $serialize(Map<?, ?> o, SimpleCharBuffer cb, Provider provider,
			Class<?> generic) {
		Serializer serializer = provider.getSerializer(generic);
		cb.append('{');
		for (Map.Entry<?, ?> entry : o.entrySet()) {
			Object key = entry.getKey();
			if (key != null) {
				cb.appendString(key.toString());
				cb.append(':');
				serializer.serialize(cb, entry.getValue(), provider);
				cb.append(',');
			}
		}
		cb.setCharAt(cb.length() - 1, '}');
	}

	public static void $serialize(Map<?, ?> o, SimpleCharBuffer cb, Provider provider) {
		cb.append('{');
		for (Map.Entry<?, ?> entry : o.entrySet()) {
			Object key = entry.getKey();
			if (key != null) {
				cb.appendString(key.toString());
				cb.append(':');
				JSONSerializer.serializeValue(cb, entry.getValue(), provider);
				cb.append(',');
			}
		}
		cb.setCharAt(cb.length() - 1, '}');
	}

	@Override
	public void serialize(SimpleCharBuffer cb, Object o, Provider provider) {
		MapSerializer.$serialize((Map<?, ?>) o, cb, provider);
	}

	@Override
	public Type getGeneric(MethodVisitor mv, Type type) {
		if (type instanceof ParameterizedType) {
			return ((ParameterizedType) type).getActualTypeArguments()[1];
		}
		return null;
	}
}
