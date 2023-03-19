package com.gahui.blogsearch.domain.request;

import com.gahui.blogsearch.domain.enums.SortType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchQuery {
    String query;
    SortType sort;
    int page;
    int size;
}
