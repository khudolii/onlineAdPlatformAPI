package com.onlineadplatform.logic.repository;

import com.onlineadplatform.logic.entities.Attachment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends MongoRepository <Attachment, Long> {
    List<Attachment> findAll();
}
