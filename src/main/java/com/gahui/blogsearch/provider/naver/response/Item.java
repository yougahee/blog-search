package com.gahui.blogsearch.provider.naver.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    String title;
    String link;
    String desciption;
    String bloggername;
    String bloggerlink;
    String postdate;
}
