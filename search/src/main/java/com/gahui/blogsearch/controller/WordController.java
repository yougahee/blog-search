package com.gahui.blogsearch.controller;

import com.gahui.blogsearch.aop.response.annotation.SearchRes;
import com.gahui.blogsearch.domain.response.PopularWordRes;
import com.gahui.blogsearch.service.PopularWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SearchRes
@RestController
@RequestMapping("/v1.0")
@RequiredArgsConstructor
public class WordController {
    private final PopularWordService popularWordService;

    /**
     * <h2> 인기 검색어 조회 </h2>
     *
     * @return 인기 검색어 상위 10개
     */
    @GetMapping("/popular-word")
    public List<PopularWordRes> popularWord() {
        return popularWordService.getPopularWords();
    }

}
