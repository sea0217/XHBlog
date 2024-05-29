package com.xiaohai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.xiaohai.mapper")
@EnableTransactionManagement
public class BlogAdminApplication {
    public static void main(String[] args) {
        // 启动Spring Boot项目
        SpringApplication.run(BlogAdminApplication.class, args);
    }
}
