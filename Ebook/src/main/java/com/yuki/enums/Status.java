package com.yuki.enums;

public class Status {
    public static final String UNVERIFIED = "UNVERIFIED";
    public static final String ACTIVE = "ACTIVE";
    public static final String SUSPENDED = "SUSPENDED";
    public static final String BANNED = "BANNED";
    public static final String DELETED = "DELETED";

    private Status() {
        throw new IllegalStateException("Utility class");
    }
}
