package com.bhaambhu.scalevote.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bhaambhu.scalevote.entity.Constituency;

public interface ConstituencyRepository extends JpaRepository<Constituency, Long> {
}