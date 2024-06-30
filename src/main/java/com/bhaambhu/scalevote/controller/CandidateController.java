package com.bhaambhu.scalevote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bhaambhu.scalevote.entity.Candidate;
import com.bhaambhu.scalevote.repository.CandidateRepository;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/candidates")
@Tag(name = "Candidates", description = "At every constituency, every party should have one candidate")
public class CandidateController {

    @Autowired
    private CandidateRepository candidateRepository;

    // Get all candidates
    @GetMapping
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    // Get candidate by ID
    @GetMapping("/{id}")
    public Optional<Candidate> getCandidateById(@PathVariable Long id) {
        return candidateRepository.findById(id);
    }

    // Create a new candidate
    @PostMapping
    public Candidate createCandidate(@RequestBody Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    // Update a candidate
    @PutMapping("/{id}")
    public Candidate updateCandidate(@PathVariable Long id, @RequestBody Candidate candidateDetails) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> new RuntimeException("Candidate not found"));
        candidate.setName(candidateDetails.getName());
        candidate.setParty(candidateDetails.getParty());
        candidate.setConstituency(candidateDetails.getConstituency());
        return candidateRepository.save(candidate);
    }

    // Delete a candidate
    @DeleteMapping("/{id}")
    public String deleteCandidate(@PathVariable Long id) {
        candidateRepository.deleteById(id);
        return "Candidate with ID " + id + " deleted successfully!";
    }
}
