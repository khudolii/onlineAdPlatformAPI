package com.onlineadplatform.logic.repository;

import com.onlineadplatform.logic.entities.ACLUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ACLUserRepository extends MongoRepository<ACLUser, String> {
    ACLUser findACLUserByUserName(String userName);
}
