package com.gahui.blogsearch.service;

import static java.util.stream.Collectors.toMap;

import com.gahui.blogsearch.aop.concurrent.annotation.ConcurrentAop;
import domain.response.PopularWordRes;
import com.gahui.blogsearch.model.PopularWord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PopularWordService {
    private final WordDbService wordDbService;
    private final WordLocalService wordLocalService;

    @PostConstruct
    public void init() {
        setLocalHashMapByDbData();
    }

    /**
     * <h2> 검색어 수집을 위한 비동기 작업 </h2>
     *
     * @param searchWord 검색어
     */
    @Async
    public void saveWord(String searchWord) {
        try {
            wordDbService.upsertWord(searchWord); // 메인 DB 업데이트
            wordLocalService.incSearchCnt(searchWord); // 로컬 캐시 업데이트
        } catch (Exception e) {
            log.error("Save Word Exception : {}", e.getMessage());
        }
    }

    /**
     * <h2> 인기 검색어 조회 </h2>
     *
     * @return 인기 검색어 상위 10개
     */
    public List<PopularWordRes> getPopularWords() {
        return calPopularWords();
    }

    /**
     * <h2>스케줄링</h2>
     * <br> 주기 : 5분
     * <br> db HashMap setting
     */
    @ConcurrentAop
    @Scheduled(cron = "0 0/5 * * * *")
    public void setHashMap() {
        log.info("[Scheduling] start time : {} ", LocalDateTime.now());

        // 메인 DB 인기 검색어 조회 및 셋팅
        setLocalHashMapByDbData();

        log.info("[Scheduling] complete time : {} ", LocalDateTime.now());
    }

    private List<PopularWordRes> calPopularWords() {
        return getTop10Words(wordLocalService.getSearchWordMap());
    }

    private List<PopularWordRes> getTop10Words(HashMap<String, Integer> wordsMap) {
        val sortingMap = sortByCntDesc(wordsMap.entrySet());

        return sortingMap.stream().limit(10)
                .map(t -> getPopularWordRes(t.getKey(), t.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * 인기 검색어 조회 횟수로 정렬
     */
    private List<Map.Entry<String, Integer>> sortByCntDesc(Set<Map.Entry<String, Integer>> entry) {
        val result = new LinkedList<>(entry);
        result.sort((o1, o2) -> o2.getValue() - o1.getValue());

        return result;
    }

    private PopularWordRes getPopularWordRes(String word, Integer cnt) {
        return PopularWordRes.builder()
                .word(word)
                .cnt(cnt)
                .build();
    }

    private void setLocalHashMapByDbData() {
        val wordsMap = wordDbService.getPopularWordLimit1000().stream()
                .collect(toMap(PopularWord::getWord, PopularWord::getCnt));

        wordLocalService.saveBulkDbWord(wordsMap);
    }
}
