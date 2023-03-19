package com.gahui.blogsearch.provider.kakao;

import com.gahui.blogsearch.provider.kakao.fallback.KakaoClientFallbackFactory;
import com.gahui.blogsearch.provider.kakao.response.Document;
import com.gahui.blogsearch.provider.kakao.response.KakaoResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@ConditionalOnProperty(name = "rest.api.kakao.url")
@FeignClient(name = "kakaoClient", url = "${rest.api.kakao.url}",
        fallbackFactory = KakaoClientFallbackFactory.class
)
public interface KakaoClient {

    // 블로그 검색 오픈 API
    @GetMapping(value = "/v2/search/blog")
    KakaoResponse<Document> searchBlog(@RequestHeader(value = "Authorization") String Authorization,
                                       @RequestParam(value = "query") String query,
                                       @RequestParam(value = "sort", required = false) String sort,
                                       @RequestParam(value = "page", required = false) int page,
                                       @RequestParam(value = "size", required = false) int size);
}

