package com.gahui.blogsearch.repository;

import com.gahui.blogsearch.model.PopularWord;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PopularWordRepository extends JpaRepository<PopularWord, Long> {

    Optional<PopularWord> findPopularWordsByWord(String word);

    Optional<List<PopularWord>> findByWordIn(List<String> words);

    Optional<List<PopularWord>> findTop1000By(Sort sort);

    @Modifying
    @Transactional
    @Query("UPDATE PopularWord p SET p.cnt = p.cnt + :cnt , p.updateDt = current_time where p.word = :word")
    void updateWordCnt(String word, int cnt);

}
