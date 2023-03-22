package com.gahui.blogsearch.provider.naver;

import domain.enums.SortType;
import domain.request.SearchQuery;
import com.gahui.blogsearch.provider.naver.response.Item;
import com.gahui.blogsearch.provider.naver.response.NaverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import util.StringUtils;

@Component
@RequiredArgsConstructor
public class NaverProvider {

    @Value("${rest.api.naver.client.id}")
    private String clientId;

    @Value("${rest.api.naver.client.secret}")
    private String clientSecret;

    private final NaverClient naverClient;

    public NaverResponse<Item> searchBlogApi(SearchQuery searchQuery) {
        return naverClient.searchBlog(clientId, clientSecret,
                StringUtils.encodingWord(searchQuery.getQuery()),
                getNaverSortType(searchQuery.getSort()),
                searchQuery.getPage(),
                searchQuery.getSize());
    }

    private String getNaverSortType(SortType sortType) {
        String result = "sim";

        if (SortType.RECENCY.equals(sortType)) result = "date";

        return result;
    }

}
