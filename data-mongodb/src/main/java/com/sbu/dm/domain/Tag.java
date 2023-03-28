package com.sbu.dm.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.ObjectInputFilter;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@Document("tags")
public class Tag {
    @Id
    ObjectId id;

    @Field("create_time")
    private Date createTime;
    @Field("update_time")
    private Date updateTime;
    @Field("name")
    private String name;
    // 标签类型 enum#TAG_TYPE
    @Field("type")
    private String type;
    // 可用状态 enum#COMMON_STATUS
    @Field("status")
    private ObjectInputFilter.Status status;

    public Tag() {
        id = new ObjectId();
    }
}
