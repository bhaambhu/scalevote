package com.bhaambhu.scalevote.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "parties")
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String symbol;

    public Party(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public Party() {
    }

}

