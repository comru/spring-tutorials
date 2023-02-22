package com.sbu.dj.domain;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface DogRepository extends ListCrudRepository<Dog, Long> {

    Dog findByName(String name);

    List<Dog> findAllByOwnerId(Long ownerId);
}
