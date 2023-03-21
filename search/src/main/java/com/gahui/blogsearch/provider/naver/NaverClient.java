package com.gahui.blogsearch.provider.naver;

import com.gahui.blogsearch.provider.naver.fallback.NaverClientFallbackFactory;
import com.gahui.blogsearch.provider.naver.response.Item;
import com.gahui.blogsearch.provider.naver.response.NaverResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@ConditionalOnProperty(name = "rest.api.naver.url")
@FeignClient(name = "NaverClient", url = "${rest.api.naver.url}",
        fallbackFactory = NaverClientFallbackFactory.class)
public interface NaverClient {

    @GetMapping("/v1/search/blog.json")
    NaverResponse<Item> searchBlog(@RequestHeader(value = "X-Naver-Client-Id") String id,
                                   @RequestHeader(value = "X-Naver-Client-Secret") String secret,
                                   @RequestParam(value = "query") String query,
                                   @RequestParam(value = "sort", required = false) String sort,
                                   @RequestParam(value = "start", required = false) int start,
                                   @RequestParam(value = "display", required = false) int display);

}
