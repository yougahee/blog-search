package com.gahui.blogsearch.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class WordService {
    private final HashMap<String, Integer> dbWordMap = new HashMap<>();
    private final HashMap<String, Integer> searchWordMap = new HashMap<>();

    public HashMap<String, Integer> getDbWordMap() {
        return dbWordMap;
    }

    public HashMap<String, Integer> getSearchWordMap() {
        return searchWordMap;
    }

    public synchronized void incSearchCnt(String word) {
        searchWordMap.merge(word, 1, (k, v) -> searchWordMap.get(word) + 1);
    }

    public void saveBulkDbWord(Map<String, Integer> map) {
        getDbWordMap().putAll(map);
    }

    public void clearSearchWordMap() {
        getSearchWordMap().clear();
    }

    public void clearDbWordMap() {
        getDbWordMap().clear();
    }

}
