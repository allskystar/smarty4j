package com.ruixus.util.json.ser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.objectweb.asm.MethodVisitor;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONSerializer;
import com.ruixus.util.json.Provider;

public class ListSerializer implements Serializer, Generic {
	public static void $serialize(List<?> o, SimpleCharBuffer cb, Provider provider, Class<?> generic) {
		Serializer serializer = provider.getSerializer(generic);
		cb.append('[');
		for (Object item : o) {
			serializer.serialize(item, cb, provider);
			cb.append(',');
		}
		cb.appendClose(']');
	}

	public static void $serialize(List<?> o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (Object item : o) {
			JSONSerializer.serializeValue(item, cb, provider);
			cb.append(',');
		}
		cb.appendClose(']');
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		ListSerializer.$serialize((List<?>) o, cb, provider);
	}

	@Override
	public Type getGeneric(MethodVisitor mv, Type type) {
		if (type instanceof ParameterizedType) {
			return ((ParameterizedType) type).getActualTypeArguments()[0];
		}
		return null;
	}
}
