package com.gahui.blogsearch.setting.exceptions.handler;

import com.gahui.blogsearch.domain.RestResponse;
import com.gahui.blogsearch.domain.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/***
 * Controller 까지 진입하지 못하는 에러
 */
@Slf4j
@RestControllerAdvice
public class SpringLevelExceptionHandler {

    @ResponseBody
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class})
    public RestResponse<Object> methodNotSupportedException() {
        return new RestResponse<>(ResponseCode.EXCEPTION_ERROR);
    }

    @ResponseBody
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public RestResponse<Object> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("", e);
        return new RestResponse<>(ResponseCode.EXCEPTION_ERROR);
    }

}
