package com.onlineadplatform.logic.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CurrencyDTO implements Serializable {
    @NotNull(message = "Currency Code is null")
    private String currencyCode;

    @NotNull (message = "Currency Name is null")
    private String currencyName;
}
