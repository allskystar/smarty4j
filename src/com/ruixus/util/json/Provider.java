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

		defBeanMapper.put(char.class, CharacterSerializer.instance);
		defBeanMapper.put(boolean.class, BooleanSerializer.instance);
		defBeanMapper.put(byte.class, ByteSerializer.instance);
		defBeanMapper.put(short.class,  ShortSerializer.instance);
		defBeanMapper.put(int.class,  IntegerSerializer.instance);
		defBeanMapper.put(long.class,  LongSerializer.instance);
		defBeanMapper.put(float.class,  FloatSerializer.instance);
		defBeanMapper.put(double.class,  DoubleSerializer.instance);

		defBeanMapper.put(Character.class,  CharacterSerializer.instance);
		defBeanMapper.put(Boolean.class,  BooleanSerializer.instance);
		defBeanMapper.put(Byte.class,  ByteSerializer.instance);
		defBeanMapper.put(Short.class,  ShortSerializer.instance);
		defBeanMapper.put(Integer.class,  IntegerSerializer.instance);
		defBeanMapper.put(Long.class,  LongSerializer.instance);
		defBeanMapper.put(Float.class,  FloatSerializer.instance);
		defBeanMapper.put(Double.class,  DoubleSerializer.instance);

		defBeanMapper.put(String.class,  StringSerializer.instance);

		defBeanMapper.put(Map.class,  MapSerializer.instance);
		defBeanMapper.put(List.class,  ListSerializer.instance);
		defBeanMapper.put(Set.class,  SetSerializer.instance);

		defBeanMapper.put(ArrayList.class,  ArrayListSerializer.instance);
		defBeanMapper.put(String[].class,  StringArraySerializer.instance);
		defBeanMapper.put(char[].class,  CharArraySerializer.instance);
		defBeanMapper.put(boolean[].class,  BooleanArraySerializer.instance);
		defBeanMapper.put(byte[].class,  ByteArraySerializer.instance);
		defBeanMapper.put(short[].class,  ShortArraySerializer.instance);
		defBeanMapper.put(int[].class,  IntArraySerializer.instance);
		defBeanMapper.put(long[].class,  LongArraySerializer.instance);
		defBeanMapper.put(float[].class,  FloatArraySerializer.instance);
		defBeanMapper.put(double[].class,  DoubleArraySerializer.instance);
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
