package com.bhaambhu.scalevote.dto;

public class VoteCountDTO {
    private Long id;
    private String name;
    private Long votes;

    public VoteCountDTO(Long id, String name, Long votes) {
        this.id = id;
        this.name = name;
        this.votes = votes;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVotes() {
        return votes;
    }

    public void setVotes(Long votes) {
        this.votes = votes;
    }
}
