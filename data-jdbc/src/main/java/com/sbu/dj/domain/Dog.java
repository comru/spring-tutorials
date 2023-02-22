package com.sbu.dj.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@RequiredArgsConstructor
@Table("DOG")
public class Dog {
    @Id
    private Long dogId;
    private final String name;
    private final Long ownerId;
}
