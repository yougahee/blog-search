package com.gahui.blogsearch.controller;

import com.gahui.blogsearch.aop.response.annotation.SearchRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@SearchRes
@Controller
@RequiredArgsConstructor
public class SearchController {

    // todo - 수정 필요 일단 임시 방편으로 하나 만들어 놓음
    @GetMapping()
    public List<String> blogSearch() {

    }
}
