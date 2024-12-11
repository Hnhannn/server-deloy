package com.yuki.enums;

public class RoleEnum {
    public static final String CLIENT = "CLIENT";
    public static final String ADMIN = "ADMIN";
    public static final String AUTHOR = "AUTHOR";

    private RoleEnum() {
        throw new IllegalStateException("Utility class");
    }
}
