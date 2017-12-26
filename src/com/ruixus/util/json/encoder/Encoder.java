package com.ruixus.util.json.encoder;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JSONEncoder.Provider;

public interface Encoder {
	public static final String NAME = Encoder.class.getName().replace('.', '/');

	public void stringify(SimpleCharBuffer cb, Object o, Provider provider);
}
