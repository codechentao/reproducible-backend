package com.huawei.openEuler.aop;

import java.lang.annotation.*;

/**
 * 未捕获异常处理注解:
 * 写入error日志
 *
 * @author zhanglei
 * @since 2023-11-23
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UncaughtExceptionHandler {
}
