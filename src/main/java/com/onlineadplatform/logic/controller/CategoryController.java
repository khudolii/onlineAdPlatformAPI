package com.onlineadplatform.logic.controller;

import com.onlineadplatform.logic.dto.CategoryDTO;
import com.onlineadplatform.logic.entities.Category;
import com.onlineadplatform.logic.facade.CategoryFacade;
import com.onlineadplatform.logic.payload.ClientMessageResponse;
import com.onlineadplatform.logic.payload.ResponseValidator;
import com.onlineadplatform.logic.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
/*ONLY ADMIN CAN ADD CATEGORY*/
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryFacade categoryFacade;
    private final ResponseValidator responseValidator;
    public static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryFacade categoryFacade, ResponseValidator responseValidator) {
        this.categoryService = categoryService;
        this.categoryFacade = categoryFacade;
        this.responseValidator = responseValidator;
    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOS = categoryFacade.getDTOsList(categories);
        return new ResponseEntity<>(categoryDTOS, HttpStatus.OK);
    }

    /*
    * Add new category of ads. Only admin can add new category. For create category need CategoryDTO object with field:
    * @categoryName
    * */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addCategory")
    public ResponseEntity<Object> addCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseValidator.validate(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Category category = categoryService.addCategory(categoryDTO);
        ClientMessageResponse msg = new ClientMessageResponse();

        if (ObjectUtils.isEmpty(category)) {
            logger.error("Category didn't create! " + categoryDTO);
            msg.setMessage("Category didn't create!");
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        } else {
            msg.setMessage("Category was added!");
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
    }
}
