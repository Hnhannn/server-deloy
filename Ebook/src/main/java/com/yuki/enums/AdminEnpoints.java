package com.yuki.enums;

import lombok.Getter;

@Getter
public class AdminEnpoints {
    private static final String[] ADMIN_ENDPOINTS = {
            "/user/getuser",
            "/rest/adress/**",
            "/rest/",
            "/rest/cartdetails/**", "/rest/cartdetails",
            "/rest/books/**", "/rest/books"

    };
}
