package top.fusuccess.aidemo.demos.module.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.fusuccess.aidemo.demos.module.auth.dao.UserMapper;
import top.fusuccess.aidemo.demos.module.auth.entity.UserEntity;
import top.fusuccess.aidemo.demos.module.auth.service.UserService;

import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Override
    public UserEntity findByUserId(Long userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    public UserEntity findByUserName(String userName) {
        if (Objects.isNull(userName))
            return null;
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("username",userName));
    }

    @Override
    public boolean existsByUsername(String username) {
        if (Objects.isNull(username))
            return true;
        UserEntity userEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", username));
        return Objects.nonNull(userEntity);
    }

    @Override
    public void saveUser(String username, String encodedPassword) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(encodedPassword);
        baseMapper.insert(userEntity);
    }
}
