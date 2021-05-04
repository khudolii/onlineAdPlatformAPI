package com.onlineadplatform.logic.repository;

import com.onlineadplatform.logic.entities.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findCategoryByCategoryName (String categoryName);
}
