package com.bhaambhu.scalevote.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bhaambhu.scalevote.entity.Constituency;

public interface ConstituencyRepository extends JpaRepository<Constituency, Long> {
    // Find constituencies by state
    List<Constituency> findByState(String state);

    // Alternatively, using a custom query:
    // @Query("SELECT c FROM Constituency c WHERE c.state = :state")
    // List<Constituency> findByState(@Param("state") String state);
}