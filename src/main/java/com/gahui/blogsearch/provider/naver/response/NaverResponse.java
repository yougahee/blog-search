package com.gahui.blogsearch.provider.naver.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NaverResponse<T> {
    int total;
    int start;
    int display;
    List<T> items;
    LocalDateTime lastBuildDate;

    public NaverResponse() {
        this.items = Collections.emptyList();
    }

}
