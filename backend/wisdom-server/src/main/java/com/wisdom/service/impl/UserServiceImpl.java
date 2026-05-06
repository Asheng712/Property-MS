package com.wisdom.service.impl;

import com.wisdom.dto.UserLoginDTO;
import com.wisdom.dto.UserRegisterDTO;
import com.wisdom.entity.User;
import com.wisdom.mapper.UserMapper;
import com.wisdom.service.UserService;
import com.wisdom.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        User user = userMapper.selectByUsername(userLoginDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("USER_NOT_FOUND");
        }
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("PASSWORD_ERROR");
        }
        return "token";
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        User existingUser = userMapper.selectByUsername(userRegisterDTO.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("USERNAME_ALREADY_EXISTS");
        }
        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setStatus(1);
        userMapper.insert(user);
    }

    @Override
    public UserVO getCurrentUserInfo() {
        Long userId = 1L;
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("USER_NOT_FOUND");
        }
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setRealName(user.getRealName());
        userVO.setPhone(user.getPhone());
        userVO.setEmail(user.getEmail());
        userVO.setAvatar(user.getAvatar());
        userVO.setStatus(user.getStatus());
        userVO.setRoleId(user.getRoleId());
        userVO.setRoleName("SUPER_ADMIN");
        return userVO;
    }
}
