package com.ruixus.util.json.encoder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.objectweb.asm.MethodVisitor;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder;
import com.ruixus.util.json.JSONEncoder.Provider;

public class ListEncoder implements Encoder, Generic {
	public static void $stringify(List<?> o, SimpleCharBuffer cb, Provider provider, Class<?> generic) {
		Encoder encoder = provider.getEncoder(generic);
		cb.append('[');
		for (Object item : o) {
			encoder.stringify(cb, item, provider);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public static void $stringify(List<?> o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (Object item : o) {
			JSONEncoder.encodeValue(cb, item, provider);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		ListEncoder.$stringify((List<?>) o, cb, provider);
	}

	@Override
	public Type getGeneric(MethodVisitor mv, Type type) {
		if (type instanceof ParameterizedType) {
			return ((ParameterizedType) type).getActualTypeArguments()[0];
		}
		return null;
	}
}
