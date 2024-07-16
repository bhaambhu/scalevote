package com.bhaambhu.scalevote.dto;

import com.bhaambhu.scalevote.entity.Candidate;

public class VoteCountDTO {
    private Candidate candidate;
    private Long votes;
    
    public VoteCountDTO(Candidate candidate, Long votes) {
        this.candidate = candidate;
        this.votes = votes;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Long getVotes() {
        return votes;
    }

    public void setVotes(Long votes) {
        this.votes = votes;
    }
}
