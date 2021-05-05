package com.onlineadplatform.logic.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdvertisementDTO implements Serializable {
    private String advertisementId;
    @NotEmpty(message = "Title is empty!")
    private String title;
    @NotNull(message = "Description is empty!")
    private String description;
    @NotNull(message = "Price is empty!")
    private Double price;

    @NotNull(message = "CurrencyCode is empty!")
    private String currencyCode;
    @NotNull(message = "CategoryName is empty!")
    private String categoryName;

    private CurrencyDTO currency;
    private List<AttachmentDTO> attachments;
    private CategoryDTO category;
}
