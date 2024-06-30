package com.bhaambhu.scalevote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bhaambhu.scalevote.entity.Party;
import com.bhaambhu.scalevote.service.PartyService;

import java.util.List;

@RestController
@RequestMapping("/api/parties")
public class PartyController {
    @Autowired
    private PartyService partyService;

    @GetMapping
    public List<Party> getAllParties() {
        return partyService.getAllParties();
    }

    @PostMapping
    public Party createParty(@RequestBody Party party) {
        return partyService.saveParty(party);
    }

}