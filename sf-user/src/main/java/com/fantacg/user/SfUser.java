package com.fantacg.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author SUMMER
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(value = "com.fantacg.user.mapper")
public class SfUser {

    public static void main(String[] args) {
        SpringApplication.run(SfUser.class, args);
    }

}
