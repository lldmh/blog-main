package com.mh.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author dmh
 * @Date 2023/7/22 16:05
 * @Version 1.0
 * @introduce  日志切面注解
 */

//元注解
@Retention(RetentionPolicy.RUNTIME)  //注解保存在什么阶段，（通过反射获取这的注解）
@Target(ElementType.METHOD)     //注解放在什么上
public @interface SystemLog {
    //接口名字
    String businessName();
}
