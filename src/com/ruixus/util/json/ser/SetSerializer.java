package com.ruixus.util.json.ser;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

import org.objectweb.asm.MethodVisitor;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.JsonSerializer;
import com.ruixus.util.json.Provider;

public class SetSerializer implements Serializer, Generic {
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
		SetSerializer.$serialize((Set<?>) o, cb, provider);
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
		// TODO Auto-generated method stub
		return null;
	}
}