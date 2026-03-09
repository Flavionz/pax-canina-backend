package com.flavio.paxcanina.dto;

import java.time.LocalDate;

public record DogAdminListDto(
        Integer idDog,
        String name,
        LocalDate birthDate,
        String sex,
        String breedName,
        String ownerName
) {}
