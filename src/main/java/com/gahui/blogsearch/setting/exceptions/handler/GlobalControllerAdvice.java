package com.gahui.blogsearch.setting.exceptions.handler;



import com.gahui.blogsearch.domain.RestResponse;
import com.gahui.blogsearch.domain.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@Slf4j
@Component
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public RestResponse<Void> exceptionHandler(Exception e) {
        log.error("Exception :", e);
        return new RestResponse<>(ResponseCode.EXCEPTION_ERROR);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public RestResponse<Object> exceptionHandler(MissingServletRequestParameterException e) {
        log.info("MissingServletRequestParameterException : ", e);
        return new RestResponse<>(ResponseCode.INVALID_PARAM).appendMessage(e.getParameterName());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public RestResponse<Object> methodValidationExceptionHandler(ConstraintViolationException e) {
        log.info("ConstraintViolationException : ", e);
        return new RestResponse<>(ResponseCode.INVALID_PARAM).appendMessage(e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public RestResponse<Object> exceptionHandler(IllegalArgumentException e) {
        log.info("IllegalArgumentException : ", e);
        return new RestResponse<>(ResponseCode.INVALID_PARAM).appendMessage(e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public RestResponse<Object> exceptionHandler(MethodArgumentTypeMismatchException e) {
        log.info("MethodArgumentNotValidException : ", e);
        return new RestResponse<>(ResponseCode.INVALID_PARAM)
                .appendMessage(e.getName());
    }
}
