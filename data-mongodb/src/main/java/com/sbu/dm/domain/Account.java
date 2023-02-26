package com.sbu.dm.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Getter
@Setter
@Accessors(fluent = true)
@Document
public class Account {
    @Id
    ObjectId id;
    Float total;
    @DocumentReference
    private List<Tag> tags;

    public Account() {
        id = new ObjectId();
    }
}
