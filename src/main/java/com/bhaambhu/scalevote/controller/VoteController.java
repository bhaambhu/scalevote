package com.bhaambhu.scalevote.controller;

import com.bhaambhu.scalevote.entity.Vote;
import com.bhaambhu.scalevote.entity.Candidate;
import com.bhaambhu.scalevote.entity.Constituency;
import com.bhaambhu.scalevote.repository.VoteRepository;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.bhaambhu.scalevote.repository.CandidateRepository;
import com.bhaambhu.scalevote.repository.ConstituencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/votes")
@Tag(name = "Vote", description = "The voting API - for single and bulk voting (per constituency)")
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ConstituencyRepository constituencyRepository;

    // Cast a single vote
    @PostMapping("/cast")
    public String castVote(@RequestParam String voterName, @RequestParam int age, @RequestParam Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        Vote vote = new Vote();
        vote.setVoterName(voterName);
        vote.setAge(age);
        vote.setConstituency(candidate.getConstituency());
        vote.setCandidate(candidate);
        vote.setTimestamp(LocalDateTime.now());

        voteRepository.save(vote);

        return "Vote cast successfully!";
    }

    // Bulk voting
    @PostMapping("/bulk-cast")
    public String bulkCastVotes(@RequestParam Long constituencyId, @RequestParam int numberOfVotes) {
        Constituency constituency = constituencyRepository.findById(constituencyId)
                .orElseThrow(() -> new RuntimeException("Constituency not found"));

        List<Candidate> candidates = candidateRepository.findByConstituency(constituency);
        Random random = new Random();

        List<String> voterNames = generateIndianNames(numberOfVotes);

        List<Vote> votes = IntStream.range(0, numberOfVotes).mapToObj(i -> {
            Vote vote = new Vote();
            vote.setVoterName(voterNames.get(i));
            vote.setAge(18 + random.nextInt(63)); // Random age between 18 and 80
            vote.setConstituency(constituency);
            vote.setCandidate(candidates.get(random.nextInt(candidates.size())));
            vote.setTimestamp(LocalDateTime.now());
            return vote;
        }).collect(Collectors.toList());

        voteRepository.saveAll(votes);

        return numberOfVotes + " votes cast successfully!";
    }

    // Generate random Indian names for voters
    private List<String> generateIndianNames(int count) {
        String[] firstNames = { "Narendra", "Rahul", "Amit", "Sonia", "Arvind", "Mamata", "Uddhav", "Yogi", "Nitish",
                "Shashi" };
        String[] lastNames = { "Modi", "Gandhi", "Shah", "Gandhi", "Kejriwal", "Banerjee", "Thackeray", "Adityanath",
                "Kumar", "Tharoor" };
        Random random = new Random();
        return IntStream.range(0, count).mapToObj(
                i -> firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)])
                .collect(Collectors.toList());
    }
}
