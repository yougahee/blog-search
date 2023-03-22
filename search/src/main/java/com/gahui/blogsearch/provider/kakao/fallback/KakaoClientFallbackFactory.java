package com.gahui.blogsearch.provider.kakao.fallback;

import domain.enums.SortType;
import domain.request.SearchQuery;
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

import java.util.*;

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
                int httpStatus = ((FeignException) throwable).status();

                if ( String.valueOf(httpStatus).startsWith("5") ) { // 서버 내부 에러
                    val naverRes = getNaverRes(query, sort, page, size);
                    mappingKakaoRes(result, naverRes);
                }
            }

            return result;
        };
    }

    private NaverResponse<Item> getNaverRes(String query, String sort, int page, int size) {
        return naverProvider.searchBlogApi(getNaverSearchQuery(query, sort, page, size));
    }

    private void mappingKakaoRes(KakaoResponse<Document> result, NaverResponse<Item> naverRes) {
        if (Objects.isNull(naverRes) || Objects.isNull(naverRes.getItems())) return;

        result.getMeta().setTotalCount(naverRes.getTotal());
        result.getMeta().setIsEnd((naverRes.getStart() * naverRes.getDisplay() >= naverRes.getTotal()));

        result.setDocuments(getDocuments(naverRes.getItems()));
    }

    private List<Document> getDocuments(List<Item> items) {
        val result = new ArrayList<Document>();

        items.forEach(item -> result.add(getDocument(item)));

        return result;
    }

    private Document getDocument(Item item) {
        return Document.builder()
                .title(Optional.ofNullable(item.getTitle()).orElse(""))
                .contents(Optional.ofNullable(item.getDescription()).orElse(""))
                .blogname(Optional.ofNullable(item.getBloggername()).orElse(""))
                .url(Optional.ofNullable(item.getLink()).orElse(""))
                .datetime(Optional.ofNullable(item.getPostdate()).orElse(""))
                .build();
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
