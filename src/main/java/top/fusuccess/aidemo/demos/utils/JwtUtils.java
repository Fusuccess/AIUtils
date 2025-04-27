package top.fusuccess.aidemo.demos.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtUtils {
    // 秘钥
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // 过期时间，单位：毫秒
    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    /**
     * 生成Token
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // 设置主题（用户标识）
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // 签名算法+秘钥
                .compact();
    }

    /**
     * 解析Token
     */
    public  Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY) // 解析时也需要秘钥
                .parseClaimsJws(token)
                .getBody();
    }
}
