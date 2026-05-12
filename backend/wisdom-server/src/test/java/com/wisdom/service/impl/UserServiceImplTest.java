package com.wisdom.service.impl;

import com.wisdom.context.BaseContext;
import com.wisdom.dto.UserLoginDTO;
import com.wisdom.dto.UserRegisterDTO;
import com.wisdom.entity.User;
import com.wisdom.mapper.UserMapper;
import com.wisdom.util.JwtTokenUtil;
import com.wisdom.vo.UserVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        BaseContext.setCurrentId(1L);
    }

    @AfterEach
    void tearDown() {
        BaseContext.removeCurrentId();
    }

    @Test
    void loginShouldReturnTokenWhenCredentialsAreCorrect() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setUsername("admin");
        dto.setPassword("123456");

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("$2a$10$hashedPassword");

        when(userMapper.selectByUsername("admin")).thenReturn(user);
        when(passwordEncoder.matches("123456", "$2a$10$hashedPassword")).thenReturn(true);
        when(jwtTokenUtil.generateToken(1L, "admin")).thenReturn("test-token");

        String token = userService.login(dto);

        assertEquals("test-token", token);
    }

    @Test
    void loginShouldThrowWhenUsernameDoesNotExist() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setUsername("missing");
        dto.setPassword("123456");

        when(userMapper.selectByUsername("missing")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.login(dto));

        assertEquals("USER_NOT_FOUND", ex.getMessage());
    }

    @Test
    void loginShouldThrowWhenPasswordIsWrong() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setUsername("admin");
        dto.setPassword("wrong");

        User user = new User();
        user.setUsername("admin");
        user.setPassword("$2a$10$hashedPassword");

        when(userMapper.selectByUsername("admin")).thenReturn(user);
        when(passwordEncoder.matches("wrong", "$2a$10$hashedPassword")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.login(dto));

        assertEquals("PASSWORD_ERROR", ex.getMessage());
    }

    @Test
    void registerShouldInsertUserWhenUsernameIsAvailable() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("newUser");
        dto.setPassword("123456");
        dto.setRealName("Test User");
        dto.setPhone("13800000000");
        dto.setEmail("test@example.com");
        dto.setAvatar("avatar.png");
        dto.setRoleId(2L);

        when(userMapper.selectByUsername("newUser")).thenReturn(null);
        when(passwordEncoder.encode("123456")).thenReturn("$2a$10$encodedPassword");

        userService.register(dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userMapper, times(1)).insert(captor.capture());

        User savedUser = captor.getValue();
        assertEquals("newUser", savedUser.getUsername());
        assertEquals("$2a$10$encodedPassword", savedUser.getPassword());
        assertEquals("Test User", savedUser.getRealName());
        assertEquals("13800000000", savedUser.getPhone());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("avatar.png", savedUser.getAvatar());
        assertEquals(2L, savedUser.getRoleId());
        assertEquals(1, savedUser.getStatus());
    }

    @Test
    void registerShouldThrowWhenUsernameAlreadyExists() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("admin");

        User existingUser = new User();
        existingUser.setUsername("admin");

        when(userMapper.selectByUsername("admin")).thenReturn(existingUser);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.register(dto));

        assertEquals("USERNAME_ALREADY_EXISTS", ex.getMessage());
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void getCurrentUserInfoShouldReturnMappedViewObject() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setRealName("Admin");
        user.setPhone("13800000000");
        user.setEmail("admin@example.com");
        user.setAvatar("avatar.png");
        user.setStatus(1);
        user.setRoleId(1L);

        when(userMapper.selectById(1L)).thenReturn(user);

        UserVO result = userService.getCurrentUserInfo();

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("admin", result.getUsername());
        assertEquals("Admin", result.getRealName());
        assertEquals("13800000000", result.getPhone());
        assertEquals("admin@example.com", result.getEmail());
        assertEquals("avatar.png", result.getAvatar());
        assertEquals(1, result.getStatus());
        assertEquals(1L, result.getRoleId());
        assertEquals("SUPER_ADMIN", result.getRoleName());
    }

    @Test
    void getCurrentUserInfoShouldThrowWhenUserDoesNotExist() {
        when(userMapper.selectById(1L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getCurrentUserInfo());

        assertEquals("USER_NOT_FOUND", ex.getMessage());
    }

    @Test
    void registerShouldQueryUsernameBeforeInsert() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("orderedUser");
        dto.setPassword("123456");

        when(userMapper.selectByUsername("orderedUser")).thenReturn(null);
        when(passwordEncoder.encode("123456")).thenReturn("$2a$10$orderedHash");

        userService.register(dto);

        verify(userMapper, times(1)).selectByUsername("orderedUser");
        verify(userMapper, times(1)).insert(any(User.class));
    }
}