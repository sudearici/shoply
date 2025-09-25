package com.sudearici.demo.service;

import com.sudearici.demo.domain.User;
import com.sudearici.demo.repository.UserRepository;
import com.sudearici.demo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public User getUserFromToken(String authHeader) {
        String token = authHeader.substring(7); // Bearer <token>
        String username = jwtUtil.extractUsername(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
