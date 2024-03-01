package com.senhai.common;

import com.alibaba.druid.pool.DruidDataSource;
import com.senhai.common.datasources.DataSourceProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @description: Common服务启动类
 * @projectName:senhai-huang
 * @see:com.senhai.common
 * @author:HW
 * @createTime:2024/2/29 15:19
 * @version:1.0
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.senhai")
@MapperScan(value = "com.senhai.common")
@EnableConfigurationProperties(DataSourceProperties.class)
public class CommonApplication {


    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class);
        log.info("common服务已启动");
    }

}
