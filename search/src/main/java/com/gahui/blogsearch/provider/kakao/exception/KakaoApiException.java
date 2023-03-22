package com.gahui.blogsearch.provider.kakao.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoApiException extends Throwable {
    private String code;
    private String message;
}