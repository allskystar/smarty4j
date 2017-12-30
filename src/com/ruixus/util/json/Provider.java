package com.ruixus.util.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ruixus.util.json.ser.ArrayListSerializer;
import com.ruixus.util.json.ser.BooleanArraySerializer;
import com.ruixus.util.json.ser.BooleanSerializer;
import com.ruixus.util.json.ser.ByteArraySerializer;
import com.ruixus.util.json.ser.ByteSerializer;
import com.ruixus.util.json.ser.CharArraySerializer;
import com.ruixus.util.json.ser.CharacterSerializer;
import com.ruixus.util.json.ser.DoubleArraySerializer;
import com.ruixus.util.json.ser.DoubleSerializer;
import com.ruixus.util.json.ser.FloatArraySerializer;
import com.ruixus.util.json.ser.FloatSerializer;
import com.ruixus.util.json.ser.IntArraySerializer;
import com.ruixus.util.json.ser.IntegerSerializer;
import com.ruixus.util.json.ser.ListSerializer;
import com.ruixus.util.json.ser.LongArraySerializer;
import com.ruixus.util.json.ser.LongSerializer;
import com.ruixus.util.json.ser.MapSerializer;
import com.ruixus.util.json.ser.Serializer;
import com.ruixus.util.json.ser.SetSerializer;
import com.ruixus.util.json.ser.ShortArraySerializer;
import com.ruixus.util.json.ser.ShortSerializer;
import com.ruixus.util.json.ser.StringArraySerializer;
import com.ruixus.util.json.ser.StringSerializer;

public class Provider {
	public static final String NAME = Provider.class.getName().replace('.', '/');
	private static final Map<Class<?>, Serializer> defBeanMapper = new HashMap<Class<?>, Serializer>();
	private static final Class<?>[] defAssignables;

	static {
		defAssignables = new Class<?>[] { Map.class, List.class, Set.class };

		defBeanMapper.put(char.class, new CharacterSerializer());
		defBeanMapper.put(boolean.class, new BooleanSerializer());
		defBeanMapper.put(byte.class, new ByteSerializer());
		defBeanMapper.put(short.class, new ShortSerializer());
		defBeanMapper.put(int.class, new IntegerSerializer());
		defBeanMapper.put(long.class, new LongSerializer());
		defBeanMapper.put(float.class, new FloatSerializer());
		defBeanMapper.put(double.class, new DoubleSerializer());

		defBeanMapper.put(Character.class, new CharacterSerializer());
		defBeanMapper.put(Boolean.class, new BooleanSerializer());
		defBeanMapper.put(Byte.class, new ByteSerializer());
		defBeanMapper.put(Short.class, new ShortSerializer());
		defBeanMapper.put(Integer.class, new IntegerSerializer());
		defBeanMapper.put(Long.class, new LongSerializer());
		defBeanMapper.put(Float.class, new FloatSerializer());
		defBeanMapper.put(Double.class, new DoubleSerializer());

		defBeanMapper.put(String.class, new StringSerializer());

		defBeanMapper.put(Map.class, new MapSerializer());
		defBeanMapper.put(List.class, new ListSerializer());
		defBeanMapper.put(Set.class, new SetSerializer());

		defBeanMapper.put(ArrayList.class, new ArrayListSerializer());
		defBeanMapper.put(String[].class, new StringArraySerializer());
		defBeanMapper.put(char[].class, new CharArraySerializer());
		defBeanMapper.put(boolean[].class, new BooleanArraySerializer());
		defBeanMapper.put(byte[].class, new ByteArraySerializer());
		defBeanMapper.put(short[].class, new ShortArraySerializer());
		defBeanMapper.put(int[].class, new IntArraySerializer());
		defBeanMapper.put(long[].class, new LongArraySerializer());
		defBeanMapper.put(float[].class, new FloatArraySerializer());
		defBeanMapper.put(double[].class, new DoubleArraySerializer());
	}

	private Map<Class<?>, Serializer> beanMapper;
	private Class<?>[] assignables;
	private int assignableSize;

	public Provider() {
		this.beanMapper = new HashMap<Class<?>, Serializer>(defBeanMapper);
		assignableSize = defAssignables.length;
		this.assignables = new Class[assignableSize];
		System.arraycopy(defAssignables, 0, assignables, 0, assignableSize);
	}

	public void addBeanSerializer(Class<?> beanClass, Serializer beanSerializer) {
		synchronized (beanMapper) {
			beanMapper.put(beanClass, beanSerializer);
		}
	}

	public void addAssignableSerializer(Class<?> baseClass, Serializer beanSerializer) {
		synchronized (beanMapper) {
			Class<?>[] newAssignables = new Class<?>[assignableSize + 1];
			System.arraycopy(assignables, 0, newAssignables, 0, assignableSize);
			newAssignables[assignableSize++] = baseClass;
			assignables = newAssignables;
			beanMapper.put(baseClass, beanSerializer);
		}
	}

	public Serializer getSerializer(Class<?> cc) {
		return this.getSerializer(cc, true);
	}

	public Serializer getSerializer(Class<?> cc, boolean needBuild) {
		Serializer serializer = beanMapper.get(cc);
		if (serializer == null && cc != null) {
			for (Class<?> item : assignables) {
				if (item.isAssignableFrom(cc)) {
					serializer = beanMapper.get(item);
					synchronized (beanMapper) {
						beanMapper.put(cc, serializer);
					}
					break;
				}
			}
			if (serializer == null && needBuild) {
				synchronized (beanMapper) {
					serializer = beanMapper.get(cc);
					if (serializer == null) {
						serializer = JsonSerializer.createSerializer(cc, this);
						beanMapper.put(cc, serializer);
					}
				}
			}
		}
		return serializer;
	}
}
