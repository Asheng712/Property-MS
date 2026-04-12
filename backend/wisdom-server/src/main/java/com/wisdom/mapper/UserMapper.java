package com.wisdom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wisdom.entity.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {
    @Select("select * from sys_user where username = #{username}")
    User selectByUsername(String username);
}