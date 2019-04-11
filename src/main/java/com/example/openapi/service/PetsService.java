package com.example.openapi.service;

import com.example.openapi.domain.Pets;
import com.example.openapi.repository.PetsRepository;
import com.example.openapi.web.mapper.PetMapper;
import com.example.openapi.web.model.NewPet;
import com.example.openapi.web.model.Pet;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetsService {
    private final PetMapper petMapper;
    private final PetsRepository petsRepository;

    public PetsService(final PetMapper petMapper, final PetsRepository petsRepository) {
        this.petMapper = petMapper;
        this.petsRepository = petsRepository;
    }

    public Pets saveNewPet(final NewPet newPet) {
        return petsRepository.save(petMapper.newPetToPets(newPet));
    }

    public Pet getPet(final Long id) {
        return petsRepository.findById(id).map(petMapper::petsToPet).orElse(null);
    }

    public List<Pet> getPets(final Integer limit) {
        final Pageable pageable = PageRequest.of(0, limit);
        return Optional.ofNullable(petsRepository.findAll(pageable).stream().map(petMapper::petsToPet).collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }
}
