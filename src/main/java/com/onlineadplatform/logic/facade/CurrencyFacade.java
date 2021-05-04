package com.onlineadplatform.logic.facade;

import com.onlineadplatform.logic.dto.CurrencyDTO;
import com.onlineadplatform.logic.entities.Currency;
import com.onlineadplatform.logic.service.EntityService;
import org.springframework.stereotype.Component;

@Component

public class CurrencyFacade extends EntityService<Currency, CurrencyDTO> {
}
