package com.ruixus.util.json.encoder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.objectweb.asm.MethodVisitor;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder;
import com.ruixus.util.json.JSONEncoder.Provider;

public class MapEncoder implements Encoder, Generic {
	public static void $stringify(Map<?, ?> o, SimpleCharBuffer cb, Provider provider,
			Class<?> generic) {
		Encoder encoder = provider.getEncoder(generic);
		cb.append('{');
		for (Map.Entry<?, ?> entry : o.entrySet()) {
			Object key = entry.getKey();
			if (key != null) {
				cb.appendString(key.toString());
				cb.append(':');
				encoder.stringify(cb, entry.getValue(), provider);
				cb.append(',');
			}
		}
		cb.setCharAt(cb.length() - 1, '}');
	}

	public static void $stringify(Map<?, ?> o, SimpleCharBuffer cb, Provider provider) {
		cb.append('{');
		for (Map.Entry<?, ?> entry : o.entrySet()) {
			Object key = entry.getKey();
			if (key != null) {
				cb.appendString(key.toString());
				cb.append(':');
				JSONEncoder.encodeValue(cb, entry.getValue(), provider);
				cb.append(',');
			}
		}
		cb.setCharAt(cb.length() - 1, '}');
	}

	@Override
	public void stringify(SimpleCharBuffer cb, Object o, Provider provider) {
		MapEncoder.$stringify((Map<?, ?>) o, cb, provider);
	}

	@Override
	public Type getGeneric(MethodVisitor mv, Type type) {
		if (type instanceof ParameterizedType) {
			return ((ParameterizedType) type).getActualTypeArguments()[1];
		}
		return null;
	}
}
