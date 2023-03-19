package com.gahui.blogsearch.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Page<T> {
    int totalCount;
    int pageableCount;
    boolean isEnd;
    List<T> list;
}
