package com.onlineadplatform.logic.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;

@Document (collection = "Attachments")
@Setter
@Getter
@ToString
public class Attachment {

    @Id
    private Long id;

    @Field(targetType = FieldType.STRING)
    private String attachmentName;

    @Field(targetType = FieldType.STRING)
    private String attachmentDownloadUri;

    @Field(targetType = FieldType.STRING)
    private String attachmentType;

    @Field(targetType = FieldType.INT64)
    private Long size;
}
