package com.flavio.paxcanina.service;

import com.flavio.paxcanina.model.Proprietaire;

import java.util.List;

public interface ProprietaireService {
    Proprietaire register(Proprietaire proprietaire);
    List<Proprietaire> findAll();
    Proprietaire findById(Integer id);
    Proprietaire findByIdWithChiensAndInscriptions(Integer id);
}
