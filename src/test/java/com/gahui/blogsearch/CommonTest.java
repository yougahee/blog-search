package com.gahui.blogsearch;

import org.junit.jupiter.api.Test;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

class CommonTest {

	@Test
	void contextLoads() {
	}

	@Test
	void urlEncoding() {
		String word = "천재";

		try {
			String result = URLEncoder.encode(word, StandardCharsets.UTF_8);
			System.out.println("word : " + word  + "  encoding :  "+ result);
		} catch (Exception e) {
		}
	}

	@Test
	void urlDecoding() {
		String word = "0xec0xb20x9c0xec0x9e0xac";

		try {
			String result = URLDecoder.decode(word, StandardCharsets.UTF_8);
			System.out.println("decode result = " + result);
		} catch (Exception e) {

		}
	}

}
