package com.axisbanking.auth.service;

import com.axisbanking.auth.dto.AuthRequest;
import com.axisbanking.auth.dto.AuthResponse;
import com.axisbanking.auth.dto.RegisterRequest;
import com.axisbanking.auth.model.Role;
import com.axisbanking.auth.model.UserEntity;
import com.axisbanking.auth.repository.UserRepository;
import com.axisbanking.auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, Object> redisTemplate;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Role role = Role.CUSTOMER;
        if (request.getRole() != null) {
            try { role = Role.valueOf(request.getRole()); } catch (IllegalArgumentException ignored) {}
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .role(role)
                .build();

        userRepository.save(user);
        return generateAuthResponse(user);
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return generateAuthResponse(user);
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = tokenProvider.extractUsername(refreshToken);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return generateAuthResponse(user);
    }

    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        redisTemplate.opsForValue().set("blacklist:" + token, "revoked",
                tokenProvider.getExpiration(), TimeUnit.MILLISECONDS);
    }

    public boolean validateToken(String token) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token))) {
            return false;
        }
        return tokenProvider.validateToken(token);
    }

    private AuthResponse generateAuthResponse(UserEntity user) {
        String accessToken = tokenProvider.generateToken(user.getUsername(), user.getRole().name());
        String refreshToken = tokenProvider.generateRefreshToken(user.getUsername());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(tokenProvider.getExpiration() / 1000)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }
}
