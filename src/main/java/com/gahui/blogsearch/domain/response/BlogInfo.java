package com.gahui.blogsearch.domain.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogInfo {
    String title;
    String contents;
    String url;
    String blogname;
    String thumbnail;
    String datetime;
}
