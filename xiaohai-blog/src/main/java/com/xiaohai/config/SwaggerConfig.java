package com.xiaohai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xiaohai.controller"))
                .build();
    }

    private ApiInfo apiInfo() {

        Contact contact = new Contact("小海世界", "http://www.baidu.com",
                "1793886060@qq.com");
        return new ApiInfoBuilder()
                .title("小海博客系统")
                .description("小海博客系统接口文档")
                .contact(contact) // 联系方式
                .version("1.1.0") // 版本
                .build();
    }
}
