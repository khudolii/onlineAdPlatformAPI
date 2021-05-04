package com.onlineadplatform.logic.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.Set;

@Document
@Getter
@Setter
public class Category {

    @MongoId
    @Field(targetType = FieldType.OBJECT_ID)
    private String categoryId;

    @Field(targetType = FieldType.STRING)
    private String categoryName;

    @DBRef(lazy = true)
    private Set<Advertisement> advertisements;
}
