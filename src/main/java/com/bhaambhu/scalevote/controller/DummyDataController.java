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
import com.bhaambhu.scalevote.repository.VoteRepository;
import com.bhaambhu.scalevote.utils.LogWithTimeStamp;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/dummy")
@Tag(name = "Fill Dummy Data / Reset System", description = "Calling this endpoint will reset the database and fill dummy data for parties, constituencies and candidates.")
public class DummyDataController {

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private ConstituencyRepository constituencyRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private VoteRepository voteRepository;

    @PostMapping("/reset-data")
    public String resetDummyData() {
        LogWithTimeStamp lts = new LogWithTimeStamp("Reset DummyData Endpoint called");
        // Clear existing data from all relevant tables
        voteRepository.deleteAll();
        lts.log("Deleted all votes data");
        candidateRepository.deleteAll();
        lts.log("Deleted all candidates data");
        partyRepository.deleteAll();
        lts.log("Deleted all party data");
        constituencyRepository.deleteAll();
        lts.log("Deleted all constituency data");

        // Add dummy data for parties
        List<Party> dummyParties = Arrays.asList(new Party("Bharatiya Janata Party", "Lotus"),
                new Party("Indian National Congress", "Hand"), new Party("Aam Aadmi Party", "Broom"),
                new Party("Bahujan Samaj Party", "Elephant"), new Party("Samajwadi Party", "Bicycle"));
        partyRepository.saveAll(dummyParties);

        lts.log("Saved dummyParties data");

        // Add dummy data for constituencies
        List<Constituency> dummyConstituencies = Arrays.asList(new Constituency("Ambala", "Haryana"),
                new Constituency("Bhiwani–Mahendragarh", "Haryana"), new Constituency("Faridabad", "Haryana"),
                new Constituency("Gurgaon", "Haryana"), new Constituency("Hisar", "Haryana"),
                new Constituency("Karnal", "Haryana"), new Constituency("Kurukshetra", "Haryana"),
                new Constituency("Rohtak", "Haryana"), new Constituency("Sirsa", "Haryana"),
                new Constituency("Sonipat", "Haryana"));
        constituencyRepository.saveAll(dummyConstituencies);

        lts.log("Saved dummyConstituencies data");

        // Add dummy data for candidates
        List<String> candidateNames = generateIndianPoliticianNames(50);

        List<Candidate> dummyCandidates = Arrays.asList(
                // Ambala
                new Candidate(candidateNames.get(0), dummyParties.get(0), dummyConstituencies.get(0)),
                new Candidate(candidateNames.get(1), dummyParties.get(1), dummyConstituencies.get(0)),
                new Candidate(candidateNames.get(2), dummyParties.get(2), dummyConstituencies.get(0)),
                new Candidate(candidateNames.get(3), dummyParties.get(3), dummyConstituencies.get(0)),
                new Candidate(candidateNames.get(4), dummyParties.get(4), dummyConstituencies.get(0)),
                // Bhiwani–Mahendragarh
                new Candidate(candidateNames.get(5), dummyParties.get(0), dummyConstituencies.get(1)),
                new Candidate(candidateNames.get(6), dummyParties.get(1), dummyConstituencies.get(1)),
                new Candidate(candidateNames.get(7), dummyParties.get(2), dummyConstituencies.get(1)),
                new Candidate(candidateNames.get(8), dummyParties.get(3), dummyConstituencies.get(1)),
                new Candidate(candidateNames.get(9), dummyParties.get(4), dummyConstituencies.get(1)),
                // Faridabad
                new Candidate(candidateNames.get(10), dummyParties.get(0), dummyConstituencies.get(2)),
                new Candidate(candidateNames.get(11), dummyParties.get(1), dummyConstituencies.get(2)),
                new Candidate(candidateNames.get(12), dummyParties.get(2), dummyConstituencies.get(2)),
                new Candidate(candidateNames.get(13), dummyParties.get(3), dummyConstituencies.get(2)),
                new Candidate(candidateNames.get(14), dummyParties.get(4), dummyConstituencies.get(2)),
                // Gurgaon
                new Candidate(candidateNames.get(15), dummyParties.get(0), dummyConstituencies.get(3)),
                new Candidate(candidateNames.get(16), dummyParties.get(1), dummyConstituencies.get(3)),
                new Candidate(candidateNames.get(17), dummyParties.get(2), dummyConstituencies.get(3)),
                new Candidate(candidateNames.get(18), dummyParties.get(3), dummyConstituencies.get(3)),
                new Candidate(candidateNames.get(19), dummyParties.get(4), dummyConstituencies.get(3)),
                // Hisar
                new Candidate(candidateNames.get(20), dummyParties.get(0), dummyConstituencies.get(4)),
                new Candidate(candidateNames.get(21), dummyParties.get(1), dummyConstituencies.get(4)),
                new Candidate(candidateNames.get(22), dummyParties.get(2), dummyConstituencies.get(4)),
                new Candidate(candidateNames.get(23), dummyParties.get(3), dummyConstituencies.get(4)),
                new Candidate(candidateNames.get(24), dummyParties.get(4), dummyConstituencies.get(4)),
                // Karnal
                new Candidate(candidateNames.get(25), dummyParties.get(0), dummyConstituencies.get(5)),
                new Candidate(candidateNames.get(26), dummyParties.get(1), dummyConstituencies.get(5)),
                new Candidate(candidateNames.get(27), dummyParties.get(2), dummyConstituencies.get(5)),
                new Candidate(candidateNames.get(28), dummyParties.get(3), dummyConstituencies.get(5)),
                new Candidate(candidateNames.get(29), dummyParties.get(4), dummyConstituencies.get(5)),
                // Kurukshetra
                new Candidate(candidateNames.get(30), dummyParties.get(0), dummyConstituencies.get(6)),
                new Candidate(candidateNames.get(31), dummyParties.get(1), dummyConstituencies.get(6)),
                new Candidate(candidateNames.get(32), dummyParties.get(2), dummyConstituencies.get(6)),
                new Candidate(candidateNames.get(33), dummyParties.get(3), dummyConstituencies.get(6)),
                new Candidate(candidateNames.get(34), dummyParties.get(4), dummyConstituencies.get(6)),
                // Rohtak
                new Candidate(candidateNames.get(35), dummyParties.get(0), dummyConstituencies.get(7)),
                new Candidate(candidateNames.get(36), dummyParties.get(1), dummyConstituencies.get(7)),
                new Candidate(candidateNames.get(37), dummyParties.get(2), dummyConstituencies.get(7)),
                new Candidate(candidateNames.get(38), dummyParties.get(3), dummyConstituencies.get(7)),
                new Candidate(candidateNames.get(39), dummyParties.get(4), dummyConstituencies.get(7)),
                // Sirsa
                new Candidate(candidateNames.get(40), dummyParties.get(0), dummyConstituencies.get(8)),
                new Candidate(candidateNames.get(41), dummyParties.get(1), dummyConstituencies.get(8)),
                new Candidate(candidateNames.get(42), dummyParties.get(2), dummyConstituencies.get(8)),
                new Candidate(candidateNames.get(43), dummyParties.get(3), dummyConstituencies.get(8)),
                new Candidate(candidateNames.get(44), dummyParties.get(4), dummyConstituencies.get(8)),
                // Sonipat
                new Candidate(candidateNames.get(45), dummyParties.get(0), dummyConstituencies.get(9)),
                new Candidate(candidateNames.get(46), dummyParties.get(1), dummyConstituencies.get(9)),
                new Candidate(candidateNames.get(47), dummyParties.get(2), dummyConstituencies.get(9)),
                new Candidate(candidateNames.get(48), dummyParties.get(3), dummyConstituencies.get(9)),
                new Candidate(candidateNames.get(49), dummyParties.get(4), dummyConstituencies.get(9)));

        candidateRepository.saveAll(dummyCandidates);

        lts.log("Saved dummyCandidates data");

        return "Dummy data for parties, constituencies, and candidates has been added to the database.";
    }

    // Generate random Indian names for voters
    private List<String> generateIndianPoliticianNames(int count) {
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