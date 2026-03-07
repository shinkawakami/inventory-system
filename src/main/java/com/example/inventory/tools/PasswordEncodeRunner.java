package com.example.inventory.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncodeRunner {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("admin123 => " + encoder.encode("admin123"));
        System.out.println("staff123 => " + encoder.encode("staff123"));
    }
}