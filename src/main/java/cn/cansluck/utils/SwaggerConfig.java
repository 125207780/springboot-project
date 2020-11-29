package cn.cansluck.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 指定扫描包下面的注解
                .apis(RequestHandlerSelectors.basePackage("cn.cansluck"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("集成 Swagger 2 构建RESTful APIs")
                .description("集成 Swagger 2 构建RESTful APIs")
                .termsOfServiceUrl("http://blog.csdn.net/xgx120413")
                .contact(new Contact("cansluck", "cn.cansluck", "125207780@qq.com"))
                .version("1.0.0")
                .build();
    }
}
