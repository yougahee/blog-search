package com.gahui.blogsearch.service;

import com.gahui.blogsearch.model.PopularWord;
import com.gahui.blogsearch.repository.PopularWordRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PopularWordService {
    private final PopularWordRepository popularWordRepository;

    public PopularWord getPopularWord(String word) {
        return popularWordRepository.findByWord(word);
    }

    public void insertWord(String word) {
        val result = PopularWord.insertWord()
                .word(word)
                .build();

        popularWordRepository.save(result);
    }

    public void updateWordCnt(String word) {
        popularWordRepository.updateWordCnt(word);
    }

    public List<PopularWord> getPopularWords() {
        return popularWordRepository.findTop10By(getSort())
                .orElse(Collections.emptyList());
    }

    public Sort getSort() {
        return Sort.by(
                Sort.Order.desc("cnt"),
                Sort.Order.desc("updateDt")
        );
    }

}
