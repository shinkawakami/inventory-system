package com.example.inventory.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.entity.User;
import com.example.inventory.exception.BusinessException;
import com.example.inventory.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public User login(String loginId, String rawPassword) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BusinessException("ログインIDまたはパスワードが違います。"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BusinessException("ログインIDまたはパスワードが違います。");
        }

        return user;
    }
}