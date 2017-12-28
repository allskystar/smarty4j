package com.ruixus.util.json.ser;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.objectweb.asm.MethodVisitor;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.JsonSerializer;
import com.ruixus.util.json.Provider;

public class ArrayListSerializer implements Serializer, Generic {
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
		ArrayListSerializer.$serialize((ArrayList<?>) o, cb, provider);
	}

	@Override
	public Type getGeneric(MethodVisitor mv, Type type) {
		if (type instanceof ParameterizedType) {
			return ((ParameterizedType) type).getActualTypeArguments()[0];
		}
		return null;
	}

	@Override
	public Object deserialize(Class<?> cc, JsonReader reader, Provider provider) throws IOException {
		return null;
	}
}
