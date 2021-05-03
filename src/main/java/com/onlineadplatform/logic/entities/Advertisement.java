package com.onlineadplatform.logic.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.util.Set;

@Document
@Getter
@Setter
public class Advertisement {
    @Id
    private Long advertisementId;

    private String title;
    private String description;
    private Double price;
    @DBRef
    private Currency currency;

    @DBRef
    private Set<Attachment> attachments;

    @DBRef
    private ACLUser aclUser;

    @DBRef
    private Category category;
}
