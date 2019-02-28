package org.cache.simple.utils;

import com.alibaba.fastjson.JSON;

public class ArgUtil {
	public static String[] argstoStrings(Object[] objs) {
		String[] strings = new String[objs.length];
		Object obj;
		for (int i = 0; i < objs.length; i++) {
//			strings[i] = ToString.toString(objs[i]);
			obj = objs[i];
			if (obj==null) {
				strings[i] = "null";
			}else {
				strings[i] = obj.getClass().getSimpleName()+"-"+JSON.toJSONString(objs[i]);
			}
			
		}
		return strings;
	}
}
