package com.example.openapi.web.api;

import com.example.openapi.domain.Pets;
import com.example.openapi.repository.PetsRepository;
import com.example.openapi.service.PetsService;
import com.example.openapi.web.model.NewPet;
import com.example.openapi.web.model.Pet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;

@RestController
public class FooApiController implements FooApi {

    private final PetsRepository petsRepository;
    private final PetsService petsService;

    public FooApiController(final PetsRepository petsRepository, final PetsService petsService) {
        this.petsRepository = petsRepository;
        this.petsService = petsService;
    }

    @Override
    public ResponseEntity<Pet> get(@Min(1L) final Long id) {
        return ResponseEntity.ok(petsService.getPet(id));
    }

    @Override
    public ResponseEntity<Void> post(@Valid final NewPet newPet) {
        final Pets pets = petsService.saveNewPet(newPet);
        final URI uri = MvcUriComponentsBuilder.fromController(FooApiController.class)
                .path("/foo/{id}").buildAndExpand(pets.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
