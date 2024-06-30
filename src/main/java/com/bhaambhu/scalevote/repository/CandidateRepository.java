package com.bhaambhu.scalevote.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bhaambhu.scalevote.entity.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}