package com.onlineadplatform.logic;

public class AppConstants {
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
    public static final Integer NUM_OF_RECIPES_ON_INDEX_PAGE = 10;
    //SECURITY
    public static final String SECURITY_SIGN_UP_URL = "/api/auth/*";
    public static final String SECURITY_SECRET = "H34435HTUERtrrt4";
    public static final String SECURITY_TOKEN_PREFIX = "Brearer ";
    public static final String SECURITY_HEADER_STRING = "Authorization";
    public static final String SECURITY_CONTENT_TYPE = "application/json";
    public static final long SECURITY_EXPIRATION_TIME = 6000000;

    //ROLES
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";
}
