package top.fusuccess.aidemo.demos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.fusuccess.aidemo.demos.filter.JwtInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许所有来源访问，可以根据需要修改 origin
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081")  // 允许你的前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允许的请求方式
                .allowedHeaders("*")  // 允许所有请求头
                .allowCredentials(true);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置拦截器，拦截所有的请求，除了登录接口外
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // 所有路径都拦截
                .excludePathPatterns("/auth/login"); // 登录接口不拦截
    }
}
