package com.onlineadplatform.logic.facade;

import com.onlineadplatform.logic.dto.CategoryDTO;
import com.onlineadplatform.logic.entities.Category;
import com.onlineadplatform.logic.service.EntityService;
import org.springframework.stereotype.Component;

@Component

public class CategoryFacade extends EntityService<Category, CategoryDTO> {
}
