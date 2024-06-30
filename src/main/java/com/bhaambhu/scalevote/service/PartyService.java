package com.bhaambhu.scalevote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhaambhu.scalevote.entity.Party;
import com.bhaambhu.scalevote.repository.PartyRepository;

import java.util.List;

@Service
public class PartyService {
    @Autowired
    private PartyRepository partyRepository;

    public List<Party> getAllParties() {
        return partyRepository.findAll();
    }

    public Party saveParty(Party party) {
        return partyRepository.save(party);
    }

}