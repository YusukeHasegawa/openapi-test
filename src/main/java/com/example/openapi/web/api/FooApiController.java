package com.example.openapi.web.api;

import com.example.openapi.domain.Pets;
import com.example.openapi.service.PetsService;
import com.example.openapi.web.model.NewPet;
import com.example.openapi.web.model.Pet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
public class FooApiController implements FooApi {

    private final PetsService petsService;

    public FooApiController(final PetsService petsService) {
        this.petsService = petsService;
    }

    @Override
    public ResponseEntity<Void> delete(@Min(1L) Long id) {
        petsService.deletePets(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Pet> get(@Min(1L) final Long id) {
        return ResponseEntity.ok(petsService.getPet(id));
    }

    @Override
    public ResponseEntity<List<Pet>> list(@NotNull @Min(1) @Max(500) @Valid final Integer limit) {
        return ResponseEntity.ok(petsService.getPets(limit));
    }

    @Override
    public ResponseEntity<Void> post(@Valid final NewPet newPet) {
        final Pets pets = petsService.saveNewPet(newPet);
        final URI uri = MvcUriComponentsBuilder.fromController(FooApiController.class)
                .path("/foo/{id}").buildAndExpand(pets.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
