package com.gahui.blogsearch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PopularWord {

    @Id
    private String word;

    private Integer cnt;

    private LocalDateTime createDt;

    private LocalDateTime updateDt;

    @Builder(builderClassName = "insertWord", builderMethodName = "insertWord")
    public PopularWord(@NotNull String word) {
        this.word = word;
        this.cnt = 1;
        this.createDt = LocalDateTime.now();
    }
}
