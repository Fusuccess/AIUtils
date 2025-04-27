package com.fusuccess.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.fusuccess.aidemo.AiDemoApplication;
import top.fusuccess.aidemo.demos.utils.JwtUtils;

import java.util.Date;

@SpringBootTest(classes = AiDemoApplication.class)
public class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils; // 直接注入你写的JwtUtils


    /**
     * 测试解析Token
     */
    @Test
    public Claims testParseToken() {
        // 准备一个合法的Token（可以是你手动从登录接口拿到的token）
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc0NTc2NTczNSwiZXhwIjoxNzQ1NzY5MzM1fQ.0Izab5grAQNmUEtc-eynLjzQQjAfD5XT7Nmu_SDmhr8CZhw9-U0gZDs5esf5bUMuiNXYPyDwToyiP0zsaGF7Mw";

        // 调用你要测试的方法
        Claims claims = jwtUtils.parseToken(token);

        // 打印一下看结果
        System.out.println("用户名（subject）：" + claims.getSubject());
        System.out.println("过期时间（expiration）：" + new Date(claims.getExpiration().getTime()));
        return claims;
    }

    /**
     * 测试生成Token
     */
    @Test
    public String testGenerateToken() {
        // 调用你要测试的方法
        String token = jwtUtils.generateToken("admin"); // 假设用户名是admin
        // 打印一下看结果
        System.out.println("生成的Token：" + token);
        return token;
    }
}
