package com.ruixus.util.json;

import static org.objectweb.asm.Opcodes.*;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.ruixus.util.json.ser.SetSerializer;
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
			defAssignables = new Class<?>[] { Number.class, Map.class, List.class, Set.class };
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
			if (serializer == null) {
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
							serializer = createSerializer(cc, this);
							beanMapper.put(cc, serializer);
						}
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

	private SimpleCharBuffer getBuffer() {
		synchronized (recycler) {
			if (recycler.size() > 0) {
				return (SimpleCharBuffer) recycler.pop();
			} else {
				return new SimpleCharBuffer(4000);
			}
		}
	}

	private void freeBuffer(SimpleCharBuffer cb) {
		synchronized (recycler) {
			recycler.push(cb);
		}
	}

	public String serialize(Object o) {
		SimpleCharBuffer cb = getBuffer();
		serializeValue(o, cb, provider);
		String ret = cb.toString();
		cb.setLength(0);
		freeBuffer(cb);
		return ret;
	}

	public void serialize(Writer writer, Object o) throws IOException {
		SimpleCharBuffer cb = getBuffer();
		cb.setWriter(writer);
		serializeValue(o, cb, provider);
		cb.flush();
		freeBuffer(cb);
	}

	private static final int OBJ = 0;
	private static final int CB = OBJ + 1;
	private static final int PROVIDER = CB + 1;
	private static final int VALUE = PROVIDER + 1;

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
				name = org.objectweb.asm.Type.getDescriptor(method.getParameterTypes()[0]);
				if (name.length() == 1) {
					name = null;
				} else {
					break;
				}
			}
		}
		if (name == null) {
			throw new RuntimeException(
					"Please provide a static method '$serialize(E value, SimpleCharBuffer cb, Provider provider)' in Class "
							+ serializer.getClass().getName() + " to serialize ‘value’");
		}
		mv.visitVarInsn(ALOAD, CB);
		mv.visitVarInsn(ALOAD, PROVIDER);
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

		mv = cw.visitMethod(ACC_PUBLIC, "serialize",
				"(Ljava/lang/Object;L" + SimpleCharBuffer.NAME + ";L" + Provider.NAME + ";)V", null, null);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitTypeInsn(CHECKCAST, className);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitMethodInsn(INVOKESTATIC, mapperName.replace('.', '/'), "$serialize",
				"(L" + className + ";L" + SimpleCharBuffer.NAME + ";L" + Provider.NAME + ";)V");
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		Label ret = new Label();
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "$serialize",
				"(L" + className + ";L" + SimpleCharBuffer.NAME + ";L" + Provider.NAME + ";)V", null, null);

		mv.visitVarInsn(ALOAD, CB);
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

				mv.visitVarInsn(ALOAD, CB);
				mv.visitLdcInsn((first ? "" : ",") + "\"" + name + "\":");
				mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(Ljava/lang/String;)V");

				first = false;

				Class<?> type = accessor.getReturnType();
				String typeName = org.objectweb.asm.Type.getDescriptor(type);
				if (typeName.length() == 1) {
					boolean assign = false;
					Annotation[] annos = accessor.getDeclaredAnnotations();
					for (Annotation anno : annos) {
						Serializer serializer = provider.getSerializer(anno.getClass().getInterfaces()[0], false);
						if (serializer != null) {
							Class<?> cc = serializer.getClass();
							try {
								cc.getMethod("$serialize", type, SimpleCharBuffer.class, Provider.class);
							} catch (Exception ex) {
								throw new RuntimeException("Please provide a static method '$serialize("
										+ type.getName() + " value, SimpleCharBuffer cb, Provider provider)' in Class "
										+ cc.getName() + " to serialize ‘value’");
							}

							mv.visitVarInsn(ALOAD, OBJ);
							mv.visitMethodInsn(INVOKEVIRTUAL, className, accessor.getName(), "()" + typeName);
							mv.visitVarInsn(ALOAD, CB);
							mv.visitVarInsn(ALOAD, PROVIDER);
							mv.visitMethodInsn(INVOKESTATIC, cc.getName().replace('.', '/'), "$serialize",
									"(" + typeName + "L" + SimpleCharBuffer.NAME + ";L" + Provider.NAME + ";)V");
							assign = true;
							break;
						}
					}

					if (!assign) {
						mv.visitVarInsn(ALOAD, CB);
						mv.visitVarInsn(ALOAD, OBJ);
						mv.visitMethodInsn(INVOKEVIRTUAL, className, accessor.getName(), "()" + typeName);
						mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME,
								typeName.equals("C") ? "appendString" : "append",
								"(" + (typeName.equals("B") || typeName.equals("S") ? "I" : typeName) + ")V");
					}
				} else {
					Label nonull = new Label();
					Label end = new Label();

					mv.visitVarInsn(ALOAD, OBJ);
					mv.visitMethodInsn(INVOKEVIRTUAL, className, accessor.getName(), "()" + typeName);
					mv.visitVarInsn(ASTORE, VALUE);
					mv.visitVarInsn(ALOAD, VALUE);
					mv.visitJumpInsn(IFNONNULL, nonull);

					mv.visitVarInsn(ALOAD, CB);
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
								mv.visitVarInsn(ALOAD, CB);
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
									mv.visitVarInsn(ALOAD, VALUE);
									mv.visitVarInsn(ILOAD, VALUE + 1);
									mv.visitInsn(AALOAD);
									mv.visitVarInsn(ALOAD, CB);
									mv.visitVarInsn(ALOAD, PROVIDER);
									mv.visitMethodInsn(INVOKESTATIC, NAME, "serializeObject", "(Ljava/lang/Object;L"
											+ SimpleCharBuffer.NAME + ";L" + Provider.NAME + ";)V");
								}
								mv.visitVarInsn(ALOAD, CB);
								mv.visitLdcInsn(',');
								mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(C)V");
								mv.visitIincInsn(VALUE + 1, 1);

								mv.visitLabel(condition);
								mv.visitVarInsn(ILOAD, VALUE + 1);
								mv.visitVarInsn(ILOAD, VALUE + 2);
								mv.visitJumpInsn(IF_ICMPLT, loop);

								mv.visitVarInsn(ALOAD, CB);
								mv.visitLdcInsn(']');
								mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "appendClose", "(C)V");
							} else {
								serializer = provider.getSerializer(type);

								if (Modifier.isFinal(type.getModifiers())) {
									mv.visitVarInsn(ALOAD, VALUE);
									callStaticEncode(mv, provider, serializer, accessor.getGenericReturnType());
								} else {
									mv.visitVarInsn(ALOAD, VALUE);
									mv.visitVarInsn(ALOAD, CB);
									mv.visitVarInsn(ALOAD, PROVIDER);
									mv.visitMethodInsn(INVOKESTATIC, NAME, "serializeObject", "(Ljava/lang/Object;L"
											+ SimpleCharBuffer.NAME + ";L" + Provider.NAME + ";)V");
								}
							}
						}
					}

					mv.visitLabel(end);
				}
			}
		} catch (IntrospectionException e) {
		}

		mv.visitVarInsn(ALOAD, CB);
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
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new NullPointerException();
		}
	}

	public static void serializeObject(Object o, SimpleCharBuffer cb, Provider provider) {
		Class<?> clazz = o.getClass();
		if (clazz.isArray()) {
			Serializer serializer = provider.getSerializer(clazz, false);
			if (serializer != null) {
				serializer.serialize(o, cb, provider);
			} else {
				cb.append('[');
				int len = Array.getLength(o);
				for (int i = 0; i < len; i++) {
					serializeValue(Array.get(o, i), cb, provider);
					cb.append(',');
				}
				cb.appendClose(']');
			}
		} else {
			provider.getSerializer(clazz).serialize(o, cb, provider);
		}
	}

	public static void serializeValue(Object o, SimpleCharBuffer cb, Provider provider) {
		if (o == null) {
			cb.appendNull();
		} else {
			serializeObject(o, cb, provider);
		}
	}
}
