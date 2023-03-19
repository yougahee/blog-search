package com.gahui.blogsearch.controller;

import com.gahui.blogsearch.aop.response.annotation.SearchRes;
import com.gahui.blogsearch.domain.Page;
import com.gahui.blogsearch.domain.enums.SortType;
import com.gahui.blogsearch.domain.request.SearchQuery;
import com.gahui.blogsearch.domain.response.BlogInfo;
import com.gahui.blogsearch.domain.response.PopularWordRes;
import com.gahui.blogsearch.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@SearchRes
@Validated
@RestController
@RequestMapping("/v1.0")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    /**
     * <h2> 블로그 검색 API </h2>
     *
     * @param query 검색어
     * @param sort  정렬방식
     * @param page  결과 페이지 번호
     * @param size  한 페이지에 보여질 문서 수
     * @return Page<BlogInfo>
     */
    @GetMapping("/search/blog")
    public Page<BlogInfo> blogSearch(@RequestParam(value = "query") @NotEmpty String query,
                                     @RequestParam(value = "sort", required = false, defaultValue = "ACCURACY") SortType sort,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) @Max(50) int page,
                                     @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) @Max(50) int size) {

        val searchQuery = SearchQuery.builder()
                .query(query)
                .sort(sort)
                .page(page)
                .size(size)
                .build();

        return searchService.searchBlog(searchQuery);
    }


    // todo - controller 분리해야될지도 검토 필요 (searchController에 약간 성격이 안맞는 것 같다는 느낌이 들기도 (네이밍을 변경하던지)
    @GetMapping("/popular/word")
    public List<PopularWordRes> popularWord() {
        return searchService.getPopularWord();
    }

    // todo - 자동완성
}
