package com.wisdom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.dto.UserLoginDTO;
import com.wisdom.dto.UserRegisterDTO;
import com.wisdom.handler.GlobalExceptionHandler;
import com.wisdom.service.UserService;
import com.wisdom.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerApiTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void loginShouldReturnSuccessWhenCredentialsAreValid() throws Exception {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setUsername("admin");
        dto.setPassword("123456");

        when(userService.login(any(UserLoginDTO.class))).thenReturn("token");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.data").value("token"));
    }

    @Test
    void loginShouldReturnErrorWhenUserDoesNotExist() throws Exception {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setUsername("missing");
        dto.setPassword("123456");

        when(userService.login(any(UserLoginDTO.class)))
                .thenThrow(new RuntimeException("USER_NOT_FOUND"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("SERVER_BUSY"));
    }

    @Test
    void loginShouldReturnErrorWhenPasswordIsWrong() throws Exception {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setUsername("admin");
        dto.setPassword("bad-password");

        when(userService.login(any(UserLoginDTO.class)))
                .thenThrow(new RuntimeException("PASSWORD_ERROR"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("SERVER_BUSY"));
    }

    @Test
    void loginShouldReturnBadRequestWhenJsonIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerShouldReturnSuccessWhenPayloadIsValid() throws Exception {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("newUser");
        dto.setPassword("123456");
        dto.setRealName("Test User");

        doNothing().when(userService).register(any(UserRegisterDTO.class));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("success"));
    }

    @Test
    void registerShouldReturnErrorWhenUsernameAlreadyExists() throws Exception {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("admin");
        dto.setPassword("123456");

        doThrow(new RuntimeException("USERNAME_ALREADY_EXISTS"))
                .when(userService)
                .register(any(UserRegisterDTO.class));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("SERVER_BUSY"));
    }

    @Test
    void getCurrentUserInfoShouldReturnSuccessWhenUserExists() throws Exception {
        UserVO userVO = new UserVO();
        userVO.setId(1L);
        userVO.setUsername("admin");
        userVO.setRealName("Admin");

        when(userService.getCurrentUserInfo()).thenReturn(userVO);

        mockMvc.perform(get("/api/v1/auth/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("admin"));
    }
}
