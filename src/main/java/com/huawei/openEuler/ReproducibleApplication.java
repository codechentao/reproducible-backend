package com.huawei.openEuler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 *
 * @author zhangshengjie
 * @since 2022-5-27
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
public class ReproducibleApplication {
    protected final static Logger logger = LoggerFactory.getLogger(ReproducibleApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ReproducibleApplication.class, args);
        logger.debug("reproducible project start");
    }
}
