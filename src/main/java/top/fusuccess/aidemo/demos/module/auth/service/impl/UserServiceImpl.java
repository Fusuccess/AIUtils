package top.fusuccess.aidemo.demos.module.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.fusuccess.aidemo.demos.module.auth.dao.UserMapper;
import top.fusuccess.aidemo.demos.module.auth.entity.UserEntity;
import top.fusuccess.aidemo.demos.module.auth.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Override
    public UserEntity findByUserId(Long userId) {
        return baseMapper.selectById(userId);
    }
}
