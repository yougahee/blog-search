package com.gahui.blogsearch.domain.enums;

public enum SortType {
    ACCURACY("accuracy"),
    RECENCY("recency");

    private final String code;

    SortType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
