package com.ruixus.smarty4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.ruixus.smarty4j.statement.Parameter;

/**
 * <p>
 * Global configuration information stored in template engine, which manages all the template
 * objects associated with the template file, and search function or variable modifier instance from
 * the specified package.
 * </p>
 * 
 * <p>
 * The configuration file(smarty.properties) stored in the root directory of classpath.
 * </p>
 * <b>smarty.properties:</b>
 * 
 * <pre>
 * debug=true - debug mode, check template file update
 * encoding=UTF-8 - encoding for template file
 * template.path=/ - template root path
 * left.delimiter={
 * right.delimiter=}
 * package.function=com.ruixus.smarty4j.statement.function - name of the function expansion pack, separated by semicolons
 * package.modifier=com.ruixus.smarty4j.statement.modifier - name of variable modifier expansion pack, separated by semicolons
 * </pre>
 * 
 * @see com.ruixus.smarty4j.Template
 * 
 * @version 1.1.0, 2015/05/16
 * @author Ouyang Xianwei
 * @since Smarty 1.1
 */
public class Engine {

	/** The internal name of the method's owner class */
	public static final String NAME = Engine.class.getName().replace('.', '/');

	private boolean debug = true;
	private boolean cache = true;
	private Charset cs;
	private String root;
	private String ldelim;
	private String rdelim;
	private String[] extFunc;
	private String[] extMod;
	private Map<String, String> configs;
	private Map<String, Class<?>> funcs = new HashMap<String, Class<?>>();
	private Map<String, Class<?>> mods = new HashMap<String, Class<?>>();
	private Map<String, Template> tpls = new HashMap<String, Template>();

	/**
	 * Constructs an engine.
	 */
	public Engine() {
		Properties prop = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader()
		    .getResourceAsStream("smarty.properties");
		if (in != null) {
			try {
				prop.load(in);
			} catch (IOException e) {
			}
		}

		if ("false".equals(prop.getProperty("debug"))) {
			debug = false;
		}
		if ("off".equals(prop.getProperty("cache"))) {
			cache = false;
		}
		setEncoding(prop.getProperty("encoding", "UTF-8"));
		setTemplatePath(prop.getProperty("template.path", System.getProperty("user.dir")));
		setLeftDelimiter(prop.getProperty("left.delimiter", "{"));
		setRightDelimiter(prop.getProperty("right.delimiter", "}"));

		StringBuilder sb = new StringBuilder(64);
		sb.append(Parameter.class.getPackage().getName());
		int len = sb.length();
		sb.append(".function");
		String value = prop.getProperty("package.function");
		if (value != null) {
			sb.append(';').append(value);
			extFunc = sb.toString().split("\\s*;\\s*");
		} else {
			extFunc = new String[] { sb.toString() };
		}
		sb.setLength(len);
		sb.append(".modifier");
		value = prop.getProperty("package.modifier");
		if (value != null) {
			sb.append(';').append(value);
			extMod = (sb.toString()).split("\\s*;\\s*");
		} else {
			extMod = new String[] { sb.toString() };
		}
	}

	/**
	 * Returns debug mode.
	 * 
	 * @return <tt>true</tt> if the engine is debug mode
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Sets debug mode to on/off. In debug mode, it checks for updates when you get a template file
	 * object, the template will generate error messages with debug information, changing engine
	 * operating mode, does not affect the already compiled template.
	 *
	 * @param isDebug
	 *          <tt>true</tt> set debug mode to enable, <tt>false</tt> set debug mode to disable
	 */
	public void setDebug(boolean isDebug) {
		this.debug = isDebug;
	}

	/**
	 * Returns cache mode.
	 * 
	 * @return <tt>true</tt> if the engine is cache mode
	 */
	public boolean isCached() {
		return cache;
	}
	
	/**
	 * Sets cache mode to on/off.
	 *
	 * @param isCached
	 *          <tt>true</tt> set cache mode to enable, <tt>false</tt> set cache mode to disable
	 */
	public void setCached(boolean isCached) {
		cache = isCached;
	}
	
	/**
	 * Returns the charset of the engine
	 * 
	 * @return the charset of the engine
	 */
	public Charset getCharset() {
		return cs;
	}

