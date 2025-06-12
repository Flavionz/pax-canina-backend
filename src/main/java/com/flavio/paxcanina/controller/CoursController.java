package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dao.CoursDao;
import com.flavio.paxcanina.dto.CoursDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cours")
public class CoursController {

    @Autowired
    private CoursDao coursDao;

    @GetMapping
    public List<CoursDto> getAllCours() {
        return coursDao.findAll().stream()
                .map(cours -> new CoursDto(
                        cours.getIdCours(),
                        cours.getNom(),
                        cours.getDescription(),
                        cours.getStatut(),
                        cours.getImgUrl()
                ))
                .collect(Collectors.toList());
    }
}
