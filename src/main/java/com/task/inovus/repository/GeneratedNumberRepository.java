package com.task.inovus.repository;

import com.task.inovus.domain.model.GeneratedNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeneratedNumberRepository extends JpaRepository<GeneratedNumber, Long> {
    @Query("select count(n) > 0 from GeneratedNumber n " +
            "where n.firstChar = :firstChar " +
            "      and n.number = :number " +
            "      and n.secondChar = :secondChar " +
            "      and n.thirdChar = :thirdChar ")
    boolean existsByParams(@Param("firstChar") Character firstChar,
                           @Param("number") Integer number,
                           @Param("secondChar") Character secondChar,
                           @Param("thirdChar") Character thirdChar);

    @Query(value = "" +
            "select n.* from generated_number n " +
            "order by n.created_at desc " +
            "limit 1 ",
            nativeQuery = true)
    Optional<GeneratedNumber> findLast();
}