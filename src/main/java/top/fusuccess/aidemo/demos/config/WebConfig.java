package top.fusuccess.aidemo.demos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许所有来源访问，可以根据需要修改 origin
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081")  // 允许你的前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允许的请求方式
                .allowedHeaders("*")  // 允许所有请求头
                .allowCredentials(true);
    }
}
