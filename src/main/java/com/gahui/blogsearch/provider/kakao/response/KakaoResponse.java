package com.gahui.blogsearch.provider.kakao.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KakaoResponse<T> {

    private MetaResponse meta;
    private List<T> documents;

    public KakaoResponse() {
        meta = new MetaResponse();
        documents = Collections.emptyList();
    }
}
