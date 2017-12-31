package com.ruixus.util.json.ser;

import java.io.IOException;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.json.JsonReader;
import com.ruixus.util.json.Provider;

public class DoubleSerializer implements Serializer {

	public static final DoubleSerializer instance = new DoubleSerializer();
	
	private DoubleSerializer() {		
	}

	public static void $serialize(Double o, SimpleCharBuffer cb, Provider provider) {
		cb.append(o.toString());
	}

	@Override
	public void serialize(Object o, SimpleCharBuffer cb, Provider provider) {
		$serialize((Double) o, cb, provider);
	}

	@Override
	public Object createObject(Object parent) {
		return null;
	}

	@Override
	public Object deserialize(Object o, JsonReader reader, Provider provider) throws IOException {
		return Double.valueOf(reader.readConst(true));
	}
}
