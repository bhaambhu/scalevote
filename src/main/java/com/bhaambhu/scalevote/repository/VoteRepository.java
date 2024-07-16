package com.bhaambhu.scalevote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bhaambhu.scalevote.entity.Constituency;
import com.bhaambhu.scalevote.entity.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
     // Find votes by Constituency
    List<Vote> findByConstituency(Constituency constituency);

    // Find votes by Constituency name
    @Query("SELECT v FROM Vote v WHERE v.constituency.id = :id")
    List<Vote> findByConstituencyId(@Param("id") Long id);

    // Find votes by Constituency name
    @Query("SELECT v FROM Vote v WHERE v.constituency.name = :name")
    List<Vote> findByConstituencyName(@Param("name") String name);

    // Find votes by Constituency state
    @Query("SELECT v FROM Vote v WHERE v.constituency.state = :state")
    List<Vote> findByState(@Param("state") String state);
}