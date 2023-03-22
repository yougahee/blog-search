package util;

import org.junit.jupiter.api.Test;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

class StringUtilsTest {

    @Test
    void urlEncoding() {
        String word = "천재";

        String encodeWord = URLEncoder.encode(word, StandardCharsets.UTF_8);
        String code = StringUtils.encodingWord(word);

        String decodeWord = StringUtils.decodeWord(encodeWord);

        assert word.equals(decodeWord);
        assert encodeWord.equals(code);
        assert !word.equals(encodeWord);
    }

}