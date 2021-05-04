package com.onlineadplatform.logic.service;

import com.onlineadplatform.logic.dto.CategoryDTO;
import com.onlineadplatform.logic.entities.Category;
import com.onlineadplatform.logic.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class CategoryService extends EntityService<Category, CategoryDTO> {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category addCategory(Category category){
        return categoryRepository.save(category);
    }

    public Category findCategoryByName(String categoryName){
        if (ObjectUtils.isEmpty(categoryName)) {
            return null;
        }
        return categoryRepository.findCategoryByCategoryName(categoryName);
    }
}
