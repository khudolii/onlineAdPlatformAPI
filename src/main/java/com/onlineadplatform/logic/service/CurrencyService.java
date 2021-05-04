package com.onlineadplatform.logic.service;

import com.onlineadplatform.logic.dto.CurrencyDTO;
import com.onlineadplatform.logic.entities.Currency;
import com.onlineadplatform.logic.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


@Service
public class CurrencyService extends EntityService<Currency, CurrencyDTO> {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public Currency addCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }

    public Currency getCurrencyByCode (String currencyCode){
        if(ObjectUtils.isEmpty(currencyCode)) {
            return null;
        }
        return currencyRepository.findCurrencyByCurrencyCode(currencyCode);
    }
}
