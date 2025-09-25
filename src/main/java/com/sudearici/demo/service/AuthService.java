package com.sudearici.demo.service;

import com.sudearici.demo.domain.Role;
import com.sudearici.demo.domain.User;
import com.sudearici.demo.repository.RoleRepository;
import com.sudearici.demo.repository.UserRepository;
import com.sudearici.demo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String register(String username, String email, String password) {
        if (userRepository.existsByUsername(username)) throw new RuntimeException("Username already taken");
        if (userRepository.existsByEmail(email)) throw new RuntimeException("Email already in use");

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
        user.setRoles(new HashSet<>() {{ add(role); }});

        userRepository.save(user);

        return jwtUtil.generateToken(username);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(username);
    }
}
