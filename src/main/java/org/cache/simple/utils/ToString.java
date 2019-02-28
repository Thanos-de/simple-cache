package org.cache.simple.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 一种公认提供toString的方法
 */
public class ToString {

	public static String toString(Object obj) {
		List<Object> visited = new ArrayList<Object>();
		if (obj == null)
			return "null";
		if (visited.contains(obj))
			return "...";
		visited.add(obj);
		Class<?> c = obj.getClass();
		if (c == String.class)
			return (String) obj;
		if (c.isArray()) {
			String r = c.getSimpleName() + "[";

			for (int i = 0; i < Array.getLength(obj); i++) {
				if (i > 0)
					r += ",";
				Object value = Array.get(obj, i);
				if (c.getComponentType().isPrimitive())
					r += value;
				else
					r += toString(value);
			}
			return r + "]";
		}

		String r = c.getSimpleName();

		do {
			
			Field[] fileds = c.getDeclaredFields();
			AccessibleObject.setAccessible(fileds, true);
			boolean flag = false;
			for (Field field : fileds) {
				if (!Modifier.isStatic(field.getModifiers())) {
					flag = true;
				}
			}
			if (flag) {
				r += "{";
			}

			for (Field field : fileds) {
				if (!Modifier.isStatic(field.getModifiers())) {
					if (!r.endsWith("{")) 
						r += ",";
					r += field.getName() + "=";

					try {
						Class<?> t = field.getType();
						Object val = field.get(obj);
						if (t.isPrimitive())
							r += val;
						else
							r += toString(val);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (flag) {
				r += "}";
			}
			
			c = c.getSuperclass();
		} while (c != null);

		return r;
	}

	public static void main(String[] args) {
		List<Integer> values = new ArrayList<Integer>();
		for (int i = 0; i < 6; i++) {
			values.add(i * i);
		}
		
		System.err.println(new ToString().toString(values));
	}
}
