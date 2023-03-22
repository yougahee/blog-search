package com.gahui.blogsearch.service;

import domain.request.SearchQuery;
import com.gahui.blogsearch.provider.kakao.KakaoProvider;
import com.gahui.blogsearch.provider.kakao.response.Document;
import com.gahui.blogsearch.provider.kakao.response.KakaoResponse;
import com.gahui.blogsearch.setting.constant.Search;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

class SearchServiceTest {

    KakaoProvider kakaoProvider = mock(KakaoProvider.class);
    PopularWordService popularWordService = mock(PopularWordService.class);

    SearchService searchService = new SearchService(kakaoProvider, popularWordService);

    SearchService spySearchService = spy(searchService);

    @Test
    @DisplayName("블로그 검색 API > 성공 케이스")
    void searchBlog() {
        // given
        val query = "hi";
        val searchQuery = SearchQuery.builder()
                .query(query.strip())
                .page((int) Search.MIN_PAGE)
                .size((int) Search.MIN_SIZE)
                .build();

        val kakaoResult = new KakaoResponse<Document>();
        kakaoResult.getMeta().setIsEnd(true);
        kakaoResult.getMeta().setPageableCount(100);
        kakaoResult.getMeta().setTotalCount(100);
        kakaoResult.setDocuments(Collections.singletonList(getDoc()));

        doReturn(kakaoResult).when(kakaoProvider).searchBlogApi(searchQuery);

        // when
        val result = spySearchService.searchBlog(searchQuery);

        // then
        verify(kakaoProvider).searchBlogApi(searchQuery);
        assert result.getTotalCount() > 0;
        assert result.getPage() == Search.MIN_PAGE;
        assert result.getSize() == Search.MIN_PAGE;
    }

    Document getDoc() {
        return Document.builder()
                .title("test")
                .contents("test")
                .blogname("test")
                .build();
    }
}