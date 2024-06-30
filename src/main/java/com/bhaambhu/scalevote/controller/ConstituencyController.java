package com.bhaambhu.scalevote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bhaambhu.scalevote.entity.Constituency;
import com.bhaambhu.scalevote.repository.ConstituencyRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/constituencies")
public class ConstituencyController {

    @Autowired
    private ConstituencyRepository constituencyRepository;

    // Get all constituencies
    @GetMapping
    public List<Constituency> getAllConstituencies() {
        return constituencyRepository.findAll();
    }

    // Get constituency by ID
    @GetMapping("/{id}")
    public Optional<Constituency> getConstituencyById(@PathVariable Long id) {
        return constituencyRepository.findById(id);
    }

    // Create a new constituency
    @PostMapping
    public Constituency createConstituency(@RequestBody Constituency constituency) {
        return constituencyRepository.save(constituency);
    }

    // Update a constituency
    @PutMapping("/{id}")
    public Constituency updateConstituency(@PathVariable Long id, @RequestBody Constituency constituencyDetails) {
        Constituency constituency = constituencyRepository.findById(id).orElseThrow(() -> new RuntimeException("Constituency not found"));
        constituency.setName(constituencyDetails.getName());
        constituency.setState(constituencyDetails.getState());
        return constituencyRepository.save(constituency);
    }

    // Delete a constituency
    @DeleteMapping("/{id}")
    public String deleteConstituency(@PathVariable Long id) {
        constituencyRepository.deleteById(id);
        return "Constituency with ID " + id + " deleted successfully!";
    }
}
