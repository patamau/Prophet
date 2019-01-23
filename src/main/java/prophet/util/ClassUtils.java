package prophet.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ClassUtils {

	private static final Logger logger = Logger.getLogger(ClassUtils.class);
	
	/**
	 * Retrieve all the non static, non volatile, non transient fields of the given class
	 * @param c
	 * @return
	 */
	public static List<Field> getFields(final Class<?> c) {
		final List<Field> rf = new ArrayList<Field>();
		final Field[] fields = c.getDeclaredFields();
		int fieldsn = fields.length;
		for (int i = 0; i < fieldsn; ++i) {
			final Field f = fields[i];
			if (!Modifier.isStatic(f.getModifiers())
					&& !Modifier.isVolatile(f.getModifiers())
					&& !Modifier.isTransient(f.getModifiers())) {
				f.setAccessible(true);
				rf.add(f);
			}
		}
		return rf;
	}
	
	public static Field getField(final Class<?> c, final String name) {
		final Field[] fields = c.getDeclaredFields();
		int fieldsn = fields.length;
		for (int i = 0; i < fieldsn; ++i) {
			final Field f = fields[i];
			if(f.getName().equals(name)
					&& !Modifier.isStatic(f.getModifiers())
					&& !Modifier.isVolatile(f.getModifiers())
					&& !Modifier.isTransient(f.getModifiers())) {
				f.setAccessible(true);
				return f;
			}
		}
		return null;
	}
	
	public static String getSetterName(final String attribute) {
		return "set"+Character.toUpperCase(attribute.charAt(0))+attribute.substring(1);
	}
	
	public static Method getMethod(final Object object, final String methodName) {
		Method[] omethods = object.getClass().getMethods();
		for(Method m: omethods) {
			final String mname = m.getName();
			if(mname.equals(methodName)) {
				return m;
			}
		}
		return null;
	}
	
	public static Method getSetterMethod(final Object object, final String attribute) {
		return getMethod(object, getSetterName(attribute));
	}
	
	public static void setFieldValue(final Object object, final String fieldName, final String value) throws Exception {
		final Method amethod = ClassUtils.getSetterMethod(object, fieldName);
		if(null != amethod) {
			final Class<?>[] atypes = amethod.getParameterTypes();
			final Class<?> atype = atypes[0];
			try {
				if(atype.isAssignableFrom(String.class)) {
					amethod.invoke(object, value);
				} else if(atype.isAssignableFrom(double.class)) {
					amethod.invoke(object, Double.valueOf(value));
				} else { 
					throw new IllegalArgumentException("No such supported type "+atype);
				}
			} catch (Throwable e) {
				throw new Exception(e);
			} 
		} else {
			throw new IllegalAccessException("No such setter method for "+fieldName+" in "+object.getClass().getName());
		}
	}
	
	public static String getFieldValue(final Object object, final String fieldName) {
		final Field field = getField(object.getClass(), fieldName);
		final Class<?> ftype = field.getType();
		try {
			if(ftype.isAssignableFrom(String.class)) {
				return (String)field.get(object);
			} else if(ftype.isAssignableFrom(double.class)) {
				return Double.toString((double)field.get(object));
			} else { 
				throw new IllegalArgumentException("No such supported type "+ftype);
			}
		} catch (Exception e) {
			logger.error(e);
			return null;
		} 
	}
}
