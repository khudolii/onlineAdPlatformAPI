package com.onlineadplatform.logic.controller;

import com.onlineadplatform.logic.dto.AdvertisementDTO;
import com.onlineadplatform.logic.entities.Advertisement;
import com.onlineadplatform.logic.exceptions.ADAppException;
import com.onlineadplatform.logic.facade.AdvertisementFacade;
import com.onlineadplatform.logic.payload.ClientMessageResponse;
import com.onlineadplatform.logic.payload.ResponseValidator;
import com.onlineadplatform.logic.service.AdvertisementService;
import com.onlineadplatform.logic.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/ads")
@CrossOrigin
public class AdvertisementController {
    private final AdvertisementService advertisementService;
    private final AdvertisementFacade advertisementFacade;
    private final ResponseValidator responseValidator;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService, AdvertisementFacade advertisementFacade,
                                   ResponseValidator responseValidator) {
        this.advertisementService = advertisementService;
        this.advertisementFacade = advertisementFacade;
        this.responseValidator = responseValidator;
    }

    @PostMapping("/addAdvertisement")
    public ResponseEntity<Object> addAdvertisement (@Valid @RequestBody AdvertisementDTO advertisementDTO, BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors = responseValidator.validate(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Advertisement advertisement = null;
        try {
            advertisement = advertisementService.addAdvertisement(advertisementDTO, principal);
        } catch (ADAppException e) {
            ClientMessageResponse msg = new ClientMessageResponse(e.getMessage());
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        AdvertisementDTO adDTO = advertisementFacade.getDTO(advertisement);
        return new ResponseEntity<>(adDTO, HttpStatus.OK);
    }

    @GetMapping("/getAllAdvertisements")
    public ResponseEntity<List<AdvertisementDTO>> getAllAdvertisements(){
        List<Advertisement> advertisements = advertisementService.getAllAdvertisements();
        List<AdvertisementDTO> advertisementDTOS = advertisementFacade.getDTOsList(advertisements);
        return new ResponseEntity<>(advertisementDTOS, HttpStatus.OK);
    }
}
