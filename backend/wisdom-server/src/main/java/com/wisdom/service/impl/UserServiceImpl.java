package com.wisdom.service.impl;

import com.wisdom.context.BaseContext;
import com.wisdom.dto.UserLoginDTO;
import com.wisdom.dto.UserRegisterDTO;
import com.wisdom.entity.User;
import com.wisdom.mapper.UserMapper;
import com.wisdom.service.UserService;
import com.wisdom.util.JwtTokenUtil;
import com.wisdom.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        User user = userMapper.selectByUsername(userLoginDTO.getUsername());
        if (user == null) {
            log.warn("登录失败：用户不存在 - {}", userLoginDTO.getUsername());
            throw new RuntimeException("USER_NOT_FOUND");
        }
        
        boolean passwordMatch = passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword());
        log.info("登录尝试 - 用户名: {}, 密码匹配: {}, 数据库密码前缀: {}", 
                userLoginDTO.getUsername(), 
                passwordMatch, 
                user.getPassword() != null && user.getPassword().length() > 10 
                    ? user.getPassword().substring(0, 10) : "N/A");
        
        if (!passwordMatch) {
            log.error("登录失败：密码错误 - 用户名: {}, 输入密码长度: {}, 数据库密码长度: {}", 
                    userLoginDTO.getUsername(),
                    userLoginDTO.getPassword() != null ? userLoginDTO.getPassword().length() : 0,
                    user.getPassword() != null ? user.getPassword().length() : 0);
            throw new RuntimeException("PASSWORD_ERROR");
        }
        
        log.info("登录成功：{}", userLoginDTO.getUsername());
        return jwtTokenUtil.generateToken(user.getId(), user.getUsername());
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
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new RuntimeException("UNAUTHORIZED");
        }
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