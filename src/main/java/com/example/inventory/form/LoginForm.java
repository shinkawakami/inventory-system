package com.example.inventory.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginForm {

    @NotBlank(message = "ログインIDは必須です")
    @Size(max = 50, message = "ログインIDは50文字以内で入力してください")
    private String loginId;

    @NotBlank(message = "パスワードは必須です")
    @Size(max = 100, message = "パスワードは100文字以内で入力してください")
    private String password;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}