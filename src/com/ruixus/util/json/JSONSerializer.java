package com.ruixus.util.json;

import static org.objectweb.asm.Opcodes.*;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.SimpleStack;
import com.ruixus.util.json.ser.ArrayListSerializer;
import com.ruixus.util.json.ser.BooleanArraySerializer;
import com.ruixus.util.json.ser.BooleanSerializer;
import com.ruixus.util.json.ser.ByteArraySerializer;
import com.ruixus.util.json.ser.CharArraySerializer;
import com.ruixus.util.json.ser.CharacterSerializer;
import com.ruixus.util.json.ser.DoubleArraySerializer;
import com.ruixus.util.json.ser.FloatArraySerializer;
import com.ruixus.util.json.ser.Generic;
import com.ruixus.util.json.ser.IntArraySerializer;
import com.ruixus.util.json.ser.IntegerSerializer;
import com.ruixus.util.json.ser.ListSerializer;
import com.ruixus.util.json.ser.LongArraySerializer;
import com.ruixus.util.json.ser.LongSerializer;
import com.ruixus.util.json.ser.MapSerializer;
import com.ruixus.util.json.ser.NumberSerializer;
import com.ruixus.util.json.ser.Serializer;
import com.ruixus.util.json.ser.ShortArraySerializer;
import com.ruixus.util.json.ser.StringArraySerializer;
import com.ruixus.util.json.ser.StringSerializer;

public class JSONSerializer {
	private static class ObjectMapper extends ClassLoader {
		private static final ObjectMapper loader = new ObjectMapper();

		private ObjectMapper() {
			super(ObjectMapper.class.getClassLoader());
		}

		static Class<?> defineClass(String name, byte[] code) {
			return loader.defineClass(name, code, 0, code.length);
		}
	}

	private static final String NAME = JSONSerializer.class.getName().replace('.', '/');

	public static class Provider {
		public static final String NAME = Provider.class.getName().replace('.', '/');
		private static final Map<Class<?>, Serializer> defBeanMapper = new HashMap<Class<?>, Serializer>();
		private static final Class<?>[] defAssignables;

		static {
			defAssignables = new Class<?>[] { Number.class, Map.class, List.class };
			defBeanMapper.put(String.class, new StringSerializer());
			defBeanMapper.put(Character.class, new CharacterSerializer());
			defBeanMapper.put(Boolean.class, new BooleanSerializer());
			defBeanMapper.put(Byte.class, new IntegerSerializer());
			defBeanMapper.put(Short.class, new IntegerSerializer());
			defBeanMapper.put(Integer.class, new IntegerSerializer());
			defBeanMapper.put(Long.class, new LongSerializer());

			defBeanMapper.put(Number.class, new NumberSerializer());
			defBeanMapper.put(Map.class, new MapSerializer());
			defBeanMapper.put(List.class, new ListSerializer());

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
			beanMapper.put(beanClass, beanSerializer);
		}

		public void addAssignableSerializer(Class<?> baseClass, Serializer beanSerializer) {
			Class<?>[] newAssignables = new Class<?>[assignableSize + 1];
			System.arraycopy(assignables, 0, newAssignables, 0, assignableSize);
			newAssignables[assignableSize++] = baseClass;
			assignables = newAssignables;
			beanMapper.put(baseClass, beanSerializer);
		}

		public Serializer getSerializer(Class<?> cc) {
			return this.getSerializer(cc, true);
		}

		public Serializer getSerializer(Class<?> cc, boolean needBuild) {
			Serializer serializer = beanMapper.get(cc);
			if (serializer == null) {
				for (Class<?> item : assignables) {
					if (item.isAssignableFrom(cc)) {
						serializer = beanMapper.get(item);
						synchronized (JSONSerializer.class) {
							beanMapper.put(cc, serializer);
						}
						break;
					}
				}
				if (serializer == null && needBuild) {
					serializer = createSerializer(cc, this);
					synchronized (JSONSerializer.class) {
						beanMapper.put(cc, serializer);
					}
				}
			}
			return serializer;
		}
	}

	private SimpleStack recycler = new SimpleStack();
	private Provider provider;

