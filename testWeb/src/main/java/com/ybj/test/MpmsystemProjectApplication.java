package com.ybj.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * 项目管理模块启动类
 * @author kdl
 */
@ServletComponentScan
@SpringBootApplication
public class MpmsystemProjectApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MpmsystemProjectApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MpmsystemProjectApplication.class);
    }

}
