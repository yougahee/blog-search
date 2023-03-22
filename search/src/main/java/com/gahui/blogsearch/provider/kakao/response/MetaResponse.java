package com.gahui.blogsearch.provider.kakao.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetaResponse {
    int totalCount;
    int pageableCount;
    Boolean isEnd;
}
