package com.sbu.dj.domain.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Set<Tag> findByNameIn(Collection<String> names);
}