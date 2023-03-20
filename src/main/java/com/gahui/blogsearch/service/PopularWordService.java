package com.gahui.blogsearch.service;

import com.gahui.blogsearch.domain.response.PopularWordRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class PopularWordService {
    private final WordDbService wordDbService;
    private final WordService wordService;

    @PostConstruct
    public void init() {
        setDbWordMap();
    }

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
        return getCalPopularWords();
    }

    private List<PopularWordRes> getCalPopularWords() {
        val mergeHashMap = new HashMap<>(wordService.getDbWordMap());
        wordService.getSearchWordMap().forEach((key, value) ->
                mergeHashMap.merge(key, value, (k, v) -> mergeHashMap.get(key) + value));

        val sortingMap = sortMapByCntDesc(mergeHashMap.entrySet());
        return getTop10PopularWords(sortingMap);
    }

    private List<PopularWordRes> getTop10PopularWords(List<Map.Entry<String, Integer>> sortingMap) {
        val result = new ArrayList<PopularWordRes>();

        sortingMap.stream()
                .limit(10)
                .forEach(t -> result.add(getPopularWordRes(t.getKey(), t.getValue())));

        return result;
    }

    private List<Map.Entry<String, Integer>> sortMapByCntDesc(Set<Map.Entry<String, Integer>> entry) {
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
    public synchronized void setInitHashMap() {
        log.info("[Scheduling] 스케줄링 시간 : {} ", LocalDateTime.now());

        wordDbService.bulkUpsertWords(wordService.getSearchWordMap());

        wordService.clearSearchWordMap();
        wordService.clearDbWordMap();

        setDbWordMap();
    }

    private void setDbWordMap() {
        val words = wordDbService.getPopularWordLimit1000();
        words.forEach(word -> wordService.saveOriginDbWordDb(word.getWord(), word.getCnt()));
    }
}
