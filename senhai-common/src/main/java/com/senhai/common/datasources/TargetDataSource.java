package com.senhai.common.datasources;

import java.lang.annotation.*;

/**
 * @description:
 * @projectName:senhai-huang
 * @see:com.senhai.common.datasources
 * @author:HW
 * @createTime:2024/3/1 10:47
 * @version:1.0
 */
//注解作用范围
@Target({ElementType.METHOD,ElementType.TYPE})
//表示注解的生命周期
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {

    String value();
}
