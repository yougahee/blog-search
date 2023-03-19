package com.gahui.blogsearch.service;

import com.gahui.blogsearch.domain.Page;
import com.gahui.blogsearch.domain.request.SearchQuery;
import com.gahui.blogsearch.domain.response.BlogInfo;
import com.gahui.blogsearch.domain.response.PopularWordRes;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService<T> {

    private final KakaoProvider kakaoProvider;
    private final PopularWordService popularWordService;

    /**
     * <h2> 블로그 검색 </h2>
     * - 트래픽이 많고, 저장되어 있는 데이터가 많음을 염두에 둔 구현
     * - 동시성 이슈가 발생할 수 있는 부분을 염두에 둔 구현 (예시. 키워드 별로 검색된 횟수의 정확도)
     * - 카카오 블로그 검색 API에 장애가 발생한 경우, 네이버 블로그 검색 API를 통해 데이터 제공
     *
     * @param searchQuery 검색 조건
     * @return 검색 결과
     */
    public Page<BlogInfo> searchBlog(SearchQuery searchQuery) {
        saveWord(searchQuery.getQuery()); // 검색어 저장

        val searchResult = kakaoProvider.searchBlogApi(searchQuery);

        // todo - 만약, kakao 오픈 API 에러가 나면 naver API 연동 필요
        // todo - 고민이 드는 것은... kakao API 에러가 나면 네이버 연동을 하는 주체가 어디가 될 것인가? 에 대한 의문

        val result = getDefaultSearchResult(searchResult.getMeta());
        if (ObjectUtils.isEmpty(searchResult.getDocuments())) {
            return result;
        }

        result.setList(mappingSearchResult(searchResult.getDocuments()));

        return result;
    }

    /**
     * <h2> 인기 검색어 조회 </h2>
     * <p>
     * <p>
     * todo - 설계 시, lock 고려 필요 (동시 업데이트 검토 > redis lock )
     * * - 트래픽이 많고, 저장되어 있는 데이터가 많음을 염두에 둔 구현
     * * - 동시성 이슈가 발생할 수 있는 부분을 염두에 둔 구현 (예시. 키워드 별로 검색된 횟수의 정확도)
     * * - 카카오 블로그 검색 API에 장애가 발생한 경우, 네이버 블로그 검색 API를 통해 데이터 제공
     * <p>
     * todo - 이거 설계를 어떻게 할지 고민 해야함.
     *
     * @return
     */
    public List<PopularWordRes> getPopularWord() {
        val result = new ArrayList<PopularWordRes>(); // todo - java 11

        val popularWords = popularWordService.getPopularWords();
        popularWords.forEach(x -> result.add(mappingPopularWord(x)));

        return result;
    }

    // todo - 설계 다시 해야함. 아주 그지같음. select 하는 것도.... 너무 거지같음.
    @Transactional
    public void saveWord(String searchWord) {
        try {
            // todo - findbyid 로 있으면, 데이터 update 아니면 데이터
            val popularWord = popularWordService.getPopularWord(searchWord);

            if (Objects.isNull(popularWord)) {
                popularWordService.insertWord(searchWord);
            }
            else {
                popularWordService.updateWordCnt(searchWord);
            }
        } catch (Exception e) {
            log.error("Save Word Exception : {}", e.getMessage());
        }
    }

    private List<BlogInfo> mappingSearchResult(List<Document> documents) {
        val result = new ArrayList<BlogInfo>();

        documents.forEach(x -> result.add(mappingDocument(x)));

        return result;
    }

    private PopularWordRes mappingPopularWord(com.gahui.blogsearch.model.PopularWord word) {
        val result = new PopularWordRes();

        try {
            BeanUtils.copyProperties(word, result, getNullPropertyNames(word));
        } catch (IllegalArgumentException e) {
            log.error("copyProperties Error : {}", e.getMessage());
        }

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

    public String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);

        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

    private Page<BlogInfo> getDefaultSearchResult(MetaResponse meta) {
        return Page.<BlogInfo>builder()
                .pageableCount(meta.getPageableCount())
                .totalCount(meta.getTotalCount())
                .isEnd(true)
                .list(Collections.emptyList()).build();
    }

}
