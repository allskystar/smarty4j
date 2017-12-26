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

import com.ruixus.util.MethodVisitorProxy;
import com.ruixus.util.SimpleCharBuffer;
import com.ruixus.util.SimpleStack;
import com.ruixus.util.json.encoder.ArrayListEncoder;
import com.ruixus.util.json.encoder.BooleanArrayEncoder;
import com.ruixus.util.json.encoder.BooleanEncoder;
import com.ruixus.util.json.encoder.ByteArrayEncoder;
import com.ruixus.util.json.encoder.CharArrayEncoder;
import com.ruixus.util.json.encoder.CharacterEncoder;
import com.ruixus.util.json.encoder.DoubleArrayEncoder;
import com.ruixus.util.json.encoder.Encoder;
import com.ruixus.util.json.encoder.FloatArrayEncoder;
import com.ruixus.util.json.encoder.Generic;
import com.ruixus.util.json.encoder.IntArrayEncoder;
import com.ruixus.util.json.encoder.IntegerEncoder;
import com.ruixus.util.json.encoder.ListEncoder;
import com.ruixus.util.json.encoder.LongArrayEncoder;
import com.ruixus.util.json.encoder.LongEncoder;
import com.ruixus.util.json.encoder.MapEncoder;
import com.ruixus.util.json.encoder.NumberEncoder;
import com.ruixus.util.json.encoder.ShortArrayEncoder;
import com.ruixus.util.json.encoder.StringArrayEncoder;
import com.ruixus.util.json.encoder.StringEncoder;

public class JSONEncoder {
	private static class ObjectMapper extends ClassLoader {
		private static final ObjectMapper loader = new ObjectMapper();

		private ObjectMapper() {
			super(ObjectMapper.class.getClassLoader());
		}

		static Class<?> defineClass(String name, byte[] code) {
			return loader.defineClass(name, code, 0, code.length);
		}
	}

	private static final String NAME = JSONEncoder.class.getName().replace('.', '/');

	public static class Provider {
		public static final String NAME = Provider.class.getName().replace('.', '/');
		private static final Map<Class<?>, Encoder> defBeanMapper = new HashMap<Class<?>, Encoder>();
		private static final Class<?>[] defAssignables;

		static {
			defAssignables = new Class<?>[] {Number.class, Map.class, List.class};
			defBeanMapper.put(String.class, new StringEncoder());
			defBeanMapper.put(Character.class, new CharacterEncoder());
			defBeanMapper.put(Boolean.class, new BooleanEncoder());
			defBeanMapper.put(Byte.class, new IntegerEncoder());
			defBeanMapper.put(Short.class, new IntegerEncoder());
			defBeanMapper.put(Integer.class, new IntegerEncoder());
			defBeanMapper.put(Long.class, new LongEncoder());

			defBeanMapper.put(Number.class, new NumberEncoder());
			defBeanMapper.put(Map.class, new MapEncoder());
			defBeanMapper.put(List.class, new ListEncoder());

			defBeanMapper.put(ArrayList.class, new ArrayListEncoder());
			defBeanMapper.put(String[].class, new StringArrayEncoder());
			defBeanMapper.put(char[].class, new CharArrayEncoder());
			defBeanMapper.put(boolean[].class, new BooleanArrayEncoder());
			defBeanMapper.put(byte[].class, new ByteArrayEncoder());
			defBeanMapper.put(short[].class, new ShortArrayEncoder());
			defBeanMapper.put(int[].class, new IntArrayEncoder());
			defBeanMapper.put(long[].class, new LongArrayEncoder());
			defBeanMapper.put(float[].class, new FloatArrayEncoder());
			defBeanMapper.put(double[].class, new DoubleArrayEncoder());
		}

		private Map<Class<?>, Encoder> beanMapper;
		private Class<?>[] assignables;
		private int assignableSize;
		
