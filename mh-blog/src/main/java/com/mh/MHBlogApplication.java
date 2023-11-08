package com.mh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author dmh
 * @Date 2023/7/15 20:10
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan("com.mh.mapper")   //需要去扫描mapper所在位置
@EnableScheduling   //开启定时任务功能
@EnableSwagger2     //开启swagger2接口文档
public class MHBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(MHBlogApplication.class, args);
    }
}
