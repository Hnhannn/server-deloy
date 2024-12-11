package com.yuki.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class PublicEndpoints {
    public static final String[] ENDPOINTS = {
            "/user/getemailbyusername",
            "/user/signin",
            "/user/check-login",
            "/user/logout", "/user/signup-client",
            "/api/otp/**",
            "/rest/users/**",
            "/reset/**",
            "/rest/books/**"
    };
}