		public Provider() {
			this.beanMapper = new HashMap<Class<?>, Encoder>(defBeanMapper);
			assignableSize = defAssignables.length;
			this.assignables = new Class[assignableSize];
			System.arraycopy(defAssignables, 0, assignables, 0, assignableSize);
		}

		public void addBeanEncoder(Class<?> beanClass, Encoder beanEncoder) {
			beanMapper.put(beanClass, beanEncoder);
		}

		public void addAssignableEncoder(Class<?> baseClass, Encoder beanEncoder) {
			Class<?>[] newAssignables = new Class<?>[assignableSize + 1];
			System.arraycopy(assignables, 0, newAssignables, 0, assignableSize);
			newAssignables[assignableSize++] = baseClass;
			assignables = newAssignables;
			beanMapper.put(baseClass, beanEncoder);
		}
		
		public Encoder getEncoder(Class<?> cc) {
			return this.getEncoder(cc, true);
		}

		public Encoder getEncoder(Class<?> cc, boolean needBuild) {
			Encoder encoder = beanMapper.get(cc);
			if (encoder == null) {
				for (Class<?> item : assignables) {
					if (item.isAssignableFrom(cc)) {
						encoder = beanMapper.get(item);
						synchronized (JSONEncoder.class) {
							beanMapper.put(cc, encoder);
						}
						break;
					}
				}
				if (encoder == null && needBuild) {
					encoder = createEncoder(cc, this);
					synchronized (JSONEncoder.class) {
						beanMapper.put(cc, encoder);
					}
				}
			}
			return encoder;
		}
	}
	
	private SimpleStack recycler = new SimpleStack();
	private Provider provider = new Provider();

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

