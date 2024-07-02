package com.bhaambhu.scalevote.controller;

import com.bhaambhu.scalevote.entity.Party;
import com.bhaambhu.scalevote.repository.PartyRepository;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parties")
@Tag(name = "Political Parties")
public class PartyController {

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private Environment env;

    // Get all parties
    @GetMapping
    public List<Party> getAllParties() {
        String datasourceURL = env.getProperty("spring.datasource.url");
        System.out.println(datasourceURL);
        return partyRepository.findAll();
    }

    // Get party by ID
    @GetMapping("/{id}")
    public Optional<Party> getPartyById(@PathVariable Long id) {
        return partyRepository.findById(id);
    }

    // Create a new party
    @PostMapping
    public Party createParty(@RequestBody Party party) {
        return partyRepository.save(party);
    }

    // Update a party
    @PutMapping("/{id}")
    public Party updateParty(@PathVariable Long id, @RequestBody Party partyDetails) {
        Party party = partyRepository.findById(id).orElseThrow(() -> new RuntimeException("Party not found"));
        party.setName(partyDetails.getName());
        party.setSymbol(partyDetails.getSymbol());
        return partyRepository.save(party);
    }

    // Delete a party
    @DeleteMapping("/{id}")
    public String deleteParty(@PathVariable Long id) {
        partyRepository.deleteById(id);
        return "Party with ID " + id + " deleted successfully!";
    }
}
