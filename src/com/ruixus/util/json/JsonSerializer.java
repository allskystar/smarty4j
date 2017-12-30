package com.ruixus.util.json;

import static org.objectweb.asm.Opcodes.*;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
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
import com.ruixus.util.json.JsonInclude.Include;
import com.ruixus.util.json.ser.AbstractBeanSerializer;
import com.ruixus.util.json.ser.Generic;
import com.ruixus.util.json.ser.Serializer;

public class JsonSerializer {
	private static class Recycler {
		private SimpleCharBuffer cb = new SimpleCharBuffer(4000);
		private JsonReader jr = new JsonReader();
	}

	private static final String NAME = JsonSerializer.class.getName().replace('.', '/');
	private static Method defineClass;
	private static final ThreadLocal<Recycler> threadLocal = new ThreadLocal<Recycler>();

	static {
		try {
			defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class,
					int.class);
			defineClass.setAccessible(true);
		} catch (Exception ex) {
		}
	}

	private Provider provider;

	public JsonSerializer() {
		this(new Provider());
	}

	public JsonSerializer(Provider provider) {
		this.provider = provider;
	}

	private Recycler getRecycler() {
		Recycler recycler = threadLocal.get();
		if (recycler == null) {
			recycler = new Recycler();
			threadLocal.set(recycler);
		}
		return recycler;
	}

	public String serialize(Object o) {
		SimpleCharBuffer cb = getRecycler().cb;
		serializeValue(o, cb, provider);
		String ret = cb.toString();
		cb.setLength(0);
		return ret;
	}

	public void serialize(Writer writer, Object o) throws IOException {
		SimpleCharBuffer cb = getRecycler().cb;
		cb.setWriter(writer);
		serializeValue(o, cb, provider);
		cb.flush();
	}

	public Object deserialize(Reader reader, Class<?> cc) throws Exception {
		Serializer serializer = provider.getSerializer(cc);
		JsonReader jsonReader = getRecycler().jr;
		jsonReader.bind(reader);
		int ch = jsonReader.read();
		if (ch == '[') {
			List<Object> list = new ArrayList<Object>();
			while (true) {
				list.add(serializer.deserialize(cc.newInstance(), jsonReader, provider));
				ch = jsonReader.read();
				if (ch == ']') {
					break;
				}
				if (ch != ',') {
					// TODO 出错
				}
			}
			return list;
		}
		jsonReader.unread();
		Object value = serializer.deserialize(cc.newInstance(), jsonReader, provider);
		return value;
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
				Class<?>[] params = method.getParameterTypes();
				if ((generic != null && params.length == 3) || (generic == null && params.length == 4)) {
					continue;
				}
				name = org.objectweb.asm.Type.getDescriptor(params[0]);
				if (name.length() == 1) {
					name = null;
				} else {
					break;
				}
			}
		}
		if (name == null) {
			throw new RuntimeException(
					"Please provide a static method '$serialize(E value, SimpleCharBuffer cb, Provider provider"
							+ (generic != null ? ", Class cc" : "") + ")' in Class " + serializer.getClass().getName()
							+ " to serialize ‘value’");
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
		String className = clazz.getName().replace('.', '/');
		String mapperName = className + "$" + clazz.getName().replace('.', '$');
		Include classJsonInclude;
		{
			JsonInclude anno = clazz.getAnnotation(JsonInclude.class);
			classJsonInclude = anno != null ? anno.value() : null;
		}
		Map<String, Object> names = new HashMap<String, Object>();

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		MethodVisitor mv;
		cw.visit(V1_5, ACC_PUBLIC, mapperName.replace('.', '/'), null, AbstractBeanSerializer.NAME, null);

		// 定义类的构造方法
		mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, AbstractBeanSerializer.NAME, "<init>", "()V");
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

		mv = cw.visitMethod(ACC_PUBLIC, "createObject", "(Ljava/lang/Object;)Ljava/lang/Object;", null, null);
		mv.visitTypeInsn(NEW, className);
		mv.visitInsn(DUP);
		Constructor<?> constructor = clazz.getConstructors()[0];
		if (constructor.getParameterTypes().length == 0) {
			mv.visitMethodInsn(INVOKESPECIAL, className, "<init>", "()V");
		} else {
			String name = constructor.getParameterTypes()[0].getName().replace('.', '/');
			mv.visitVarInsn(ALOAD, 1);
			mv.visitTypeInsn(CHECKCAST, name);
			mv.visitMethodInsn(INVOKESPECIAL, className, "<init>", "(L" + name + ";)V");
		}
		mv.visitInsn(ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "$serialize",
				"(L" + className + ";L" + SimpleCharBuffer.NAME + ";L" + Provider.NAME + ";)V", null, null);

		mv.visitVarInsn(ALOAD, CB);
		mv.visitLdcInsn('{');
		mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(C)V");

		try {
			// 序列化JavaBean可读属性
			loop: for (PropertyDescriptor prop : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
				String name = prop.getName();
				// class属性不需要序列化
				if ("class".equals(name)) {
					continue;
				}

				Method accessor = prop.getWriteMethod();
				if (accessor != null) {
					names.put(name, accessor);
				}

				accessor = prop.getReadMethod();
				if (accessor == null) {
					continue;
				}

				Annotation[] annos = accessor.getDeclaredAnnotations();
				Include jsonInclude = classJsonInclude;
				for (Annotation anno : annos) {
					Class<?> cc = anno.annotationType();
					if (cc == JsonIgnore.class) {
						continue loop;
					} else if (cc == JsonProperty.class) {
						name = ((JsonProperty) anno).value();
					} else if (cc == JsonInclude.class) {
						jsonInclude = ((JsonInclude) anno).value();
					}
				}

				Class<?> type = accessor.getReturnType();
				String typeName = org.objectweb.asm.Type.getDescriptor(type);
				if (typeName.length() == 1) {
					mv.visitVarInsn(ALOAD, CB);
					mv.visitLdcInsn("\"" + name + "\":");
					mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(Ljava/lang/String;)V");

					boolean assign = false;

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

					mv.visitVarInsn(ALOAD, CB);
					mv.visitLdcInsn(',');
					mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(C)V");
				} else {
					Label isnull = new Label();
					Label end = new Label();

					mv.visitVarInsn(ALOAD, OBJ);
					mv.visitMethodInsn(INVOKEVIRTUAL, className, accessor.getName(), "()" + typeName);
					mv.visitVarInsn(ASTORE, VALUE);
					mv.visitVarInsn(ALOAD, VALUE);
					mv.visitJumpInsn(IFNULL, isnull);

					if (jsonInclude == Include.NON_EMPTY) {
						mv.visitVarInsn(ALOAD, VALUE);
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;");
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "length", "()I");
						mv.visitJumpInsn(IFEQ, end);
					}

					boolean assign = false;

					for (Annotation anno : annos) {
						Serializer serializer = provider.getSerializer(anno.getClass().getInterfaces()[0], false);
						if (serializer != null) {
							mv.visitVarInsn(ALOAD, CB);
							mv.visitLdcInsn("\"" + name + "\":");
							mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(Ljava/lang/String;)V");

							mv.visitVarInsn(ALOAD, VALUE);
							callStaticEncode(mv, provider, serializer, accessor.getGenericReturnType());
							assign = true;
							break;
						}
					}

					if (!assign) {
						mv.visitVarInsn(ALOAD, CB);
						mv.visitLdcInsn("\"" + name + "\":");
						mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(Ljava/lang/String;)V");

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

					mv.visitVarInsn(ALOAD, CB);
					mv.visitLdcInsn(',');
					mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(C)V");

					if (jsonInclude != Include.NON_NULL && jsonInclude != Include.NON_EMPTY) {
						mv.visitJumpInsn(GOTO, end);
						mv.visitLabel(isnull);

						mv.visitVarInsn(ALOAD, CB);
						mv.visitLdcInsn("\"" + name + "\":");
						mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(Ljava/lang/String;)V");

						Serializer serializer = provider.getSerializer(null, false);
						if (serializer != null) {
							mv.visitInsn(ACONST_NULL);
							callStaticEncode(mv, provider, serializer, null);
						} else {
							mv.visitVarInsn(ALOAD, CB);
							mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "appendNull", "()V");
						}

						mv.visitVarInsn(ALOAD, CB);
						mv.visitLdcInsn(',');
						mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "append", "(C)V");
					} else {
						mv.visitLabel(isnull);
					}

					mv.visitLabel(end);
				}
			}
		} catch (IntrospectionException e) {
		}

		mv.visitVarInsn(ALOAD, CB);
		mv.visitLdcInsn('}');
		mv.visitMethodInsn(INVOKEVIRTUAL, SimpleCharBuffer.NAME, "appendClose", "(C)V");

		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		int size = names.size();
		MethodVisitor mvGet = cw.visitMethod(ACC_PUBLIC, "getType", "(I)Ljava/lang/Class;", null, null);
		Label defaultGet = new Label();
		Label[] switchGet = new Label[size];

		MethodVisitor mvSet = cw.visitMethod(ACC_PUBLIC, "setValue", "(Ljava/lang/Object;ILjava/lang/Object;)V", null,
				null);
		Label defaultSet = new Label();
		Label[] switchSet = new Label[size];

		if (size > 0) {
			for (int i = 0; i < size; i++) {
				switchGet[i] = new Label();
				switchSet[i] = new Label();
			}

			mvGet.visitVarInsn(ILOAD, 1);
			mvGet.visitTableSwitchInsn(0, size - 1, defaultGet, switchGet);
			mvSet.visitVarInsn(ILOAD, 2);
			mvSet.visitTableSwitchInsn(0, size - 1, defaultSet, switchSet);

			int i = 0;
			for (Map.Entry<String, Object> name : names.entrySet()) {
				Method accessor = (Method) name.getValue();
				name.setValue(i);

				Class<?> type = accessor.getParameterTypes()[0];
				String typeName = type.getName().replace('.', '/');

				mvGet.visitLabel(switchGet[i]);
				mvGet.visitLdcInsn(org.objectweb.asm.Type.getType(type));
				mvGet.visitInsn(ARETURN);

				mvSet.visitLabel(switchSet[i]);
				mvSet.visitVarInsn(ALOAD, 1);
				mvSet.visitTypeInsn(CHECKCAST, className);
				mvSet.visitVarInsn(ALOAD, 3);
				mvSet.visitTypeInsn(CHECKCAST, typeName);
				mvSet.visitMethodInsn(INVOKEVIRTUAL, className, accessor.getName(), "(L" + typeName + ";)V");
				mvSet.visitInsn(RETURN);

				i++;
			}
		}

		mvGet.visitLabel(defaultGet);
		mvGet.visitInsn(ACONST_NULL);
		mvGet.visitInsn(ARETURN);
		mvGet.visitMaxs(0, 0);
		mvGet.visitEnd();

		mvSet.visitLabel(defaultSet);
		mvSet.visitInsn(RETURN);
		mvSet.visitMaxs(0, 0);
		mvSet.visitEnd();

		cw.visitEnd();

		byte[] code = cw.toByteArray();
		try {
			AbstractBeanSerializer serializer = (AbstractBeanSerializer) ((Class<?>) defineClass
					.invoke(clazz.getClassLoader(), mapperName, code, 0, code.length)).newInstance();
			for (Map.Entry<String, Object> name : names.entrySet()) {
				serializer.setNameIndex(name.getKey(), (Integer) name.getValue());
			}
			return serializer;
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
