package top.fusuccess.aidemo.demos.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.fusuccess.aidemo.demos.common.ApiResponse;
import top.fusuccess.aidemo.demos.utils.JwtUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // 1. 处理跨域预检请求（OPTIONS）
        if (path.startsWith("/auth/login") ||
                path.startsWith("/auth/register") ||
                path.startsWith("/rsa/publicKey") ||
                "OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return; // 直接返回，不继续执行过滤器链
        }

        // 2. 获取并验证Token
        String token = request.getHeader("Authorization");
        ApiResponse apiResponse = new ApiResponse("error", "Token invalid or expired");

        // 3. Token不存在或不以Bearer开头
        if (token == null || !token.startsWith("Bearer ")) {
            sendErrorResponse(response, apiResponse);
            return;
        }

        // 4. 解析Token
        try {
            Claims claims = jwtUtils.parseToken(token.substring(7)); // 移除Bearer前缀
            String substringToken = token.substring(7);
            String username = claims.getSubject();
            String redisToken = redisTemplate.opsForValue().get("token_"+username);
            if (redisToken == null || !redisToken.equals(substringToken)) {
                sendErrorResponse(response, new ApiResponse("error", "连接超时，请重新登陆"));
                return;
            }else{
                // 刷新token有效期
                redisTemplate.expire("token_"+username,30, TimeUnit.MINUTES);
            }

            // 5. 创建Authentication对象并存入SecurityContext
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            claims.getSubject(), // 通常使用用户名
                            null, // 凭证设为null（密码不需要）
                            new ArrayList<>() // 权限列表（可根据claims添加）
                    );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 6. 将claims存入请求属性（保持与原拦截器一致）
            request.setAttribute("claims", claims);

        } catch (Exception e) {
            sendErrorResponse(response, apiResponse);
            return;
        }

        // 7. 继续执行过滤器链
        filterChain.doFilter(request, response);
    }

    private void handleOptionsRequest(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Max-Age", "3600");
    }

    private void sendErrorResponse(HttpServletResponse response, ApiResponse apiResponse) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }
}
