package com.onlineadplatform.logic.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CurrencyDTO implements Serializable {
    @NotNull
    private String currencyCode;

    @NotNull
    private String currencyName;
}
