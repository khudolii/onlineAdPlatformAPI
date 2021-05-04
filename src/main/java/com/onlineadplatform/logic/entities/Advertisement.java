package com.onlineadplatform.logic.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;
import java.util.List;

@Document
@Getter
@Setter
public class Advertisement {

    @MongoId
    @Field(targetType = FieldType.OBJECT_ID)
    @Indexed(unique = true)
    private String advertisementId;

    @Field(targetType = FieldType.STRING)
    @Indexed(unique = true)
    private String title;

    @Field(targetType = FieldType.STRING)
    private String description;

    @Field(targetType = FieldType.DOUBLE)
    private Double price;

    @DBRef
    private Currency currency;

    @DBRef
    private List<Attachment> attachments;

    @DBRef
    private ACLUser aclUser;

    @DBRef
    private Category category;
}
