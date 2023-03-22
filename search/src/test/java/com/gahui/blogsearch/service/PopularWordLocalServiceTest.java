package com.gahui.blogsearch.service;


import com.gahui.blogsearch.model.PopularWord;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.Mockito.*;


class PopularWordLocalServiceTest {

    WordLocalService wordLocalService = mock(WordLocalService.class);
    WordDbService wordDbService = mock(WordDbService.class);

    PopularWordService popularWordService = new PopularWordService(wordDbService, wordLocalService);

    @Test
    void initTest() {
        // given
        val wordDb = new ArrayList<PopularWord>();
        wordDb.add(PopularWord.builder().word("test").cnt(1).build());
        wordDb.add(PopularWord.builder().word("test22").cnt(1).build());

        doReturn(wordDb).when(wordDbService).getPopularWordLimit1000();
        doNothing().when(wordLocalService).saveBulkDbWord(anyMap());

        // when
        popularWordService.init();

        // then
        verify(wordDbService).getPopularWordLimit1000();
        verify(wordLocalService).saveBulkDbWord(anyMap());
    }

    @Test
    @DisplayName("[성공] 인기 검색어 조회 테스트 코드")
    void getPopularWords() {
        // given
        val wordLocalRes = new HashMap<String, Integer>();
        wordLocalRes.put("test", 1);
        wordLocalRes.put("emotion", 1);

        doReturn(wordLocalRes).when(wordLocalService).getSearchWordMap();

        // when
        val result = popularWordService.getPopularWords();

        // then
        verify(wordLocalService).getSearchWordMap();
        assert result != null;
        assert result.size() == 2;
        assert result.get(0).getCnt() == 1;
    }

    @Test
    void scheduleHashMap() {
        // given
        val wordDb = new ArrayList<PopularWord>();
        wordDb.add(PopularWord.builder().word("test").cnt(1).build());
        wordDb.add(PopularWord.builder().word("test22").cnt(1).build());

        doReturn(wordDb).when(wordDbService).getPopularWordLimit1000();
        doNothing().when(wordLocalService).saveBulkDbWord(anyMap());

        // when
        popularWordService.setHashMap();

        // then
        verify(wordDbService).getPopularWordLimit1000();
        verify(wordLocalService).saveBulkDbWord(anyMap());
    }
}