package com.onlineadplatform.logic.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CurrencyDTO implements Serializable {
    private String currencyCode;
    private String currencyName;
}
