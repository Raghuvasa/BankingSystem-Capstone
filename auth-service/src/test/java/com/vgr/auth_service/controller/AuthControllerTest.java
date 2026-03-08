package com.vgr.auth_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.vgr.auth_service.controller.AuthController;
import com.vgr.auth_service.dto.AuthResponse;
import com.vgr.auth_service.dto.LoginRequest;
import com.vgr.auth_service.dto.RegisterRequest;
import com.vgr.auth_service.entity.EmailOtp;
import com.vgr.auth_service.entity.User;
import com.vgr.auth_service.service.UserService;
import com.vgr.auth_service.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    void testRegister() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("raghuvasa2");

		doNothing().when(userService).register(request);

        ResponseEntity<String> response = authController.register(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully", response.getBody());

        verify(userService, times(1)).register(request);
    }

    @Test
    void testLogin() {

        LoginRequest request = new LoginRequest();
        request.setUsername("raghu");
        request.setPassword("password");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("raghu");

        when(userService.validateUser("raghu", "password"))
                .thenReturn(mockUser);

        when(jwtUtil.generateToken(eq(1L), eq("raghu"), anyList()))
                .thenReturn("mocked-jwt-token");

        AuthResponse response = authController.login(request);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());

        verify(userService).validateUser("raghu", "password");
        verify(jwtUtil).generateToken(eq(1L), eq("raghu"), anyList());
    }

    @Test
    void testVerifyOtp() {

        EmailOtp request = new EmailOtp();
        request.setEmail("test@gmail.com");
        request.setOtp("123456");

//        doNothing().when(userService)
//                .verifyOtp("test@gmail.com", "123456");

        ResponseEntity<String> response =
                authController.verifyEmailOtp(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Email verified successfully", response.getBody());

        verify(userService).verifyOtp("test@gmail.com", "123456");
    }
}