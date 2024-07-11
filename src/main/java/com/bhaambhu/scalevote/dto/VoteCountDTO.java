package com.bhaambhu.scalevote.dto;

import com.bhaambhu.scalevote.entity.Party;

public class VoteCountDTO {
    private Long id;
    private String name;
    private Long votes;
    private Party party;

    public VoteCountDTO(Long id, String name, Long votes, Party party) {
        this.id = id;
        this.name = name;
        this.votes = votes;
        this.party = party;
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

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

}
