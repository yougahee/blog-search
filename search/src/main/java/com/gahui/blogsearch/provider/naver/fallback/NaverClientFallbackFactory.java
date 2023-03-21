package com.gahui.blogsearch.provider.naver.fallback;

import com.gahui.blogsearch.provider.naver.NaverClient;
import com.gahui.blogsearch.provider.naver.response.NaverResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class NaverClientFallbackFactory implements FallbackFactory<NaverClient> {

    @Override
    public NaverClient create(Throwable cause) {
        return (id, secret, query, sort, start, display) -> {
            log.error("naver feign client Error , {} ", cause.getMessage());
            return new NaverResponse<>();
        };
    }
}
