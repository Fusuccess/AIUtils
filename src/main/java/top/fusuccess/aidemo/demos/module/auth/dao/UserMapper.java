package top.fusuccess.aidemo.demos.module.auth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.fusuccess.aidemo.demos.module.auth.entity.UserEntity;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
