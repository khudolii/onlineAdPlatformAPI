package com.onlineadplatform.logic.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;


import java.util.Set;

@Document
@Getter
@Setter
public class UserRole {

    @MongoId
    public Long roleId;

    @Field
    @Indexed(unique = true)
    public String roleName;

    @DBRef
    public Set<ACLUser> users;

}
