package com.ruixus.util.json.encoder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.objectweb.asm.MethodVisitor;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder;
import com.ruixus.util.json.JSONEncoder.Provider;

public class ArrayListEncoder implements Encoder, Generic {
	public static void $stringify(ArrayList<?> o, SimpleCharBuffer cb, Provider provider, Class<?> generic) {
		Encoder encoder = provider.getEncoder(generic);
		cb.append('[');
		for (int i = 0, len = o.size(); i < len; i++) {
			encoder.stringify(cb, o.get(i), provider);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public static void $stringify(ArrayList<?> o, SimpleCharBuffer cb, Provider provider) {
		cb.append('[');
		for (int i = 0, len = o.size(); i < len; i++) {
			JSONEncoder.encodeValue(cb, o.get(i), provider);
			cb.append(',');
		}
		cb.setCharAt(cb.length() - 1, ']');
	}

	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		ArrayListEncoder.$stringify((ArrayList<?>) o, cb, provider);
	}

	@Override
	public Type getGeneric(MethodVisitor mv, Type type) {
		if (type instanceof ParameterizedType) {
			return ((ParameterizedType) type).getActualTypeArguments()[0];
		}
		return null;
	}
}
