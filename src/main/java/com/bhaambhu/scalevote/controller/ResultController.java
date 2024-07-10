package com.bhaambhu.scalevote.controller;

import com.bhaambhu.scalevote.dto.VoteCountDTO;
import com.bhaambhu.scalevote.entity.Candidate;
import com.bhaambhu.scalevote.entity.Constituency;
import com.bhaambhu.scalevote.entity.Party;
import com.bhaambhu.scalevote.entity.Vote;
import com.bhaambhu.scalevote.repository.CandidateRepository;
import com.bhaambhu.scalevote.repository.ConstituencyRepository;
import com.bhaambhu.scalevote.repository.VoteRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/results")
@Tag(name = "Election Results")
public class ResultController {

        @Autowired
        private VoteRepository voteRepository;

        @Autowired
        private ConstituencyRepository constituencyRepository;

        // Get results for a specific constituency
        @GetMapping("/constituency/{constituencyId}")
        public Map<String, Object> getConstituencyResults(@PathVariable Long constituencyId) {
                Constituency constituency = constituencyRepository.findById(constituencyId)
                                .orElseThrow(() -> new RuntimeException("Constituency not found"));

                List<Vote> votes = voteRepository.findByConstituency(constituency);
                Map<Candidate, Long> voteCountsMap = votes.stream()
                                .collect(Collectors.groupingBy(Vote::getCandidate, Collectors.counting()));

                List<VoteCountDTO> voteCounts = voteCountsMap.entrySet().stream()
                                .map(entry -> new VoteCountDTO(entry.getKey().getId(), entry.getKey().getName(),
                                                entry.getValue()))
                                .collect(Collectors.toList());

                Candidate winningCandidate = Collections.max(voteCountsMap.entrySet(), Map.Entry.comparingByValue())
                                .getKey();
                long winningVotes = voteCountsMap.get(winningCandidate);
                long totalVotes = voteCountsMap.values().stream().mapToLong(Long::longValue).sum();
                long margin = winningVotes - voteCountsMap.values().stream()
                                .filter(count -> !count.equals(winningVotes))
                                .max(Long::compareTo)
                                .orElse(0L);

                Map<String, Object> result = new HashMap<>();
                result.put("constituency", constituency.getName());
                result.put("winningParty", winningCandidate.getParty().getName());
                result.put("winningCandidate", winningCandidate.getName());
                result.put("winningVotes", winningVotes);
                result.put("margin", margin);
                result.put("totalVotes", totalVotes);
                result.put("voteCounts", voteCounts);

                return result;
        }

        // Get results for the entire state
        @GetMapping("/state/{state}")
        @Operation(summary = "Fetch result per state")
        public List<Map<String, Object>> getStateResults(@PathVariable String state) {
                List<Constituency> constituencies = constituencyRepository.findByState(state);

                return constituencies.stream().map(constituency -> {
                        List<Vote> votes = voteRepository.findByConstituency(constituency);
                        Map<Candidate, Long> voteCounts = votes.stream()
                                        .collect(Collectors.groupingBy(Vote::getCandidate, Collectors.counting()));

                        Candidate winningCandidate = Collections
                                        .max(voteCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
                        Candidate runnerUpCandidate = voteCounts.entrySet().stream()
                                        .filter(entry -> !entry.getKey().equals(winningCandidate))
                                        .max(Map.Entry.comparingByValue())
                                        .map(Map.Entry::getKey)
                                        .orElse(null);

                        long winningVotes = voteCounts.get(winningCandidate);
                        long runnerUpVotes = runnerUpCandidate != null ? voteCounts.get(runnerUpCandidate) : 0;
                        long margin = winningVotes - runnerUpVotes;

                        Map<String, Object> constituencyResult = new HashMap<>();
                        constituencyResult.put("constituency", constituency.getName());
                        constituencyResult.put("winningParty", winningCandidate.getParty().getName());
                        constituencyResult.put("winningCandidate", winningCandidate.getName());
                        constituencyResult.put("winningVotes", winningVotes);
                        constituencyResult.put("runnerUpCandidate",
                                        runnerUpCandidate != null ? runnerUpCandidate.getName() : "None");
                        constituencyResult.put("runnerUpVotes", runnerUpVotes);
                        constituencyResult.put("margin", margin);
                        constituencyResult.put("voteCounts", voteCounts);

                        return constituencyResult;
                }).collect(Collectors.toList());
        }
}
