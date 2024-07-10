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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
                Optional<Constituency> constituencyOptional = constituencyRepository.findById(constituencyId);
                if (!constituencyOptional.isPresent()) {
                        throw new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, "Constituency not found.");
                }
                Constituency constituency = constituencyOptional.get();
                List<Vote> votes = voteRepository.findByConstituency(constituency);

                Map<String, Object> result = new HashMap<>();
                result.put("constituency", constituency.getName());

                if (!votes.isEmpty()) {
                        Map<Candidate, Long> voteCountsMap = votes.stream()
                                        .collect(Collectors.groupingBy(Vote::getCandidate, Collectors.counting()));
                        List<VoteCountDTO> voteCounts = voteCountsMap.entrySet().stream()
                                        .map(entry -> new VoteCountDTO(entry.getKey().getId(), entry.getKey().getName(),
                                                        entry.getValue()))
                                        .collect(Collectors.toList());
                        Candidate winningCandidate = Collections
                                        .max(voteCountsMap.entrySet(), Map.Entry.comparingByValue())
                                        .getKey();
                        long winningVotes = voteCountsMap.get(winningCandidate);
                        long totalVotes = voteCountsMap.values().stream().mapToLong(Long::longValue).sum();
                        long margin = winningVotes - voteCountsMap.values().stream()
                                        .filter(count -> !count.equals(winningVotes))
                                        .max(Long::compareTo)
                                        .orElse(0L);
                        result.put("margin", margin);
                        result.put("totalVotes", totalVotes);
                        result.put("voteCounts", voteCounts);
                        result.put("winningCandidate", new HashMap<String, Object>() {
                                {
                                        put("id", winningCandidate.getId());
                                        put("name", winningCandidate.getName());
                                        put("votes", winningVotes);
                                }
                        });
                        result.put("winningParty", new HashMap<String, Object>() {
                                {
                                        put("id", winningCandidate.getParty().getId());
                                        put("name", winningCandidate.getParty().getName());
                                        put("symbol", winningCandidate.getParty().getSymbol());
                                }
                        });
                } else {
                        result.put("margin", 0);
                        result.put("totalVotes", 0);
                        result.put("voteCounts", 0);
                        result.put("winningCandidate", null);
                        result.put("winningParty", null);
                }

                return result;
        }

        // Get results for the entire state
        @GetMapping("/state/{state}")
        @Operation(summary = "Fetch result per state")
        public Map<String, Object> getStateResults(@PathVariable String state) {
                List<Constituency> constituencies = constituencyRepository.findByState(state);
                System.out.println("constituencies found: " + constituencies);
                List<Map<String, Object>> constituencyResults = new ArrayList<>();
                Map<Party, Long> partyVotes = new HashMap<>();
                long totalVotes = 0;

                for (Constituency constituency : constituencies) {
                        List<Vote> votes = voteRepository.findByConstituency(constituency);
                        Map<Candidate, Long> voteCountsMap = votes.stream()
                                        .collect(Collectors.groupingBy(Vote::getCandidate, Collectors.counting()));

                        List<VoteCountDTO> voteCounts = voteCountsMap.entrySet().stream()
                                        .map(entry -> new VoteCountDTO(entry.getKey().getId(), entry.getKey().getName(),
                                                        entry.getValue()))
                                        .collect(Collectors.toList());

                        Map<String, Object> result = new HashMap<>();
                        result.put("constituency", constituency.getName());
                        result.put("totalVotes", votes.size());
                        result.put("voteCounts", voteCounts);

                        if (!voteCountsMap.isEmpty()) {
                                Candidate winningCandidate = Collections
                                                .max(voteCountsMap.entrySet(), Map.Entry.comparingByValue()).getKey();
                                long winningVotes = voteCountsMap.get(winningCandidate);
                                long margin = winningVotes - voteCountsMap.values().stream()
                                                .filter(count -> !count.equals(winningVotes))
                                                .max(Long::compareTo)
                                                .orElse(0L);

                                result.put("winningCandidate", new HashMap<String, Object>() {
                                        {
                                                put("id", winningCandidate.getId());
                                                put("name", winningCandidate.getName());
                                                put("votes", winningVotes);
                                        }
                                });
                                result.put("winningParty", new HashMap<String, Object>() {
                                        {
                                                put("id", winningCandidate.getParty().getId());
                                                put("name", winningCandidate.getParty().getName());
                                                put("symbol", winningCandidate.getParty().getSymbol());
                                        }
                                });
                                result.put("margin", margin);

                                partyVotes.put(winningCandidate.getParty(),
                                                partyVotes.getOrDefault(winningCandidate.getParty(), 0L)
                                                                + winningVotes);
                        } else {
                                result.put("winningCandidate", null);
                                result.put("winningParty", null);
                                result.put("margin", 0);
                        }

                        totalVotes += votes.size();
                        constituencyResults.add(result);
                }

                Map<String, Object> stateResult = new HashMap<>();
                stateResult.put("totalVotes", totalVotes);
                stateResult.put("constituencies", constituencyResults);
                if (!partyVotes.isEmpty()) {
                        Party winningParty = Collections.max(partyVotes.entrySet(), Map.Entry.comparingByValue())
                                        .getKey();
                        long winningPartyVotes = partyVotes.get(winningParty);
                        stateResult.put("winningParty", new HashMap<String, Object>() {
                                {
                                        put("id", winningParty.getId());
                                        put("name", winningParty.getName());
                                        put("symbol", winningParty.getSymbol());
                                        put("votes", winningPartyVotes);
                                }
                        });
                } else {
                        stateResult.put("winningParty", null);
                }

                return stateResult;
        }
}
