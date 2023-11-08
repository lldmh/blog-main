package com.mh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author dmh
 * @Date 2023/7/24 15:25
 * @Version 1.0
 * @introduce 后端启动类
 */
@SpringBootApplication
@MapperScan("com.mh.mapper")
public class BlogAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogAdminApplication.class, args);
    }
}