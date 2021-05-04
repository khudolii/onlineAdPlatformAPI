package com.onlineadplatform.logic.repository;

import com.onlineadplatform.logic.entities.Advertisement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepository extends MongoRepository<Advertisement, String> {
}
