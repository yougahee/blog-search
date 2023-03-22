package com.gahui.blogsearch.service;

import domain.Page;
import domain.request.SearchQuery;
import domain.response.BlogInfo;
import com.gahui.blogsearch.provider.kakao.KakaoProvider;
import com.gahui.blogsearch.provider.kakao.response.Document;
import com.gahui.blogsearch.provider.kakao.response.MetaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final KakaoProvider kakaoProvider;
    private final PopularWordService popularWordService;

    /**
     * <h2> 블로그 검색 </h2>
     *
     * @param searchQuery 검색 조건
     * @return 검색 결과
     */
    public Page<BlogInfo> searchBlog(SearchQuery searchQuery) {
        popularWordService.saveWord(searchQuery.getQuery()); // 검색어 저장

        val searchResult = kakaoProvider.searchBlogApi(searchQuery);

        val result = getDefaultSearchResult(searchQuery, searchResult.getMeta());
        if (ObjectUtils.isEmpty(searchResult.getDocuments())) {
            return result;
        }

        result.setList(mappingSearchResult(searchResult.getDocuments()));

        return result;
    }

    private Page<BlogInfo> getDefaultSearchResult(SearchQuery query, MetaResponse meta) {
        return Page.<BlogInfo>builder()
                .page(query.getPage())
                .size(query.getSize())
                .totalCount(meta.getTotalCount())
                .isEnd(meta.getIsEnd())
                .list(Collections.emptyList())
                .build();
    }

    private List<BlogInfo> mappingSearchResult(List<Document> documents) {
        val result = new ArrayList<BlogInfo>();

        documents.forEach(x -> result.add(mappingDocument(x)));

        return result;
    }

    private BlogInfo mappingDocument(Document doc) {
        val result = new BlogInfo();

        try {
            BeanUtils.copyProperties(doc, result, getNullPropertyNames(doc));
        } catch (IllegalArgumentException e) {
            log.error("copyProperties Error : {}", e.getMessage());
        }

        return result;
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);

        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

}
