package com.onlineadplatform.logic.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Getter
@Setter
public class Currency {
    @MongoId
    @Field(targetType = FieldType.OBJECT_ID)
    private String currencyId;

    @Field(targetType = FieldType.STRING)
    @Indexed(unique = true)
    private String currencyName;

    @Field(targetType = FieldType.STRING)
    @Indexed(unique = true)
    private String currencyCode;
}
