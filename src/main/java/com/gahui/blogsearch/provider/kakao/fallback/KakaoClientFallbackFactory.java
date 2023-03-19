package com.gahui.blogsearch.provider.kakao.fallback;

import com.gahui.blogsearch.domain.enums.SortType;
import com.gahui.blogsearch.domain.request.SearchQuery;
import com.gahui.blogsearch.provider.kakao.KakaoClient;
import com.gahui.blogsearch.provider.kakao.response.Document;
import com.gahui.blogsearch.provider.kakao.response.KakaoResponse;
import com.gahui.blogsearch.provider.naver.NaverProvider;
import com.gahui.blogsearch.provider.naver.response.Item;
import com.gahui.blogsearch.provider.naver.response.NaverResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoClientFallbackFactory implements FallbackFactory<KakaoClient> {
    private final NaverProvider naverProvider;

    @Override
    public KakaoClient create(Throwable throwable) {
        return (Authorization, query, sort, page, size) -> {
            log.error("Error Message : {} ", throwable.getMessage());

            val result = new KakaoResponse<Document>();

            if (throwable instanceof FeignException) {
                val naverRes = getNaverRes(query, sort, page, size);
                mappingKakaoRes(result, naverRes);
            }

            return result;
        };
    }

    private void mappingKakaoRes(KakaoResponse<Document> result, NaverResponse<Item> naverRes) {
        if (Objects.isNull(naverRes) || Objects.isNull(naverRes.getItems())) return;

        result.getMeta().setTotalCount(naverRes.getTotal());
        result.getMeta().setPageableCount(naverRes.getDisplay());
        result.getMeta().setEnd(false);

        result.setDocuments(getDocuments(naverRes.getItems()));
    }

    private List<Document> getDocuments(List<Item> items) {
        val result = new ArrayList<Document>();

        items.forEach(item -> result.add(Document.builder()
                .title(item.getTitle())
                .contents(item.getDesciption())
                .blogname(item.getBloggername())
                .url(item.getLink())
                .datetime(item.getPostdate())
                .build()));

        return result;
    }

    private NaverResponse<Item> getNaverRes(String query, String sort, int page, int size) {
        return naverProvider.searchBlogApi(getNaverSearchQuery(query, sort, page, size));
    }

    private SearchQuery getNaverSearchQuery(String query, String sort, int page, int size) {
        return SearchQuery.builder()
                .query(query)
                .sort(SortType.valueOf(sort.toUpperCase(Locale.ROOT)))
                .page(page)
                .size(size)
                .build();
    }
}
