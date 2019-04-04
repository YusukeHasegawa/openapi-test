package com.example.openapi.web.mapper;

import com.example.openapi.domain.Pets;
import com.example.openapi.web.model.NewPet;
import com.example.openapi.web.model.Pet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetMapper {
    Pet petsToPet(Pets pets);

    Pets petToPets(Pet pet);

    Pets newPetToPets(NewPet pet);
}
