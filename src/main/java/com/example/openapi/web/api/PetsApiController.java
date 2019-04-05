package com.example.openapi.web.api;

import com.example.openapi.domain.Pets;
import com.example.openapi.repository.PetsRepository;
import com.example.openapi.web.mapper.PetMapper;
import com.example.openapi.web.model.NewPet;
import com.example.openapi.web.model.Pet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@CrossOrigin
public class PetsApiController implements PetsApi {

    PetsRepository petsRepository;

    PetMapper petMapper;

    AtomicInteger i = new AtomicInteger();

    @Override
    public ResponseEntity<Void> createPets2(@Valid @RequestBody final NewPet newPet) {
        final Pets pet = petsRepository.save(petMapper.newPetToPets(newPet));
        return ResponseEntity
                .created(MvcUriComponentsBuilder.fromMethodName(PetsApiController.class,
                        "showPetById", pet.getId().toString()).build().toUri())
                .build();
    }

    @Override
    public ResponseEntity<Void> createPets() {
        final Pets entity = new Pets();
        entity.setName("taro " + i.incrementAndGet());
        System.out.println(petsRepository.save(entity));
        return ResponseEntity
                .created(MvcUriComponentsBuilder.fromMethodName(PetsApiController.class,
                        "showPetById", entity.getId().toString()).build().toUri())
                .build();
    }

    @Override
    public ResponseEntity<List<Pet>> listPets(
            @Valid @RequestParam(value = "limit", required = false) final Integer limit) {
        return ResponseEntity.ok(petsRepository.findAll().stream().map(petMapper::petsToPet).collect(Collectors.toList()));

    }

    @Override
    public ResponseEntity<List<Pet>> showPetById(
            @PathVariable("petId") final String petId) {
        final List<Pets> pets = petsRepository.findByName(petId);

        if(!pets.isEmpty()){
            return ResponseEntity.ok(pets.stream().map(petMapper::petsToPet).collect(Collectors.toList()));
        }else{
            throw Problem.valueOf(Status.NOT_FOUND);
        }
    }

}
