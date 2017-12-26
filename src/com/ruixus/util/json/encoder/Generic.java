package com.ruixus.util.json.encoder;

import java.lang.reflect.Type;

import org.objectweb.asm.MethodVisitor;

public interface Generic {
	public Type getGeneric(MethodVisitor mv, Type type);
}
