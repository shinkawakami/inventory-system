package com.example.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.util.Optional;

import com.example.inventory.constant.Role;
import com.example.inventory.entity.User;
import com.example.inventory.exception.BusinessException;
import com.example.inventory.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_正しいIDとパスワードならログイン成功() {
        User user = new User();
        user.setUserId(1);
        user.setLoginId("admin");
        user.setPassword("hashed-password");
        user.setUserName("管理者ユーザー");
        user.setRole(Role.ADMIN);

        when(userRepository.findByLoginId("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("admin123", "hashed-password")).thenReturn(true);

        User result = authService.login("admin", "admin123");

        assertEquals(1, result.getUserId());
        assertEquals("admin", result.getLoginId());
        assertEquals("管理者ユーザー", result.getUserName());
        assertEquals(Role.ADMIN, result.getRole());

        verify(userRepository, times(1)).findByLoginId("admin");
        verify(passwordEncoder, times(1)).matches("admin123", "hashed-password");
    }

    @Test
    void login_存在しないログインIDならBusinessException() {
        when(userRepository.findByLoginId("unknown")).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> authService.login("unknown", "password")
        );

        assertEquals("ログインIDまたはパスワードが違います。", ex.getMessage());
        verify(userRepository, times(1)).findByLoginId("unknown");
    }

    @Test
    void login_パスワード不一致ならBusinessException() {
        User user = new User();
        user.setUserId(2);
        user.setLoginId("staff");
        user.setPassword("hashed-password");
        user.setUserName("在庫担当ユーザー");
        user.setRole(Role.STAFF);

        when(userRepository.findByLoginId("staff")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "hashed-password")).thenReturn(false);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> authService.login("staff", "wrong-password")
        );

        assertEquals("ログインIDまたはパスワードが違います。", ex.getMessage());
        verify(userRepository, times(1)).findByLoginId("staff");
        verify(passwordEncoder, times(1)).matches("wrong-password", "hashed-password");
    }

    @Test
    void login_存在しないIDの場合はPasswordEncoderを呼ばない() {
        when(userRepository.findByLoginId("unknown")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> authService.login("unknown", "password"));

        verify(userRepository, times(1)).findByLoginId("unknown");
        verifyNoInteractions(passwordEncoder);
    }
}