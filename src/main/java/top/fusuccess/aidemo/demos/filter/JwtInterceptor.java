package top.fusuccess.aidemo.demos.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.fusuccess.aidemo.demos.common.ApiResponse;
import top.fusuccess.aidemo.demos.utils.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 处理跨域预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);  // 返回 200 状态码
            response.setHeader("Access-Control-Allow-Origin", "*"); // 允许所有来源的跨域请求
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"); // 允许的方法
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type"); // 允许的请求头
            response.setHeader("Access-Control-Max-Age", "3600"); // 缓存预检请求的时间
            return false;  // 返回 false 以防止进一步处理该请求
        }
        // 获取请求头中的Authorization字段
        String token = request.getHeader("Authorization");
        ApiResponse apiResponse = new ApiResponse("error", "Token invalid or expired");
        // 如果请求头中没有Authorization字段，则直接通过，不需要校验Token（可以根据实际情况调整）
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 设置401状态码
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse)); // 将ApiResponse转为JSON字符串并返回
            return false; // 拦截请求
        }
            // 解析Token，获取Claims
            try {
                Claims claims = jwtUtils.parseToken(token.substring(7)); // 移除Bearer前缀
                request.setAttribute("claims", claims); // 将解析出的Claims放入请求中，方便后续使用
            } catch (Exception e) {
                // Token校验失败，返回统一格式的ApiResponse
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 设置401状态码
                response.setContentType("application/json");
                response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse)); // 将ApiResponse转为JSON字符串并返回
                return false; // 拦截请求
            }
        return true; // 校验成功，继续执行后续的请求处理
    }
}
