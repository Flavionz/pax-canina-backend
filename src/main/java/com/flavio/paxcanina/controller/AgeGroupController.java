package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.AgeGroupDto;
import com.flavio.paxcanina.service.AgeGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/age-groups")
@CrossOrigin(origins = "http://localhost:4200")
public class AgeGroupController {

    private final AgeGroupService ageGroupService;

    public AgeGroupController(AgeGroupService ageGroupService) {
        this.ageGroupService = ageGroupService;
    }

    @GetMapping
    public List<AgeGroupDto> listAll() {
        return ageGroupService.findAll();
    }

    @GetMapping("/{id}")
    public AgeGroupDto getById(@PathVariable Integer id) {
        return ageGroupService.findById(id);
    }

    @GetMapping("/for-age/{months}")
    public AgeGroupDto getForAge(@PathVariable int months) {
        return ageGroupService.findForAgeInMonths(months);
    }
}
