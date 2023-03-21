package com.gahui.blogsearch.domain.enums;

public enum ResponseCode {

    RESPONSE_OK(0, "Success"),

    INVALID_PARAM(10000, "Invalid Param"),

    KAKAO_SERVER_EXCEPTION(20000, "Kakao Server Exception"),
    NAVER_SERVER_EXCEPTION(30000, "NAVER Server Exception"),

    EXCEPTION_ERROR(99999, "Exception occurred");


    private int code;
    private String message;


    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ResponseCode getResponseCode() {
        return this;
    }
}
