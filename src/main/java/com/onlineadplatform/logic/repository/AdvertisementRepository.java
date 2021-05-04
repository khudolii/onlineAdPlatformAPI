package com.onlineadplatform.logic.repository;

import com.onlineadplatform.logic.entities.ACLUser;
import com.onlineadplatform.logic.entities.Advertisement;
import com.onlineadplatform.logic.entities.Category;
import com.onlineadplatform.logic.entities.Currency;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends MongoRepository<Advertisement, String> {
    List<Advertisement> findAllByTitleContains(String title, Pageable pageable);
    List<Advertisement> findAllByAclUser(ACLUser aclUser, Pageable pageable);
    List<Advertisement> findAllByCategory(Category category, Pageable pageable);
    List<Advertisement> findAllByCurrency(Currency currency, Pageable pageable);
}