	public JSONSerializer() {
		this(new Provider());
	}

	public JSONSerializer(Provider provider) {
		this.provider = provider;
	}

	public String encode(Object o) {
		SimpleCharBuffer cb;
		synchronized (this) {
			if (recycler.size() > 0) {
				cb = (SimpleCharBuffer) recycler.pop();
			} else {
				cb = new SimpleCharBuffer(4000);
			}
		}
		encodeValue(cb, o, provider);
		String ret = cb.toString();
		synchronized (this) {
			cb.setLength(0);
			recycler.push(cb);
		}
		return ret;
	}

	private static final int OBJ = 0;
	private static final int SB = OBJ + 1;
	private static final int CACHE = SB + 1;
	private static final int VALUE = CACHE + 1;

	private static void callStaticEncode(MethodVisitor mv, Provider provider, Serializer serializer, Type type) {
		Class<?> generic = null;
		if (serializer instanceof Generic) {
			type = ((Generic) serializer).getGeneric(mv, type);
			if (type instanceof Class) {
				if (Modifier.isFinal(((Class<?>) type).getModifiers())) {
					generic = (Class<?>) type;
				}
			}
		}
		Class<?> clazz = serializer.getClass();
		String name = null;
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().charAt(0) == '$') {
				name = method.getParameterTypes()[0].getName().replace('.', '/');
				name = (name.charAt(0) == '[' ? name : "L" + name + ";");
				break;
			}
		}
		if (name == null) {
			throw new RuntimeException(
					"Please provide a static method 'serialize(E value, SimpleCharBuffer cb, Provider provider)' in Class "
							+ serializer.getClass().getName() + " to serialize ‘value’");
		}
		mv.visitVarInsn(ALOAD, SB);
		mv.visitVarInsn(ALOAD, CACHE);
		if (generic != null) {
			provider.getSerializer(generic);
			mv.visitLdcInsn(org.objectweb.asm.Type.getType(generic));
			mv.visitMethodInsn(INVOKESTATIC, clazz.getName().replace('.', '/'), "$serialize",
					"(" + name + "L" + SimpleCharBuffer.NAME + ";L" + Provider.NAME + ";Ljava/lang/Class;)V");
		} else {
			mv.visitMethodInsn(INVOKESTATIC, clazz.getName().replace('.', '/'), "$serialize",
					"(" + name + "L" + SimpleCharBuffer.NAME + ";L" + Provider.NAME + ";)V");
		}
	}

	public static Serializer createSerializer(Class<?> clazz, Provider provider) {
		boolean first = true;
		String className = clazz.getName().replace('.', '/');
		String mapperName = Serializer.class.getName() + "$" + clazz.getName().replace('.', '$');

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		MethodVisitor mv;
		cw.visit(V1_5, ACC_PUBLIC, mapperName.replace('.', '/'), null, "java/lang/Object",
				new String[] { Serializer.NAME });

		// 定义类的构造方法
		mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cw.visitMethod(ACC_PUBLIC + ACC_FINAL, "serialize",
				"(L" + SimpleCharBuffer.NAME + ";Ljava/lang/Object;L" + Provider.NAME + ";)V", null, null);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitTypeInsn(CHECKCAST, className);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitMethodInsn(INVOKESTATIC, mapperName.replace('.', '/'), "$serialize",
				"(L" + className + ";L" + SimpleCharBuffer.NAME + ";L" + Provider.NAME + ";)V");
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		Label ret = new Label();
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "$serialize",
				"(L" + className + ";L" + SimpleCharBuffer.NAME + ";L" + Provider.NAME + ";)V", null, null);

		mv.visitVarInsn(ALOAD, SB);
		mv.visitLdcInsn('{');
		mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(C)V");

		try {
			// 序列化JavaBean可读属性
			for (PropertyDescriptor prop : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
				Method accessor = prop.getReadMethod();
				if (accessor == null) {
					continue;
				}
				String name = prop.getName();
				// class属性不需要序列化
				if ("class".equals(name)) {
					continue;
				}

				mv.visitVarInsn(ALOAD, SB);
				mv.visitLdcInsn((first ? "" : ",") + "\"" + name + "\":");
				mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(Ljava/lang/String;)V");

				first = false;

				Class<?> type = accessor.getReturnType();
				if (type == int.class) {
					mv.visitVarInsn(ALOAD, SB);
					mv.visitVarInsn(ALOAD, OBJ);
					mv.visitMethodInsn(INVOKEVIRTUAL, className, accessor.getName(), "()I");
					mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(I)V");
				} else if (type == long.class) {
					mv.visitVarInsn(ALOAD, SB);
					mv.visitVarInsn(ALOAD, OBJ);
					mv.visitMethodInsn(INVOKEVIRTUAL, className, accessor.getName(), "()J");
					mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(J)V");
				} else if (type == short.class) {
					mv.visitVarInsn(ALOAD, SB);
					mv.visitVarInsn(ALOAD, OBJ);
					mv.visitMethodInsn(INVOKEVIRTUAL, className, accessor.getName(), "()S");
					mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(I)V");
				} else if (type == byte.class) {
					mv.visitVarInsn(ALOAD, SB);
					mv.visitVarInsn(ALOAD, OBJ);
					mv.visitMethodInsn(INVOKEVIRTUAL, className, accessor.getName(), "()B");
					mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(I)V");
				} else if (type == double.class) {
					mv.visitVarInsn(ALOAD, SB);
					mv.visitVarInsn(ALOAD, OBJ);
					mv.visitMethodInsn(INVOKEVIRTUAL, className, accessor.getName(), "()D");
					mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(D)V");
				} else if (type == float.class) {
					mv.visitVarInsn(ALOAD, SB);
					mv.visitVarInsn(ALOAD, OBJ);
					mv.visitMethodInsn(INVOKEVIRTUAL, className, accessor.getName(), "()F");
					mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(F)V");
				} else if (type == boolean.class) {
					mv.visitVarInsn(ALOAD, SB);
					mv.visitVarInsn(ALOAD, OBJ);
					mv.visitMethodInsn(INVOKEVIRTUAL, className, accessor.getName(), "()Z");
					mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(Z)V");
				} else if (type == char.class) {
					mv.visitVarInsn(ALOAD, SB);
					mv.visitVarInsn(ALOAD, OBJ);
					mv.visitMethodInsn(INVOKEVIRTUAL, className, accessor.getName(), "()C");
					mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "appendString", "(C)V");
				} else {
					String typeName = type.getName().replace('.', '/');

					Label nonull = new Label();
					Label end = new Label();

					mv.visitVarInsn(ALOAD, OBJ);
					mv.visitMethodInsn(INVOKEVIRTUAL, className, accessor.getName(),
							"()" + (typeName.charAt(0) == '[' ? typeName : "L" + typeName + ";"));
					mv.visitVarInsn(ASTORE, VALUE);
					mv.visitVarInsn(ALOAD, VALUE);
					mv.visitJumpInsn(IFNONNULL, nonull);

					mv.visitVarInsn(ALOAD, SB);
					mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "appendNull", "()V");
					mv.visitJumpInsn(GOTO, end);

					mv.visitLabel(nonull);

					boolean assign = false;

					Annotation[] annos = accessor.getDeclaredAnnotations();
					for (Annotation anno : annos) {
						Serializer serializer = provider.getSerializer(anno.getClass().getInterfaces()[0], false);
						if (serializer != null) {
							mv.visitVarInsn(ALOAD, VALUE);
							callStaticEncode(mv, provider, serializer, accessor.getGenericReturnType());
							assign = true;
							break;
						}
					}

					if (!assign) {
						Serializer serializer = provider.getSerializer(type, false);
						if (serializer != null) {
							mv.visitVarInsn(ALOAD, VALUE);
							callStaticEncode(mv, provider, serializer, accessor.getGenericReturnType());
						} else {
							if (type.isArray()) {
								clazz = type.getComponentType();
								boolean isFinal = Modifier.isFinal(clazz.getModifiers());

								Label condition = new Label();
								Label loop = new Label();

								serializer = provider.getSerializer(clazz);
								mv.visitVarInsn(ALOAD, SB);
								mv.visitLdcInsn('[');
								mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(C)V");

								mv.visitVarInsn(ALOAD, VALUE);
								mv.visitInsn(ARRAYLENGTH);
								mv.visitVarInsn(ISTORE, VALUE + 2);
								mv.visitInsn(ICONST_0);
								mv.visitVarInsn(ISTORE, VALUE + 1);
								mv.visitJumpInsn(GOTO, condition);

								mv.visitLabel(loop);
								if (isFinal) {
									mv.visitVarInsn(ALOAD, VALUE);
									mv.visitVarInsn(ILOAD, VALUE + 1);
									mv.visitInsn(AALOAD);
									callStaticEncode(mv, provider, serializer, accessor.getGenericReturnType());
								} else {
									mv.visitVarInsn(ALOAD, SB);
									mv.visitVarInsn(ALOAD, VALUE);
									mv.visitVarInsn(ILOAD, VALUE + 1);
									mv.visitInsn(AALOAD);
									mv.visitVarInsn(ALOAD, CACHE);
									mv.visitMethodInsn(INVOKESTATIC, NAME, "encodeObject", "(L" + SimpleCharBuffer.NAME
											+ ";Ljava/lang/Object;L" + Provider.NAME + ";)V");
								}
								mv.visitVarInsn(ALOAD, SB);
								mv.visitLdcInsn(',');
								mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(C)V");
								mv.visitIincInsn(VALUE + 1, 1);

								mv.visitLabel(condition);
								mv.visitVarInsn(ILOAD, VALUE + 1);
								mv.visitVarInsn(ILOAD, VALUE + 2);
								mv.visitJumpInsn(IF_ICMPLT, loop);

								mv.visitVarInsn(ALOAD, SB);
								mv.visitVarInsn(ALOAD, SB);
								mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "length", "()I");
								mv.visitInsn(ICONST_1);
								mv.visitInsn(ISUB);
								mv.visitLdcInsn(']');
								mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "setCharAt", "(IC)V");
							} else {
								serializer = provider.getSerializer(type);

								if (Modifier.isFinal(type.getModifiers())) {
									mv.visitVarInsn(ALOAD, VALUE);
									callStaticEncode(mv, provider, serializer, accessor.getGenericReturnType());
								} else {
									mv.visitVarInsn(ALOAD, SB);
									mv.visitVarInsn(ALOAD, VALUE);
									mv.visitVarInsn(ALOAD, CACHE);
									mv.visitMethodInsn(INVOKESTATIC, NAME, "encodeObject", "(L" + SimpleCharBuffer.NAME
											+ ";Ljava/lang/Object;L" + Provider.NAME + ";)V");
								}
							}
						}
					}

					mv.visitLabel(end);
				}
			}
		} catch (Exception e) {
		}

		mv.visitVarInsn(ALOAD, SB);
		mv.visitLdcInsn('}');
		mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(C)V");

		mv.visitLabel(ret);
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
		cw.visitEnd();

		byte[] code = cw.toByteArray();
		try {
			return (Serializer) ObjectMapper.defineClass(mapperName, code).newInstance();
		} catch (Exception e) {
			try {
				return (Serializer) Class.forName(mapperName).newInstance();
			} catch (Exception ex) {
				throw new NullPointerException();
			}
		}
	}

	public static void encodeObject(SimpleCharBuffer cb, Object o, Provider provider) {
		Class<?> clazz = o.getClass();
		if (clazz.isArray()) {
			Serializer serializer = provider.getSerializer(clazz, false);
			if (serializer != null) {
				serializer.serialize(cb, o, provider);
			} else {
				cb.append('[');
				int len = Array.getLength(o);
				for (int i = 0; i < len; i++) {
					encodeValue(cb, Array.get(o, i), provider);
					cb.append(',');
				}
				cb.setCharAt(cb.length() - 1, ']');
			}
		} else {
			provider.getSerializer(clazz).serialize(cb, o, provider);
		}
	}

	public static void encodeValue(SimpleCharBuffer cb, Object o, Provider provider) {
		if (o == null) {
			cb.appendNull();
		} else {
			encodeObject(cb, o, provider);
		}
	}
}
