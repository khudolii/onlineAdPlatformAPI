package com.onlineadplatform.logic.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CategoryDTO implements Serializable {
    @NotNull(message = "Category name is null")
    private String categoryName;
}
