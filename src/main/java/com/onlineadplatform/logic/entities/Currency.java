package com.onlineadplatform.logic.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Getter
@Setter
public class Currency {
    @MongoId
    private Long currencyId;

    @Field()
    @Indexed(unique = true)
    private String currencyName;

    @Field()
    @Indexed(unique = true)
    private String currencyCode;
}
