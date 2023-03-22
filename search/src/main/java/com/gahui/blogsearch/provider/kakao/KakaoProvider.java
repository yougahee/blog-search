package com.gahui.blogsearch.provider.kakao;

import domain.request.SearchQuery;
import com.gahui.blogsearch.provider.kakao.response.Document;
import com.gahui.blogsearch.provider.kakao.response.KakaoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoProvider {

    @Value("${rest.api.kakao.key}")
    private String kakaoKey;

    private final KakaoClient kakaoClient;

    public KakaoResponse<Document> searchBlogApi(SearchQuery searchQuery) {

        return kakaoClient.searchBlog(kakaoKey,
                searchQuery.getQuery(),
                searchQuery.getSort().getCode(),
                searchQuery.getPage(),
                searchQuery.getSize());
    }
}
