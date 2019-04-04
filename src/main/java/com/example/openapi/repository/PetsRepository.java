package com.example.openapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.openapi.domain.Pets;

@Repository
public interface PetsRepository extends JpaRepository<Pets, Long> {

	List<Pets> findByName(String name);

}
