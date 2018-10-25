package com.everhomes.module;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RouterPath {

	/**
	 * 路由路径，已跟客户端约定应用首页使用"/index"，应用内容详情使用"/detail"。
	 */
	String path() default "/";

}
