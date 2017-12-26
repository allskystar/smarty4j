package com.ruixus.util.json.ser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.objectweb.asm.MethodVisitor;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONSerializer;
import com.ruixus.util.json.JSONSerializer.Provider;

public class ListSerializer implements Serializer, Generic {
	public static void $serialize(List<?> o, SimpleCharBuffer cb, Provider provider, Class<?> generic) {
		Serializer serializer = provider.getSerializer(generic);
		cb.append('[');
		for (Object item : o) {
			serializer.serialize(cb, item, provider);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public static void $serialize(List<?> o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (Object item : o) {
			JSONSerializer.serializeValue(cb, item, provider);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	@Override
	public void serialize(SimpleCharBuffer cb, Object o, Provider provider) {
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