	private static void callStaticEncode(MethodVisitor mv, Provider provider, Encoder encoder, Type type) {
		Class<?> generic = null;
		if (encoder instanceof Generic) {
			type = ((Generic) encoder).getGeneric(mv, type);
			if (type instanceof Class) {
				if (Modifier.isFinal(((Class<?>) type).getModifiers())) {
					generic = (Class<?>) type;
				}
			}
		}
		Class<?> clazz = encoder.getClass();
		String name = null;
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().charAt(0) == '$') {
				name = method.getParameterTypes()[0].getName().replace('.', '/');
				name = (name.charAt(0) == '[' ? name : "L" + name + ";");
				break;
			}
		}
		mv.visitVarInsn(ALOAD, SB);
		mv.visitVarInsn(ALOAD, CACHE);
		if (generic != null) {
			provider.getEncoder(generic);
			mv.visitLdcInsn(generic);
			mv.visitMethodInsn(INVOKESTATIC, clazz.getName().replace('.', '/'), "$stringify",
					"(" + name + "L" + SimpleCharBuffer.NAME + ";L" + Provider.NAME + ";Ljava/lang/Class;)V");
		} else {
			mv.visitMethodInsn(INVOKESTATIC, clazz.getName().replace('.', '/'), "$stringify",
					"(" + name + "L" + SimpleCharBuffer.NAME + ";L" + Provider.NAME + ";)V");
		}
	}

	public static Encoder createEncoder(Class<?> clazz, Provider provider) {
		boolean first = true;
		String className = clazz.getName().replace('.', '/');
		String mapperName = Encoder.class.getName() + "$" + clazz.getName().replace('.', '$');

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		MethodVisitor mv;
		cw.visit(V1_5, ACC_PUBLIC, mapperName.replace('.', '/'), null, "java/lang/Object", new String[] { Encoder.NAME });

		// 定义类的构造方法
		mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cw.visitMethod(ACC_PUBLIC + ACC_FINAL, "stringify",
				"(L" + SimpleCharBuffer.NAME + ";Ljava/lang/Object;L" + Provider.NAME + ";)V", null, null);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitTypeInsn(CHECKCAST, className);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitMethodInsn(INVOKESTATIC, mapperName.replace('.', '/'), "$stringify",
				"(L" + className + ";L" + SimpleCharBuffer.NAME + ";L" + Provider.NAME + ";)V");
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		Label ret = new Label();
		mv = new MethodVisitorProxy(cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "$stringify",
				"(L" + className + ";L" + SimpleCharBuffer.NAME + ";L" + Provider.NAME + ";)V", null, null));

		mv.visitVarInsn(ALOAD, SB);
		mv.visitLdcInsn("{");
		mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(Ljava/lang/String;)V");

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
						Encoder encoder = provider.getEncoder(anno.getClass().getInterfaces()[0], false);
						if (encoder != null) {
							mv.visitVarInsn(ALOAD, VALUE);
							callStaticEncode(mv, provider, encoder, accessor.getGenericReturnType());
							assign = true;
							break;
						}
					}

					if (!assign) {
						Encoder encoder = provider.getEncoder(type, false);
						if (encoder != null) {
							mv.visitVarInsn(ALOAD, VALUE);
							callStaticEncode(mv, provider, encoder, accessor.getGenericReturnType());
						} else {
							if (type.isArray()) {
								clazz = type.getComponentType();
								boolean isFinal = Modifier.isFinal(clazz.getModifiers());

								Label condition = new Label();
								Label loop = new Label();

								encoder = provider.getEncoder(clazz);
								mv.visitVarInsn(ALOAD, SB);
								mv.visitLdcInsn('[');
								mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(C)V");

								mv.visitVarInsn(ALOAD, VALUE);
								mv.visitInsn(ARRAYLENGTH);
								mv.visitVarInsn(ISTORE, VALUE + 2);
								mv.visitLdcInsn(0);
								mv.visitVarInsn(ISTORE, VALUE + 1);
								mv.visitJumpInsn(GOTO, condition);

								mv.visitLabel(loop);
								if (isFinal) {
									mv.visitVarInsn(ALOAD, VALUE);
									mv.visitVarInsn(ILOAD, VALUE + 1);
									mv.visitInsn(AALOAD);
									callStaticEncode(mv, provider, encoder, accessor.getGenericReturnType());
								} else {
									mv.visitVarInsn(ALOAD, SB);
									mv.visitVarInsn(ALOAD, VALUE);
									mv.visitVarInsn(ILOAD, VALUE + 1);
									mv.visitInsn(AALOAD);
									mv.visitVarInsn(ALOAD, CACHE);
									mv.visitMethodInsn(INVOKESTATIC, NAME, "encodeObject",
											"(L" + SimpleCharBuffer.NAME + ";Ljava/lang/Object;L" + Provider.NAME + ";)V");
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
								mv.visitLdcInsn(1);
								mv.visitInsn(ISUB);
								mv.visitLdcInsn(']');
								mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "setCharAt", "(IC)V");
							} else {
								encoder = provider.getEncoder(type);

								if (Modifier.isFinal(type.getModifiers())) {
									mv.visitVarInsn(ALOAD, VALUE);
									callStaticEncode(mv, provider, encoder, accessor.getGenericReturnType());
								} else {
									mv.visitVarInsn(ALOAD, SB);
									mv.visitVarInsn(ALOAD, VALUE);
									mv.visitVarInsn(ALOAD, CACHE);
									mv.visitMethodInsn(INVOKESTATIC, NAME, "encodeObject",
											"(L" + SimpleCharBuffer.NAME + ";Ljava/lang/Object;L" + Provider.NAME + ";)V");
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
			return (Encoder) ObjectMapper.defineClass(mapperName, code).newInstance();
		} catch (Exception e) {
			try {
				return (Encoder) Class.forName(mapperName).newInstance();
			} catch (Exception ex) {
				throw new NullPointerException();
			}
		}
	}

	public static void encodeObject(SimpleCharBuffer cb, Object o, Provider provider) {
		Class<?> clazz = o.getClass();
		if (clazz.isArray()) {
			Encoder encoder = provider.getEncoder(clazz, false);
			if (encoder != null) {
				encoder.stringify(cb, o, provider);
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
			provider.getEncoder(clazz).stringify(cb, o, provider);
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
