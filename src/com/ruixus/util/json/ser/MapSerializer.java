package com.ruixus.util.json.ser;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.objectweb.asm.MethodVisitor;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.JsonSerializer;
import com.ruixus.util.json.Provider;

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
				serializer.serialize(entry.getValue(), cb, provider);
				cb.append(',');
			}
		}
		cb.appendClose('}');
	}

	public static void $serialize(Map<?, ?> o, SimpleCharBuffer cb, Provider provider) {
		cb.append('{');
		for (Map.Entry<?, ?> entry : o.entrySet()) {
			Object key = entry.getKey();
			if (key != null) {
				cb.appendString(key.toString());
				cb.append(':');
				JsonSerializer.serializeValue(entry.getValue(), cb, provider);
				cb.append(',');
			}
		}
		cb.appendClose('}');
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		$serialize((Map<?, ?>) o, cb, provider);
	}

	@Override
	public Type getGeneric(MethodVisitor mv, Type type) {
		if (type instanceof ParameterizedType) {
			return ((ParameterizedType) type).getActualTypeArguments()[1];
		}
		return null;
	}

	@Override
	public Object deserialize(Class<?> cc, JsonReader reader, Provider provider) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
