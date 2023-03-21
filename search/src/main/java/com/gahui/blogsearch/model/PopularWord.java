package com.gahui.blogsearch.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PopularWord {

    @Id
    private String word;

    private Integer cnt;

    private LocalDateTime createDt;

    private LocalDateTime updateDt;

    @Builder(builderClassName = "insertWord", builderMethodName = "insertWord")
    public PopularWord(@NotNull String word, int cnt) {
        this.word = word;
        this.cnt = cnt;
        this.createDt = LocalDateTime.now();
    }
}
