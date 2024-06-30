package com.bhaambhu.scalevote.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "constituencies")
public class Constituency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String state;

    public Constituency(String name, String state) {
        this.name = name;
        this.state = state;
    }

    public Constituency() {
    }
}