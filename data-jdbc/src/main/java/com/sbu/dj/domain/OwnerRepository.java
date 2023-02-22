package com.sbu.dj.domain;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface OwnerRepository extends ListCrudRepository<Owner, Long> {
    Owner findByName(String name);
}
