package com.clothing.enterprise.auth.service;

import com.clothing.enterprise.auth.dto.request.LoginRequest;
import com.clothing.enterprise.auth.dto.request.RegisterRequest;
import com.clothing.enterprise.auth.dto.response.LoginResponse;
import com.clothing.enterprise.auth.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}