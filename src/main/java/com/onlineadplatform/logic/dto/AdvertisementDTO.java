package com.onlineadplatform.logic.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdvertisementDTO implements Serializable {
    private String title;
    private String description;
    private Double price;

    private String currencyCode;
    private String categoryName;

    private CurrencyDTO currency;
    private List<AttachmentDTO> attachments;
    private CategoryDTO category;
}
