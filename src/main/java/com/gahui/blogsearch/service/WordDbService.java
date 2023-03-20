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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordDbService {
    private final PopularWordRepository popularWordRepository;

    @Transactional
    public void bulkUpsertWords(Map<String, Integer> words) {
        Set<String> wordSet = words.keySet();
        val dbExistWords = getPopularWordsById(new ArrayList<>(wordSet));
        val dbExistId = dbExistWords.stream().map(PopularWord::getWord).collect(Collectors.toSet());

        val dbNotExistWords= wordSet.stream()
                .filter(word -> !dbExistId.contains(word))
                .collect(Collectors.toList());

        // insert
        insertWords(dbNotExistWords, words);

        // update
        updateWords(dbExistWords, words);
    }

    public List<PopularWord> getPopularWordsById(List<String> words) {
        return popularWordRepository.findByWordIn(words)
                .orElse(Collections.emptyList());
    }

    private void insertWords(List<String> dbNotExistWords, Map<String, Integer> words) {
        dbNotExistWords.forEach(word -> popularWordRepository.save(PopularWord.insertWord()
                .word(word)
                .cnt(words.get(word))
                .build()));
    }

    private void updateWords(List<PopularWord> dbExistWords, Map<String, Integer> words) {
        dbExistWords.forEach(word ->
                updateWordCnt(word.getWord(), words.get(word.getWord())));
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
