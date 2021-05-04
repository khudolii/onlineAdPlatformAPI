package com.onlineadplatform.logic.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;

import javax.persistence.Id;
import javax.persistence.Index;
import java.util.List;
import java.util.Set;

@Document
@Getter
@Setter
public class Advertisement {

    @MongoId
    @Field(targetType = FieldType.OBJECT_ID)
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
