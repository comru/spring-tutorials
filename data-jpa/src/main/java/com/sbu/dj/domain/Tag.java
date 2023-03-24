package com.sbu.dj.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
@Entity
@Table(name = "tag")
public class Tag extends BaseEntity {

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

}