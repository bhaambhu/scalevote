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
                new Party("Bharatiya Janata Party", "Lotus"),
                new Party("Indian National Congress", "Hand"),
                new Party("Aam Aadmi Party", "Broom"),
                new Party("Bahujan Samaj Party", "Elephant"),
                new Party("Samajwadi Party", "Bicycle"));
        partyRepository.saveAll(dummyParties);

        // Add dummy data for constituencies
        List<Constituency> dummyConstituencies = Arrays.asList(
                new Constituency("Ambala", "Haryana"),
                new Constituency("Bhiwani–Mahendragarh", "Haryana"),
                new Constituency("Faridabad", "Haryana"),
                new Constituency("Gurgaon", "Haryana"),
                new Constituency("Hisar", "Haryana"),
                new Constituency("Karnal", "Haryana"),
                new Constituency("Kurukshetra", "Haryana"),
                new Constituency("Rohtak", "Haryana"),
                new Constituency("Sirsa", "Haryana"),
                new Constituency("Sonipat", "Haryana"));
        constituencyRepository.saveAll(dummyConstituencies);

        // Add dummy data for candidates
        List<String> candidateNames = Arrays.asList(
                "Rahul Sharma", "Priya Singh", "Anil Yadav", "Geeta Verma", "Vikas Choudhary",
                "Amit Patil", "Sunita Rao", "Karan Mehta", "Neha Thakur", "Ravi Kumar",
                "Nikhil Gupta", "Sonia Chauhan", "Arjun Malhotra", "Divya Patel", "Rohan Jain",
                "Sandeep Reddy", "Manju Pandey", "Tarun Agarwal", "Rashmi Kaur", "Aakash Bhatt",
                "Vijay Das", "Anjali Prasad", "Kunal Vohra", "Pooja Mittal", "Rohit Deshmukh",
                "Ashok Bansal", "Smita Kulkarni", "Siddharth Naidu", "Shalini Kapoor", "Ritesh Bhalla",
                "Arvind Sharma", "Deepa Nair", "Gaurav Sethi", "Suman Khurana", "Anand Kumar",
                "Mohan Singh", "Sushma Rathi", "Naveen Rawat", "Nisha Joshi", "Harish Tyagi",
                "Yashwant Saxena", "Meena Rao", "Ashish Goel", "Kirti Sharma", "Sanjay Dubey",
                "Kavita Rana", "Sameer Khan", "Lakshmi Nair", "Vinay Mehta", "Anita Saxena");

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

        return "Dummy data for parties, constituencies, and candidates has been added to the database.";
    }
}