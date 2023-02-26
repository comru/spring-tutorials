package com.sbu.dj.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import static org.springframework.data.relational.core.mapping.Embedded.OnEmpty.USE_NULL;

@Getter
@Setter
@RequiredArgsConstructor
@Table("OWNER")
public class Owner {
    @Id
    private Long ownerId;

    @Column("OWNER_NAME")
    private final String name;

    @Embedded(onEmpty = USE_NULL)
    private final Address address;
}
