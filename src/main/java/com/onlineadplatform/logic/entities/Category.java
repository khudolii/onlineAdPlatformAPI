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
    private Long categoryId;

    @Field(targetType = FieldType.STRING)
    private String categoryName;

    @DBRef()
    private Set<Advertisement> advertisements;
}
