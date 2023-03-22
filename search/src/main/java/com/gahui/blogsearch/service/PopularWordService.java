package com.gahui.blogsearch.service;

import static java.util.stream.Collectors.toMap;

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
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PopularWordService {
    private final WordDbService wordDbService;
    private final WordService wordService;
    private final ReentrantLock lock = new ReentrantLock();

    @PostConstruct
    public void init() {
        setDbWordMap();
    }

    /**
     * 검색어 수집을 위한 비동기 작업
     * @param searchWord 검색어
     */
    @Async
    public void saveWord(String searchWord) {
        try {
            wordService.incSearchCnt(searchWord);
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

    private List<PopularWordRes> calPopularWords() {
        val wordsMap = new HashMap<>(wordService.getDbWordMap());
        wordService.getSearchWordMap()
                .forEach((k, v) -> mergeWords(wordsMap, k, v));

        return getTop10Words(wordsMap);
    }

    private List<PopularWordRes> getTop10Words(HashMap<String, Integer> wordsMap) {
        val sortingMap = sortByCntDesc(wordsMap.entrySet());

        return sortingMap.stream().limit(10)
                .map(t -> getPopularWordRes(t.getKey(), t.getValue()))
                .collect(Collectors.toList());
    }

    private void mergeWords(HashMap<String, Integer> wordsMap, String key, Integer value) {
        wordsMap.merge(key, value, (k, v) -> wordsMap.get(key) + value);
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

    /**
     * <p>스케줄링</p>
     * <br> 주기 : 1분
     * <br> 1) DB에 값 업데이트
     * <br> 2) HashMap clear
     * <br> 3) db HashMap setting
     */
    @Scheduled(cron = "0 * * * * *")
    public void setHashMap() {
        log.info("[Scheduling] start time : {} ", LocalDateTime.now());

        // DB 값 업데이트
        wordDbService.bulkUpsertWords(wordService.getSearchWordMap());

        try {
            lock.lock();

            // 로컬 데이터 clear
            wordService.clearSearchWordMap();
            wordService.clearDbWordMap();

            // DB 데이터 조회 및 셋팅
            setDbWordMap();

        } catch (Exception e) {
            log.error("[settingHashMap] ReentrantLock lock failed.", e);
        } finally {
            if(Objects.nonNull(lock) ) {
                lock.unlock();
            }
        }
    }

    private void setDbWordMap() {
        val wordsMap = wordDbService.getPopularWordLimit1000().stream()
                        .collect(toMap(PopularWord::getWord, PopularWord::getCnt));

        wordService.saveBulkDbWord(wordsMap);
    }
}
