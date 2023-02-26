package com.sbu.dm.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(fluent = true)
@Document
public class Person {
    @Id
    private ObjectId id;

    @NotNull
    @Positive
    @Indexed(unique = true)
    private Integer ssn;

    @Size(min = 2, max = 1000)
    @Field("fName")
    private String firstName;

    @Indexed
    private String lastName;

    private LocalDate bornDate;

    @Transient
    private Integer accountTotal;

    @Size(max = 10)
    @DBRef(lazy = true)
    List<Account> accounts = new ArrayList<>();

    private Address address;

    private Map<String, String> userData;

    private List<Integer> roles;

    public Person() {
        id = new ObjectId();
    }
}
