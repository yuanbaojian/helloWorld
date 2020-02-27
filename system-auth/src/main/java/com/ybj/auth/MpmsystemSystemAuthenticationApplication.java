package com.ybj.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//所有的dao层 都要放到启动类上注解， 包括其他模块的
@MapperScan({"com.ybj.auth.dao"})
@EnableCaching
@ComponentScan(basePackages ={"com.ybj.auth","com.ybj.test","com.ybj.crawler"} )
@SpringBootApplication
@ServletComponentScan
@EnableSwagger2
public class MpmsystemSystemAuthenticationApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MpmsystemSystemAuthenticationApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MpmsystemSystemAuthenticationApplication.class);
    }

}
