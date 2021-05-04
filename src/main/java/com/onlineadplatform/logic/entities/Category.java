package com.onlineadplatform.logic.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;

@Document
@Getter
@Setter
public class Category {

    @MongoId
    @Field(targetType = FieldType.OBJECT_ID)
    private String categoryId;

    @Field(targetType = FieldType.STRING)
    @Indexed(unique = true)
    private String categoryName;
}
