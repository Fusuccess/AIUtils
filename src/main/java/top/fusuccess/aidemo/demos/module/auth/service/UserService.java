package top.fusuccess.aidemo.demos.module.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.fusuccess.aidemo.demos.module.auth.entity.UserEntity;

public interface UserService extends IService<UserEntity> {
    UserEntity findByUserId(Long userId);
    UserEntity findByUserName(String userName);
}
