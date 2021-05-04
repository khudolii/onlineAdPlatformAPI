package com.onlineadplatform.logic.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;


@Document (collection = "UserRole")
@Getter
@Setter
public class UserRole {

    @MongoId
    @Field(targetType = FieldType.OBJECT_ID)
    public String roleId;

    @Field (targetType = FieldType.STRING)
    @Indexed(unique = true)
    public String roleName;
}
