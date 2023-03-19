package com.gahui.blogsearch.setting.exceptions;

import com.gahui.blogsearch.domain.enums.ResponseCode;
import lombok.Getter;

@Getter
public class SearchException extends RuntimeException {
    private static final long serialVersionUID = -2519782171147766941L;

    private final ResponseCode responseCode;

    public SearchException(String message) {
        super(message);
        responseCode = ResponseCode.EXCEPTION_ERROR;
    }

    public SearchException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public SearchException(ResponseCode responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }
}
