package top.fusuccess.aidemo.demos.module.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.fusuccess.aidemo.demos.common.ApiResponse;
import top.fusuccess.aidemo.demos.module.auth.entity.UserEntity;
import top.fusuccess.aidemo.demos.module.auth.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userServiceImpl;

    @PostMapping("user")
    public ResponseEntity<?> user(String id){
        UserEntity userEntity = userServiceImpl.findByUserId(Long.valueOf(id));
        return ResponseEntity.ok().body(new ApiResponse("success", userEntity));
    }
}
