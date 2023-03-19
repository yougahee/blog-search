package com.gahui.blogsearch.provider.kakao.response;

import java.util.HashMap;
import java.util.Map;

public enum KakaoApiResponseCode {
    SUCCESS("SUCCESS", "success"),
    INVALID_PARAM("InvalidArgument", "size is more than max");
    ;
    private final static Map<Integer, KakaoApiResponseCode> innerStorage = new HashMap<>(50);

    private final String errorType;
    private final String message;

    KakaoApiResponseCode(String errorType, String msg) {
        this.errorType = errorType;
        this.message = msg;
    }

    public String errorType() {
        return this.errorType;
    }

    public String message() {
        return this.message;
    }
}
