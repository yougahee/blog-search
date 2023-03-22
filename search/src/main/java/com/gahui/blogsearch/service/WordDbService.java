package com.gahui.blogsearch.service;

import com.gahui.blogsearch.model.PopularWord;
import com.gahui.blogsearch.repository.PopularWordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordDbService {
    private final PopularWordRepository popularWordRepository;

    @Transactional
    public void upsertWord(String word) {
        val popularWord = getPopularWordById(word);

        if (Objects.isNull(popularWord)) {
            insertWord(word, 1);
        } else {
            updateWordCnt(word, 1);
        }
    }

    public PopularWord getPopularWordById(String word) {
        return popularWordRepository.findPopularWordsByWord(word)
                .orElse(null);
    }

    private void insertWord(String word, int cnt) {
        popularWordRepository.save(PopularWord.insertWord()
                .word(word)
                .cnt(cnt)
                .build());
    }

    public void updateWordCnt(String word, int cnt) {
        popularWordRepository.updateWordCnt(word, cnt);
    }

    public List<PopularWord> getPopularWordLimit1000() {
        return popularWordRepository.findTop1000By(getSort())
                .orElse(Collections.emptyList());
    }

    public Sort getSort() {
        return Sort.by(
                Sort.Order.desc("cnt"),
                Sort.Order.desc("updateDt")
        );
    }

}
