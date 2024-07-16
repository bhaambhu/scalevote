package com.bhaambhu.scalevote.controller;

import com.bhaambhu.scalevote.dto.VoteCountDTO;
import com.bhaambhu.scalevote.entity.Candidate;
import com.bhaambhu.scalevote.entity.Constituency;
import com.bhaambhu.scalevote.entity.Party;
import com.bhaambhu.scalevote.entity.Vote;
import com.bhaambhu.scalevote.repository.CandidateRepository;
import com.bhaambhu.scalevote.repository.ConstituencyRepository;
import com.bhaambhu.scalevote.repository.VoteRepository;
import com.bhaambhu.scalevote.utils.LogWithTimeStamp;
import com.bhaambhu.scalevote.utils.Utils;

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
        LogWithTimeStamp tsl = new LogWithTimeStamp("Fetching results for constituency " + constituencyId,
                1);
        Long ts = System.currentTimeMillis();
        // Get the constituency from constituencyId
        Optional<Constituency> constituencyOptional = constituencyRepository.findById(constituencyId);
        if (!constituencyOptional.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Constituency not found.");
        }
        Constituency constituency = constituencyOptional.get();

        tsl.log("Got constituency " + constituency.getName(), 1);

        // Get votes of this constituency
        // List<Vote> votes = voteRepository.findByConstituency(constituency);
        List<Vote> votes = voteRepository.findByConstituencyId(constituencyId);

        // Create a new map which will be the output result of this API endpoint
        Map<String, Object> result = new HashMap<>();
        result.put("constituency", constituency);

        tsl.log("Got " + votes.size() + " votes", 1);
        // If votes of this constituency are not zero
        if (!votes.isEmpty()) {

            System.out.println("\tFetching votecounts by candidates");
            // Generate a vote count map of type <candidate, vote_count>
            Map<Candidate, Long> voteCountsMap = votes.stream()
                    .collect(Collectors.groupingBy(Vote::getCandidate, Collectors.counting()));

            tsl.log("Got voteCountsMap", 1);

            // Convert this vote-counts data into an easy to read VoteCountDTO structure
            List<VoteCountDTO> voteCounts = voteCountsMap.entrySet().stream()
                    .map(entry -> new VoteCountDTO(entry.getKey(),
                            entry.getValue()))
                    .collect(Collectors.toList());

            tsl.log("Created votecountsDTO", 1);

            // Sort voteCounts by votes in descending order
            voteCounts.sort((v1, v2) -> Long.compare(v2.getVotes(), v1.getVotes()));

            // Get winning candidate by getting the most value from voteCountsMap and then
            // getting its key

            // Candidate winningCandidate = Collections
            // .max(voteCountsMap.entrySet(), Map.Entry.comparingByValue())
            // .getKey();

            tsl.log("Sorted votecounts", 1);

            Candidate winningCandidate = voteCounts.get(0).getCandidate();

            long winningVotes = voteCounts.get(0).getVotes();
            long totalVotes = voteCountsMap.values().stream().mapToLong(Long::longValue).sum();
            long margin = winningVotes;
            if (voteCounts.size() >= 2) {
                margin = winningVotes - voteCounts.get(1).getVotes();
            }
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
            result.put("winningParty", winningCandidate.getParty());
        } else {
            result.put("margin", 0L);
            result.put("totalVotes", 0L);
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
        LogWithTimeStamp tsl = new LogWithTimeStamp("Fetching results for " + state);
        List<Constituency> constituencies = constituencyRepository.findByState(state);
        List<Map<String, Object>> constituencyResults = new ArrayList<>();
        Map<Party, Integer> partySeatsCount = new HashMap<>();
        long totalVotes = 0;
        tsl.log("Got all constituencies");
        for (Constituency constituency : constituencies) {
            Map<String, Object> constituencyResult = getConstituencyResults(constituency.getId());
            constituencyResults.add(constituencyResult);

            Party winningParty = (Party) constituencyResult.get("winningParty");

            if (winningParty != null) {
                // Debug code
                Constituency cur = (Constituency) constituencyResult.get("constituency");
                System.out.println("\t" + cur.getName() + ": " + winningParty.getName() + " Setting count to "
                        + ((partySeatsCount.getOrDefault(winningParty, 0) + 1)));

                partySeatsCount.put(winningParty, partySeatsCount.getOrDefault(winningParty, 0) + 1);
            }

            totalVotes += (Long) constituencyResult.get("totalVotes");
            tsl.log("Done with constituency " + constituency.getName());
        }
        tsl.log("Looped through all constituencies");

        Party[] winningParty = new Party[1];
        int[] maxSeats = { 0 };
        List<Map<String, Object>> partySeatsList = new ArrayList<>();

        for (Map.Entry<Party, Integer> entry : partySeatsCount.entrySet()) {
            Party party = entry.getKey();
            int seats = entry.getValue();
            Map<String, Object> partySeat = new HashMap<>();
            partySeat.put("party", party);
            partySeat.put("seats", seats);
            partySeatsList.add(partySeat);

            if (seats > maxSeats[0]) {
                maxSeats[0] = seats;
                winningParty[0] = party;
            }
        }
        tsl.log("Counted party seats");

        // Sort partySeatsList by seats in descending order
        partySeatsList.sort((p1, p2) -> Integer.compare((Integer) p2.get("seats"), (Integer) p1.get("seats")));

        tsl.log("Sorted party seats");

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
        tsl.log("Done with state result");
        return stateResult;
    }
}
