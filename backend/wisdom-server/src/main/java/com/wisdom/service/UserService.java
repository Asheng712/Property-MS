package com.wisdom.service;

import com.wisdom.dto.UserLoginDTO;
import com.wisdom.dto.UserRegisterDTO;
import com.wisdom.vo.UserVO;

public interface UserService {
    String login(UserLoginDTO userLoginDTO);
    void register(UserRegisterDTO userRegisterDTO);
    UserVO getCurrentUserInfo();
}