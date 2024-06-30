package com.bhaambhu.scalevote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bhaambhu.scalevote.entity.Party;

public interface PartyRepository extends JpaRepository<Party, Long> {
}