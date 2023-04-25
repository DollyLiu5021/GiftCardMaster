package com.example.giftcardmaster.exception;

public enum ServiceError {

    // common 1000xx
    COMMON_INPUT_INVALID(100001, "Invalid input"),
    COMMON_COOKIE_ISNULL(100002, "Cookie is null"),
    COMMON_ENTITY_NOT_EXIST(100003, "Entity not exist"),
    COMMON_ACTION_NO_AUTHORITY(100004, "User does not have permission"),
    COMMON_JSON_ILLEGAL(100009, "Json transfer error"),
    COMMON_XML_ILLEGAL(1000010, "Xml transfer error"),
    COMMON_STRING_ILLEGAL(1000011, "String data error");

    public int code;
    public String message;

    ServiceError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
