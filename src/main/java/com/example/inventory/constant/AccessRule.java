package com.example.inventory.constant;

import java.util.List;

public class AccessRule {

    private AccessRule() {
    }

    public static final List<String> PUBLIC_PATH_PREFIXES = List.of(
            "/login",
            "/css/",
            "/js/",
            "/images/"
    );

    public static final List<String> ADMIN_ONLY_PATH_PREFIXES = List.of(
            "/product/regist",
            "/product/edit",
            "/product/delete",
            "/warehouse/regist",
            "/stock/regist",
            "/stock/edit",
            "/stock/delete"
    );

    public static boolean isPublicPath(String requestUri) {
        return PUBLIC_PATH_PREFIXES.stream()
                .anyMatch(requestUri::startsWith);
    }

    public static boolean isAdminOnlyPath(String requestUri) {
        return ADMIN_ONLY_PATH_PREFIXES.stream()
                .anyMatch(requestUri::startsWith);
    }
}