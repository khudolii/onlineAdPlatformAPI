package com.onlineadplatform.logic.repository;

import com.onlineadplatform.logic.entities.Attachment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AttachmentRepository extends MongoRepository <Attachment, Long> {
    List<Attachment> findAll();
}
