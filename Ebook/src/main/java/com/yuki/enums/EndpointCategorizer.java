package com.yuki.enums;

import lombok.Getter;

@Getter
public class EndpointCategorizer {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/rest/author/**",
            "/user/getemailbyusername", "/user/check-login", "/user/signin", "/user/signup-client", "/user/logout",
            "/api/otp/**",
            "/rest/users/**",
            "/reset/**",
            "/rest/books/**", "/books", "/books/**", "/rest/bookBokTypes", "/rest/booktypes"
            , "/rest/packagePlan/**", "/rest/category", "/rest/category/**", "/rest/comment/**", "/publisher/**"
    };
    public static final String[] ADMIN_ENDPOINTS = {"/user/getuser",
            "/rest/address/**",
            "/user/edit/**",
            "/user/signup", "/user/updateUser/**",
            "/rest/",
            "/rest/cartdetails/**", "/rest/cartdetails",
            "/rest/books/**", "/rest/books"

    };
    public static final String[] AD_CT_ENDPOINTS = {
            "/rest/readBook/**", "/rest/paymentMethod/**", "/rest/packagePlan/**", "/user/deleteUser/**", "/user/chapter/**", "/rest/order/**"
            , "/rest/reviews/**", "/rest/userSubscription/**"};

    public static final String[] CLIENT_ENDPOINTS = {
            "/rest/address/**",
            "/api/loginGoogle",
            "/payment", "/rest/packagePlan/**"
    };

}

