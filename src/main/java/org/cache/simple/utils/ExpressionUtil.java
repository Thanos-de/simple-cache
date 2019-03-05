package org.cache.simple.utils;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.cache.simple.excetion.ParamException;

import com.alibaba.fastjson.JSON;

public class ExpressionUtil {
	public static String getKeyByExpressionAndParams(Object[] params, String expression) throws Exception {
		if (isBlank(expression)) {
			throw new ParamException("expression is black!");
		}
		StringBuilder sb = new StringBuilder();
		String[] exs = expression.split(",");

		for (String ex : exs) {
			String[] md = ex.split("\\.");
			if (!isInteger(md[0])) {
				throw new ParamException("your expression is error,that like \" expressoion = 1.name,2.id,1... \"!");
			}
			sb.append(getBymd(params[Integer.parseInt(md[0]) - 1], md)).append("-");
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	public static String getBymd(Object obj, String[] md) throws Exception {
		for (int i = 0; i < md.length; i++) {
			if (i == 0) {
				if (md.length == 1) {
					return JSON.toJSONString(obj);
				}
				continue;
			}
			obj = getByFiedName(obj, md[i]);
		}
		return JSON.toJSONString(obj);

	}

	public static Object getByFiedName(Object obj, String fieldName) throws Exception {
		Method md = obj.getClass().getMethod("get" + toUpperCaseFirstOne(fieldName), null);
		Object rt = md.invoke(obj, null);
		return rt;
	}

	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	// 首字母转大写
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

}
