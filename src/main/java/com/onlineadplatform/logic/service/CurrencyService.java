package com.onlineadplatform.logic.service;

import com.onlineadplatform.logic.dto.CurrencyDTO;
import com.onlineadplatform.logic.entities.Currency;
import com.onlineadplatform.logic.facade.CurrencyFacade;
import com.onlineadplatform.logic.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;


@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyFacade currencyFacade;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository, CurrencyFacade currencyFacade) {
        this.currencyRepository = currencyRepository;
        this.currencyFacade = currencyFacade;
    }

    public Currency addCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }

    public Currency addCurrency(CurrencyDTO currencyDTO) {
        if (ObjectUtils.isEmpty(currencyDTO)) {
            return null;
        }
        Currency currency = currencyFacade.getEntity(currencyDTO);
        return addCurrency(currency);
    }

    public Currency getCurrencyByCode(String currencyCode) {
        if (ObjectUtils.isEmpty(currencyCode)) {
            return null;
        }
        return currencyRepository.findCurrencyByCurrencyCode(currencyCode);
    }

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }
}
