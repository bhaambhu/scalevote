package com.bhaambhu.scalevote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bhaambhu.scalevote.entity.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}