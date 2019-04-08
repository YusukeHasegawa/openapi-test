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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.util.List;
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

    //https://github.com/spring-projects/spring-framework/issues/15682
    //5.1でsuper classのアノテーション見てくれるようになった模様　@RequestBody はなくてもよい
    @Override
    public ResponseEntity<Void> createPets2(@Valid final NewPet newPet) {
        final Pets pet = petsRepository.save(petMapper.newPetToPets(newPet));
        return ResponseEntity
                .created(MvcUriComponentsBuilder.fromController(PetsApiController.class).path("/pets/{petId}").buildAndExpand(pet.getName()).toUri()).build();
    }

    @Override
    public ResponseEntity<List<Pet>> listPets(@Max(100) @Valid final Integer limit) {
        return ResponseEntity.ok(petsRepository.findAll().stream().map(petMapper::petsToPet).collect(Collectors.toList()));
    }


    @Override
    public ResponseEntity<Void> createPets() {
        final Pets entity = new Pets();
        entity.setName("taro " + i.incrementAndGet());
        petsRepository.save(entity);
        return ResponseEntity
                .created(
                        // super classのPathVariableを認識しない
//                        MvcUriComponentsBuilder.fromMethodName(PetsApiController.class,
//                        "showPetById", entity.getName()).build().toUri()
                        MvcUriComponentsBuilder.fromController(PetsApiController.class).path("/pets/{petId}").buildAndExpand(entity.getName()).toUri()
                ).build();
    }


    @Override
    public ResponseEntity<List<Pet>> showPetById(final String petId) {
        final List<Pets> pets = petsRepository.findByName(petId);

        if (!pets.isEmpty()) {
            return ResponseEntity.ok(pets.stream().map(petMapper::petsToPet).collect(Collectors.toList()));
        } else {
            throw Problem.valueOf(Status.NOT_FOUND);
        }
    }

}