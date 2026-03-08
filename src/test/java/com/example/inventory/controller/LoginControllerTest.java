package com.example.inventory.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.inventory.constant.Role;
import com.example.inventory.entity.User;
import com.example.inventory.exception.BusinessException;
import com.example.inventory.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    @DisplayName("GET /login でログイン画面が表示される")
    void loginForm_正常() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/login"))
                .andExpect(model().attributeExists("loginForm"));
    }

    @Test
    @DisplayName("POST /login 成功で /dashboard にリダイレクトしセッションにユーザー情報が入る")
    void login_成功() throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setLoginId("admin");
        user.setUserName("管理者ユーザー");
        user.setRole(Role.ADMIN);

        when(authService.login("admin", "admin123")).thenReturn(user);

        mockMvc.perform(post("/login")
                        .param("loginId", "admin")
                        .param("password", "admin123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andExpect(request().sessionAttribute("loginUserId", 1))
                .andExpect(request().sessionAttribute("loginUserName", "管理者ユーザー"))
                .andExpect(request().sessionAttribute("loginUserRole", Role.ADMIN));
    }

    @Test
    @DisplayName("POST /login 認証失敗でログイン画面に戻る")
    void login_認証失敗() throws Exception {
        when(authService.login("admin", "wrong-password"))
                .thenThrow(new BusinessException("ログインIDまたはパスワードが違います。"));

        mockMvc.perform(post("/login")
                        .param("loginId", "admin")
                        .param("password", "wrong-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/login"))
                .andExpect(model().attributeExists("loginForm"))
                .andExpect(model().attribute("errorMessage", "ログインIDまたはパスワードが違います。"));
    }

    @Test
    @DisplayName("POST /login 入力値未設定ならバリデーションエラーでログイン画面に戻る")
    void login_バリデーションエラー() throws Exception {
        mockMvc.perform(post("/login")
                        .param("loginId", "")
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("login/login"))
                .andExpect(model().attributeHasFieldErrors("loginForm", "loginId", "password"));
    }

    @Test
    @DisplayName("GET /login/logout でログアウトし /login にリダイレクトする")
    void logout_正常() throws Exception {
        mockMvc.perform(get("/login/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}