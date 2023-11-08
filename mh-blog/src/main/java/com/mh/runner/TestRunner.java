package com.mh.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Author dmh
 * @Date 2023/7/23 14:45
 * @Version 1.0
 * @introduce    CommandLineRunner实现项目启动时预处理
 */
@Component
public class TestRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("程序初始化");
    }
}
