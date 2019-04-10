package com.example.openapi.service;

import com.example.openapi.domain.Pets;
import com.example.openapi.repository.PetsRepository;
import com.example.openapi.web.mapper.PetMapper;
import com.example.openapi.web.model.NewPet;
import com.example.openapi.web.model.Pet;
import org.springframework.stereotype.Service;

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
        Pets pets = petsRepository.findById(id).orElse(null);
        return petMapper.petsToPet(pets);
    }
}
