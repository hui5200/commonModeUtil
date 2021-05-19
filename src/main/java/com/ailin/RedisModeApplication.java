package com.ailin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.ailin.mapper"})
public class RedisModeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisModeApplication.class);
    }
}
