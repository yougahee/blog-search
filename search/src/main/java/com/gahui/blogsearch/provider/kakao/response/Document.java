package com.gahui.blogsearch.provider.kakao.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Document {
    String title;
    String contents;
    String url;
    String blogname;
    String thumbnail;
    String datetime;
}