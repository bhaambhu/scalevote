package com.bhaambhu.scalevote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhaambhu.scalevote.entity.Candidate;
import com.bhaambhu.scalevote.entity.Constituency;
import com.bhaambhu.scalevote.entity.Party;
import com.bhaambhu.scalevote.repository.CandidateRepository;
import com.bhaambhu.scalevote.repository.ConstituencyRepository;
import com.bhaambhu.scalevote.repository.PartyRepository;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/dummy")
public class DummyDataController {

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private ConstituencyRepository constituencyRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @PostMapping("/reset-data")
    public String resetDummyData() {
        // Clear existing data from all relevant tables
        candidateRepository.deleteAll();
        constituencyRepository.deleteAll();
        partyRepository.deleteAll();

        // Add dummy data for parties
        List<Party> dummyParties = Arrays.asList(
            new Party("Party A", "Symbol A"),
            new Party("Party B", "Symbol B"),
            new Party("Party C", "Symbol C")
        );
        partyRepository.saveAll(dummyParties);

        // Add dummy data for constituencies
        List<Constituency> dummyConstituencies = Arrays.asList(
            new Constituency("Constituency 1", "State 1"),
            new Constituency("Constituency 2", "State 2"),
            new Constituency("Constituency 3", "State 3")
        );
        constituencyRepository.saveAll(dummyConstituencies);

        // Add dummy data for candidates, each belonging to a different party and constituency
        List<Candidate> dummyCandidates = Arrays.asList(
            new Candidate("Candidate 1", dummyParties.get(0), dummyConstituencies.get(0)),
            new Candidate("Candidate 2", dummyParties.get(1), dummyConstituencies.get(1)),
            new Candidate("Candidate 3", dummyParties.get(2), dummyConstituencies.get(2))
        );
        candidateRepository.saveAll(dummyCandidates);

        return "Dummy data for parties, constituencies, and candidates has been added to the database.";
    }
}