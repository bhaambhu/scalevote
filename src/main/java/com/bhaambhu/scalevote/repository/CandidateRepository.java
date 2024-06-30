package com.bhaambhu.scalevote.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bhaambhu.scalevote.entity.Candidate;
import com.bhaambhu.scalevote.entity.Constituency;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    // Find votes by Constituency
    List<Candidate> findByConstituency(Constituency constituency);

}