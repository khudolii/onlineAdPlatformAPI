package com.onlineadplatform.logic.repository;

import com.onlineadplatform.logic.entities.Currency;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CurrencyRepository extends MongoRepository<Currency, String> {
    Currency findCurrencyByCurrencyCode (String currencyCode);
}
