package com.ruixus.test.smarty4j;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import com.ruixus.smarty4j.Context;
import com.ruixus.smarty4j.Engine;
import com.ruixus.smarty4j.Template;
import com.ruixus.smarty4j.TemplateException;

public class BaseUnit {
	
	public Engine engine = new Engine();

	public Context getContext(String text) throws Exception {
		return getContext(text, null);
	}

	public Context getContext(String text, Map<String, Object> data) throws Exception {
		engine.setTemplatePath(System.getProperty("user.dir").replace('\\', '/')
		    + "/classes/" + BaseUnit.class.getPackage().getName().replace('.', '/') + "/");
		Context ctx = new Context();
		if (data != null) {
			ctx.putAll(data);
		}
		Template tpl = new Template(engine, text);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		tpl.merge(ctx, bos);
		ctx.set("", bos.toByteArray());
		return ctx;
	}

	public String getString(String text) throws Exception {
		return getString(text, null);
	}

	public String getString(String text, Map<String, Object> data) throws Exception {
		return new String((byte[]) getContext(text, data).get(""));
	}

	public byte[] getBytes(String text) throws Exception {
		return getBytes(text, null);
	}

	public byte[] getBytes(String text, Map<String, Object> data) throws Exception {
		return (byte[]) getContext(text, data).get("");
	}

	public String getException(String text) {
		return getException(text, null);
	}

	public String getException(String text, Map<String, Object> data) {
		try {
			return getString(text, data);
		} catch (TemplateException e) {
			return e.getParseMessages().get(0).getMessage();
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}
