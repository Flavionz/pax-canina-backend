package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dto.DogDto;
import java.util.List;

public interface ChienService {
    List<DogDto> findByProprietaireId(Integer idProprietaire);
    DogDto createDogForProprietaire(DogDto dto, Integer idProprietaire);
    boolean isDogOwnedBy(Integer dogId, Integer idProprietaire);
    DogDto updateDog(Integer dogId, DogDto dto);
    void deleteDog(Integer dogId);
}
