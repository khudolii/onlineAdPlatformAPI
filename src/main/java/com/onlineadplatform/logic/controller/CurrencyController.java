package com.onlineadplatform.logic.controller;

import com.onlineadplatform.logic.dto.CurrencyDTO;
import com.onlineadplatform.logic.entities.Currency;
import com.onlineadplatform.logic.facade.CurrencyFacade;
import com.onlineadplatform.logic.payload.ClientMessageResponse;
import com.onlineadplatform.logic.payload.ResponseValidator;
import com.onlineadplatform.logic.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/currency")
@CrossOrigin
@PreAuthorize("hasRole('ADMIN')")
public class CurrencyController {
    private final CurrencyService currencyService;
    private final CurrencyFacade currencyFacade;
    private final ResponseValidator responseValidator;

    @Autowired
    public CurrencyController(CurrencyService currencyService, CurrencyFacade currencyFacade, ResponseValidator responseValidator) {
        this.currencyService = currencyService;
        this.currencyFacade = currencyFacade;
        this.responseValidator = responseValidator;
    }

    @GetMapping("/getAllCurrency")
    public ResponseEntity<List<CurrencyDTO>> getAllCurrency() {
        List<Currency> currencies = currencyService.getAllCurrencies();
        List<CurrencyDTO> currencyDTOS = currencyFacade.getDTOsList(currencies);
        return new ResponseEntity<>(currencyDTOS, HttpStatus.OK);
    }

    @PostMapping("/addCurrency")
    public ResponseEntity<Object> addCurrency(@Valid @RequestBody CurrencyDTO currencyDTO, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseValidator.validate(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Currency currency = currencyService.addCurrency(currencyDTO);
        ClientMessageResponse msg = new ClientMessageResponse();

        if (ObjectUtils.isEmpty(currency)) {
            msg.setMessage("Currency didn't create!");
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        } else {
            msg.setMessage("Currency was added!");
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
    }
}
