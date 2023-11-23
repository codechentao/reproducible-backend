package com.huawei.openEuler.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 未捕获异常aop处理类
 *
 * @author zhanglei
 * @since 2023-11-23
 */

@Aspect
@Component
@Slf4j
public class UncaughtExceptionInterceptor {
    public static final Logger logger = LoggerFactory.getLogger(UncaughtExceptionInterceptor.class);

    @Pointcut("@within(com.huawei.openEuler.aop.UncaughtExceptionHandler)")
    public void pointcut() {
    }

    @AfterThrowing(throwing = "e", pointcut = "pointcut()")
    public void logUncaughtException(JoinPoint joinPoint, Throwable e) {
        logger.error("Uncaught exception message: {}", e.getMessage(), e);
    }
}