	/**
	 * Sets charset for encoding, not affect the already compiled template.
	 * 
	 * @param encoding
	 *          the name of charset
	 */
	public void setEncoding(String encoding) {
		this.cs = Charset.forName(encoding);
	}

	/**
	 * Returns the root location of the template file in engine in the process of generating the
	 * template, the template file name relative to the root directory location for analysis.
	 * 
	 * @return root path
	 */
	public String getTemplatePath() {
		return root;
	}

	/**
	 * Sets the root location of the template file in engine in the process of generating the
	 * template, the template file name relative to the root directory location for analysis, does not
	 * affect the already compiled template.
	 * 
	 * @param path
	 *          the string of root path
	 */
	public void setTemplatePath(String path) {
		if (path.endsWith("/")) {
			root = path;
		} else {
			root = path + "/";
		}
	}

	/**
	 * Returns the left delimiter of the engine.
	 * 
	 * @return the left delimiter of the engine
	 */
	public String getLeftDelimiter() {
		return ldelim;
	}

	/**
	 * Sets the left delimiter of the engine, not affect the already compiled template..
	 * 
	 * @param delim
	 *          the left delimiter of the engine
	 */
	public void setLeftDelimiter(String delim) {
		this.ldelim = delim;
	}

	/**
	 * Returns the right delimiter of the engine.
	 * 
	 * @return the right delimiter of the engine
	 */
	public String getRightDelimiter() {
		return rdelim;
	}

	/**
	 * Sets the right delimiter of the engine, not affect the already compiled template..
	 * 
	 * @param delim
	 *          the right delimiter of the engine
	 */
	public void setRightDelimiter(String delim) {
		this.rdelim = delim;
	}

	/**
	 * Returns a template object, if the template object does not exist in the engine, the
	 * corresponding initialization file, it would generate a template object. If the template file
	 * associated update occurred, and the engine is in debug mode, will regenerate the template
	 * object.
	 * 
	 * @param name
	 *          the name of template file (relative address)
	 * @return the template object
	 * @throws IOException
	 *           if template file cannot be read
	 * @throws TemplateException
	 *           if syntax error in template
	 */
	public Template getTemplate(String name) throws IOException, TemplateException {
		name = root + name;
		Template tpl = tpls.get(name);
		if (tpl != null && !(debug && tpl.isUpdated())) {
			return tpl;
		}

		tpl = new Template(this, new File(name));
		tpls.put(name, tpl);
		return tpl;
	}

	/**
	 * Creates a function or variable modifier node with the specified name, it will found class with
	 * the specified package's name, the package.function or package.modifier is defined in
	 * smarty.properties file.
	 * 
	 * @param name
	 *          the string of name
	 * @param isFunction
	 *          <tt>true</tt> if function is created, otherwise variable modifier is created
	 * @return {@code null} if function and variable modifier not found, otherwise the function or
	 *         variable modifier node
	 * @see template.smarty4j.statement.IFunction
	 */
	public Object createNode(String name, boolean isFunction) {
		Map<String, Class<?>> classes = isFunction ? funcs : mods;
		Class<?> c = classes.get(name);
		if (c == null) {
			check: while (true) {
				String[] packages = isFunction ? extFunc : extMod;
				for (String packageName : packages) {
					try {
						c = (Class<?>) Class.forName(packageName + ".$" + name);
						classes.put(name, c);
						break check;
					} catch (ClassNotFoundException e) {
					}
				}
				return null;
			}
		}
		try {
			return c.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(String.format(MessageFormat.CANNOT_BE_INSTANTIATED,
			    (isFunction ? "The function(" : "The modifier(") + name + ")"));
		}
	}

	/**
	 * Add the entry of the system configuration information.
	 * 
	 * @param key
	 *          the key with which the specified value is to be associated
	 * @param value
	 *          the value to be associated with the specified key
	 */
	public void addConfig(String key, String value) {
		if (configs == null) {
			configs = new HashMap<String, String>();
		}
		configs.put(key, value);
	}

	/**
	 * Returns the map of system configuration information.
	 * 
	 * @return the map of system configuration information
	 */
	Map<String, String> getConfigures() {
		return configs;
	}
}