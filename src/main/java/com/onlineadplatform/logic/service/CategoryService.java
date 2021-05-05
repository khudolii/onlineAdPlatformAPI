package com.onlineadplatform.logic.service;

import com.onlineadplatform.logic.dto.CategoryDTO;
import com.onlineadplatform.logic.dto.CurrencyDTO;
import com.onlineadplatform.logic.entities.Category;
import com.onlineadplatform.logic.entities.Currency;
import com.onlineadplatform.logic.facade.CategoryFacade;
import com.onlineadplatform.logic.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryFacade categoryFacade;
    public static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryFacade categoryFacade) {
        this.categoryRepository = categoryRepository;
        this.categoryFacade = categoryFacade;
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category addCategory(CategoryDTO categoryDTO) {
        if (ObjectUtils.isEmpty(categoryDTO)) {
            logger.error("Search objects cannot be null! categoryDTO = " + categoryDTO);
            return null;
        }
        Category category = categoryFacade.getEntity(categoryDTO);
        return addCategory(category);
    }

    public Category findCategoryByName(String categoryName) {
        if (ObjectUtils.isEmpty(categoryName)) {
            logger.error("Search objects cannot be null! categoryName = " + categoryName);
            return null;
        }
        return categoryRepository.findCategoryByCategoryName(categoryName);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
