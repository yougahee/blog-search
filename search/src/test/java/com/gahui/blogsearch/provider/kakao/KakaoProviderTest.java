package com.gahui.blogsearch.provider.kakao;

import com.gahui.blogsearch.provider.kakao.response.Document;
import com.gahui.blogsearch.provider.kakao.response.KakaoResponse;
import com.gahui.blogsearch.provider.kakao.response.MetaResponse;
import com.gahui.blogsearch.setting.constant.Search;
import domain.enums.SortType;
import domain.request.SearchQuery;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KakaoProviderTest {
    KakaoClient kakaoClient = mock(KakaoClient.class);

    KakaoProvider kakaoProvider = new KakaoProvider(kakaoClient);

    @Test
    void searchBlog() {
        // given
        val query = "hi";
        val searchQuery = SearchQuery.builder()
                .query(query.strip())
                .page((int) Search.MIN_PAGE)
                .size((int) Search.MIN_SIZE)
                .sort(SortType.ACCURACY)
                .build();

        val docs = new ArrayList<Document>();
        for (int i = 0; i < 5; i++) {
            docs.add(Document.builder()
                    .title("test")
                    .contents("code")
                    .blogname(i + "blog")
                    .build());
        }

        val kakaoRes = new KakaoResponse<Document>();
        kakaoRes.setMeta(new MetaResponse());
        kakaoRes.getMeta().setTotalCount(100);
        kakaoRes.setDocuments(docs);

        doReturn(kakaoRes).when(kakaoClient)
                .searchBlog("auth", query.strip(), "sort", 1, 10);

        // when
        val result = kakaoProvider.searchBlogApi(searchQuery);

        // then
        //assertNotNull(result);
        //assertEquals(5, result.getDocuments().size());
        //assertEquals(100, result.getMeta().getTotalCount());
    }
}