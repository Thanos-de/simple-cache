package org.cache.simple.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhaode
 * @date 2018年9月29日
 * 
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable {
	//参数是否可以全为null
	public boolean canAllNull() default false;
	/*
	 * 支持（参数位置.属性 或多级属性 例如（1.user.name） 多个属性以' , '分隔）
	 * 例如 方法参数为（User user，String arg） expression可以为： 1.head.eye,1.name,2
	 */
	public String expression();
}
