package com.wisdom.service;

import com.wisdom.dto.UserLoginDTO;
import com.wisdom.dto.UserRegisterDTO;
import com.wisdom.vo.UserVO;

import java.util.List;

public interface UserService {
    String login(UserLoginDTO userLoginDTO);
    void register(UserRegisterDTO userRegisterDTO);
    UserVO getCurrentUserInfo();
    boolean isCurrentUserAdmin();
    Long getRequiredCurrentUserId();
    List<Long> getOwnedHouseIds(Long userId);
}