package com.wisdom.controller;

import com.wisdom.annotation.LoginRequired;
import com.wisdom.dto.UserLoginDTO;
import com.wisdom.dto.UserRegisterDTO;
import com.wisdom.result.Result;
import com.wisdom.service.UserService;
import com.wisdom.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = "application/json")
@Tag(name = "认证与权限模块", description = "用户登录、注册、获取用户信息等接口")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        String token = userService.login(userLoginDTO);
        return Result.success(token);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<Void> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        userService.register(userRegisterDTO);
        return Result.success(null);
    }

    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息")
    @LoginRequired
    public Result<UserVO> getCurrentUserInfo() {
        UserVO userVO = userService.getCurrentUserInfo();
        return Result.success(userVO);
    }
}