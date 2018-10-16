package com.everhomes.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 有这个注解的 controller 层方法可以避免 xss 过滤
 * </p>
 * <p>
 * 主要是用于 xss 过滤后拿到的参数不正确的情况, 标识了这个注解的 API 需要自己做 xss 的检查过滤
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface XssExclude {

}
