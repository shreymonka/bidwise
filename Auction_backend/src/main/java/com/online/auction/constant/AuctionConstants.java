package com.online.auction.constant;

public class AuctionConstants {
    public static final String TIMESTAMP = "timestamp";
    public static final String HTTP_CODE = "httpCode";
    public static final String HTTP_STATUS = "httpStatus";
    public static final String ERROR_MSG = "errorMessage";

    public static final String USER_ALREADY_PRESENT_MSG = "User Already Exists!";

    public static final String UNAUTHORIZED = "UnAuthorized";

    public static final String JWT_EXPIRED_MSG = "JWT token is expired";

    public static final int UNAUTHORIZED_STATUS_CODE = 401;

    public static final String APPLICATION_JSON = "application/json";

    public static final int INTEGER_SEVEN = 7;

    public static final String BEARER = "Bearer ";

    public static final String AUTHORIZATION = "Authorization";

    public static final String INVALID_CREDENTIALS_MSG = "Invalid Credentials";

    public static final String API_VERSION_V1 = "/api/v1";

    public static final String USER = "/user";
    public static final String EMAIL_SUBJECT = "Welcome to Bidwise!";
    public static final String EMAIL_BODY_REGISTER = "You have successfully registered in our system!\nTake control and enjoy bidding";
    public static final String ITEM = "/item";

    public static final String ITEM_CATEGORY_NOT_FOUND = "Item category is not present";
    public static final String NEGATIVE_BID_AMOUNT = "Minimum bid amount must be positive";
    public static final String EMPTY_ITEM_NAME = "Item name is required";


    public static final String PASSWORD_RESET_REQUEST = "Password Reset Request";
    public static final String PASSWORD_RESET_LINK_BODY = "To reset your password, click the link below:\n";
    public static final String PASSWORD_RESET_LINK = "http://172.17.3.242:4200/reset-password?token=";
}
