package top.fusuccess.aidemo.demos.module.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.fusuccess.aidemo.demos.common.ApiResponse;
import top.fusuccess.aidemo.demos.module.auth.entity.UserEntity;
import top.fusuccess.aidemo.demos.module.auth.service.UserService;
import top.fusuccess.aidemo.demos.utils.JwtUtils;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        UserEntity userEntity = userService.findByUserName(username);
        if (userEntity == null) {
            return ResponseEntity.ok().body(new ApiResponse("error", "用户不存在"));
        }
        // 简单验证（实际应用应该查询数据库）
        if (userEntity.getPassword().equals(password)) {
            String token = jwtUtils.generateToken(username);
            return ResponseEntity.ok().body(new ApiResponse("success", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("error", "用户名或密码错误"));
        }
    }

}

