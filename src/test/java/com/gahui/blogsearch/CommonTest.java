package com.gahui.blogsearch;

import org.junit.jupiter.api.Test;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

class CommonTest {

	@Test
	void contextLoads() {
	}

	@Test
	void urlEncoding() {
		String word = "---하이";

		try {
			String result = URLEncoder.encode(word, StandardCharsets.UTF_8);
			System.out.println("word : " + word  + "  encoding :  "+ result);
		} catch (Exception e) {
		}
	}

}
