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

class WordControllerTest extends ApplicationTests  {
    @MockBean
    SearchService searchService;

    @Test
    @DisplayName("인기 검색어 조회 API 테스트")
    void popularWord() throws Exception {
        // given

        // when
        ResultActions response = popularWord(mockMvc);

        // then
        expectSuccess(response)
                .andDo(print());
    }


    private ResultActions popularWord(MockMvc mockMvc) throws Exception {

        return mockMvc
                .perform(get( "/v1.0/popular-word")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(handler().handlerType(WordController.class))
                .andExpect(handler().methodName("popularWord"));
    }

}