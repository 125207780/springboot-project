package cn.cansluck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注解说明：
 * @RestController
 *  构造型(stereotype)注解，它为阅读代码的人提供建议。对于Spring，该类扮演了一个特殊角色。
 *  在本案例中，我们的类是一个web@Controller，所以当处理进来的web请求时，Spring会询问它
 * @EnableAutoConfiguration
 *  注解告诉Spring Boot根据添加的jar依赖猜测你想如何配置Spring。
 *  由于spring-boot-starter-web添加了Tomcat和SpringMVC，所以：
 *      auto_configuration将假定你正在开发一个web应用并相应的对Spring进行配置
 * @RequestMapping
 *  注解提供路由信息。它告诉Spring任何来自"/"路径的HTTP请求都应该被映射到home()方法。
 *  @RequestMapping 注解告诉Spring以字符串形式渲染结果。并直接返回给调用者。
 */
@RestController
@EnableAutoConfiguration
public class Example {

    @RequestMapping("/")
    public String hello() {
        return "hello springboot";
    }

    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);
    }
}
