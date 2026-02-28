package com.clothing.enterprise.auth.service.impl;

import com.clothing.enterprise.auth.domain.UserEntity;
import com.clothing.enterprise.auth.dto.request.LoginRequest;
import com.clothing.enterprise.auth.dto.response.LoginResponse;
import com.clothing.enterprise.auth.repository.UserRepository;
import com.clothing.enterprise.auth.dto.request.RegisterRequest;
import com.clothing.enterprise.auth.dto.response.UserResponse;
import com.clothing.enterprise.auth.service.AuthService;
import com.clothing.enterprise.auth.service.JwtService;
import com.clothing.enterprise.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already in use");
        }

        UserEntity user = UserEntity.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role("CUSTOMER")
                .enabled(true)
                .build();

        UserEntity savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getRole(),
                savedUser.getPassword()
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = userRepository.findByEmail(request.email())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user.getId(), user.getEmail());

        return new LoginResponse(jwtToken);
    }
}