package top.fusuccess.aidemo.demos.module.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.fusuccess.aidemo.demos.common.ApiResponse;
import top.fusuccess.aidemo.demos.module.auth.entity.UserEntity;
import top.fusuccess.aidemo.demos.module.auth.service.UserService;
import top.fusuccess.aidemo.demos.utils.JwtUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        UserEntity userEntity = userService.findByUserName(username);

        // 简单验证（实际应用应该查询数据库）
        if (userEntity != null && passwordEncoder.matches(password, userEntity.getPassword())) {
            String token = jwtUtils.generateToken(username);
            redisTemplate.opsForValue().set("token_"+username, token, 30, TimeUnit.MINUTES);
            return ResponseEntity.ok().body(new ApiResponse("success", token));
        } else {
            return ResponseEntity.ok().body(new ApiResponse("error", "用户名或密码错误"));
        }
    }

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest) {
        String username = registerRequest.get("username");
        String password = registerRequest.get("password");

        if (userService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", "用户名已存在"));
        }

        // 加密密码
        String encodedPassword = passwordEncoder.encode(password);

        userService.saveUser(username, encodedPassword);

        return ResponseEntity.ok().body(new ApiResponse("success", "注册成功"));
    }
}

