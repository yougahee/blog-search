package com.gahui.blogsearch.service;


import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.ReentrantLock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;


class PopularWordLocalServiceTest {

    WordLocalService wordService = mock(WordLocalService.class);
    WordDbService wordDbService = mock(WordDbService.class);
    ReentrantLock lock = new ReentrantLock();

    PopularWordService popularWordService = new PopularWordService(wordDbService, wordService);
    PopularWordService spyPopularWordService = spy(popularWordService);

    @Test
    void initTest() {
        // given


        // when
        spyPopularWordService.init();

        // then
    }

    @Test
    void getPopularWords() {
        // given

        // when
        val result = spyPopularWordService.getPopularWords();

        // then

    }

    @Test
    void setInitHashMap() {
        // given

        // when

        // then
    }
}