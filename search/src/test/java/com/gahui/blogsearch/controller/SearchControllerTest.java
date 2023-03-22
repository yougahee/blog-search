package com.gahui.blogsearch.controller;

import com.gahui.blogsearch.ApplicationTests;
import com.gahui.blogsearch.service.SearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

class SearchControllerTest extends ApplicationTests {
    @MockBean
    SearchService searchService;

    @Test
    @DisplayName("블로그 검색 API 테스트")
    void searchBlog() throws Exception {
        // given
        String queryString = "?query=%EC%B2%9C%EC%9E%AC";

        // when
        ResultActions response = searchBlogActions(mockMvc, queryString);

        // then
        expectSuccess(response)
                .andDo(print());
    }

    private ResultActions searchBlogActions(MockMvc mockMvc, String queryString) throws Exception {

        return mockMvc
                .perform(get( "/v1.0/search/blog" + queryString)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(handler().handlerType(SearchController.class))
                .andExpect(handler().methodName("blogSearch"));
    }

}