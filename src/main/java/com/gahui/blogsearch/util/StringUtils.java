package com.gahui.blogsearch.util;

import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class StringUtils {

    public static String encodingWord(String word) {
        String result = word;

        try {
            result = URLEncoder.encode(word, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }
}
