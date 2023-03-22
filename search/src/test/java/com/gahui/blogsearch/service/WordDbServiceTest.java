package com.gahui.blogsearch.service;

import com.gahui.blogsearch.model.PopularWord;
import com.gahui.blogsearch.repository.PopularWordRepository;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class WordDbServiceTest {
    PopularWordRepository popularWordRepository = mock(PopularWordRepository.class);

    WordDbService wordDbService = new WordDbService(popularWordRepository);


    @Test
    void upsertWord() {

        // given
        val word = "test";
        val popularWord = Optional.of(PopularWord.insertWord().word(word).cnt(1).build());

        doReturn(popularWord).when(popularWordRepository).findPopularWordsByWord(word);
        doNothing().when(popularWordRepository).updateWordCnt(word, 1);

        // when
        wordDbService.upsertWord(word);

        // then
        verify(popularWordRepository).findPopularWordsByWord(word);
        verify(popularWordRepository).updateWordCnt(word, 1);
    }

    @Test
    void getPopularWordById() {
        // given
        val word = "test";
        val popularWord = Optional.of(PopularWord.insertWord().word(word).cnt(1).build());

        doReturn(popularWord).when(popularWordRepository).findPopularWordsByWord(word);

        // when
        wordDbService.getPopularWordById(word);

        // then
        verify(popularWordRepository).findPopularWordsByWord(word);

    }

    @Test
    void updateWordCnt() {
        // given
        val word = "test";
        val cnt = 1;

        doNothing().when(popularWordRepository).updateWordCnt(word, cnt);

        // when
        wordDbService.updateWordCnt(word, cnt);

        // then
        verify(popularWordRepository).updateWordCnt(word, cnt);
    }

    @Test
    void getPopularWordLimit1000() {
        // given
        val word = "test";
        val cnt = 1;
        val sort = Sort.by(
                Sort.Order.desc("cnt"),
                Sort.Order.desc("updateDt"));

        List<PopularWord> popularWords = new ArrayList<>();
        popularWords.add(PopularWord.builder().word(word).cnt(cnt).build());
        popularWords.add(PopularWord.builder().word(word).cnt(cnt+1).build());
        popularWords.add(PopularWord.builder().word(word).cnt(cnt+2).build());

        doReturn(Optional.of(popularWords)).when(popularWordRepository).findTop1000By(sort);

        // when
        val result = wordDbService.getPopularWordLimit1000();

        // then
        verify(popularWordRepository).findTop1000By(sort);
        assert result != null;
        assert result.size() == 3;
        assert result.get(0).getCnt() == 1;
    }

}