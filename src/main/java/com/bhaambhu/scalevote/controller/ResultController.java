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
import org.springframework.http.ResponseEntity;
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
                result.put("constituency", constituency);

                if (!votes.isEmpty()) {
                        Map<Candidate, Long> voteCountsMap = votes.stream()
                                        .collect(Collectors.groupingBy(Vote::getCandidate, Collectors.counting()));
                        List<VoteCountDTO> voteCounts = voteCountsMap.entrySet().stream()
                                        .map(entry -> new VoteCountDTO(entry.getKey().getId(), entry.getKey().getName(),
                                                        entry.getValue(), entry.getKey().getParty()))
                                        .collect(Collectors.toList());
                        // Sort voteCounts by votes in descending order
                        voteCounts.sort((v1, v2) -> Long.compare(v2.getVotes(), v1.getVotes()));

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
                        result.put("voteCounts", new String[0]);
                        result.put("winningCandidate", null);
                        result.put("winningParty", null);
                }

                return result;
        }

        @GetMapping("/state/{state}/{constituency}")
        @Operation(summary = "Fetch result per state")
        public Map<String, Object> getConstituencyResults(@PathVariable String state,
                        @PathVariable String constituency) {
                Constituency constituencyEntity = constituencyRepository.findByStateAndName(state, constituency);
                if (constituencyEntity == null) {
                        throw new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, "Constituency not found.");
                }

                return getConstituencyResults(constituencyEntity.getId());
        }

        // Get results for the entire state
        @GetMapping("/state/{state}")
        @Operation(summary = "Fetch result per state")
        public Map<String, Object> getStateResults(@PathVariable String state) {
                List<Constituency> constituencies = constituencyRepository.findByState(state);
                List<Map<String, Object>> constituencyResults = new ArrayList<>();
                Map<Party, Integer> partySeatsCount = new HashMap<>();
                long totalVotes = 0;

                for (Constituency constituency : constituencies) {
                        Map<String, Object> constituencyResult = getConstituencyResults(constituency.getId());
                        constituencyResults.add(constituencyResult);

                        Map<String, Object> winningPartyMap = (Map<String, Object>) constituencyResult
                                        .get("winningParty");
                        if (winningPartyMap != null) {
                                Party winningParty = new Party();
                                winningParty.setId((Long) winningPartyMap.get("id"));
                                winningParty.setName((String) winningPartyMap.get("name"));
                                winningParty.setSymbol((String) winningPartyMap.get("symbol"));

                                partySeatsCount.put(winningParty, partySeatsCount.getOrDefault(winningParty, 0) + 1);
                        }

                        totalVotes += (Long) constituencyResult.get("totalVotes");
                }

                Party[] winningParty = { null };
                int maxSeats = 0;
                List<Map<String, Object>> partySeatsList = new ArrayList<>();

                for (Map.Entry<Party, Integer> entry : partySeatsCount.entrySet()) {
                        Party party = entry.getKey();
                        int seats = entry.getValue();
                        Map<String, Object> partySeat = new HashMap<>();
                        partySeat.put("party", party);
                        partySeat.put("seats", seats);
                        partySeatsList.add(partySeat);

                        if (seats > maxSeats) {
                                maxSeats = seats;
                                winningParty[0] = party;
                        }
                }

                Map<String, Object> stateResult = new HashMap<>();
                stateResult.put("totalVotes", totalVotes);
                stateResult.put("constituencies", constituencyResults);
                stateResult.put("partySeats", partySeatsList);
                if (winningParty[0] != null) {
                        stateResult.put("winningParty", new HashMap<String, Object>() {
                                {
                                        put("id", winningParty[0].getId());
                                        put("name", winningParty[0].getName());
                                        put("symbol", winningParty[0].getSymbol());
                                }
                        });
                } else {
                        stateResult.put("winningParty", null);
                }

                return stateResult;
        }
}
