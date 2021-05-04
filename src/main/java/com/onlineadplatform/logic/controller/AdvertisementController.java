package com.onlineadplatform.logic.controller;

import com.onlineadplatform.logic.dto.AdvertisementDTO;
import com.onlineadplatform.logic.dto.AttachmentDTO;
import com.onlineadplatform.logic.entities.Advertisement;
import com.onlineadplatform.logic.entities.Attachment;
import com.onlineadplatform.logic.exceptions.ADAppException;
import com.onlineadplatform.logic.facade.AdvertisementFacade;
import com.onlineadplatform.logic.facade.AttachmentFacade;
import com.onlineadplatform.logic.payload.ClientMessageResponse;
import com.onlineadplatform.logic.payload.ResponseValidator;
import com.onlineadplatform.logic.service.AdvertisementService;
import com.onlineadplatform.logic.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/ads")
@CrossOrigin
public class AdvertisementController {
    private final AdvertisementService advertisementService;
    private final AdvertisementFacade advertisementFacade;
    private final ResponseValidator responseValidator;
    private final AttachmentService attachmentService;
    private final AttachmentFacade attachmentFacade;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService, AdvertisementFacade advertisementFacade,
                                   ResponseValidator responseValidator, AttachmentService attachmentService,
                                   AttachmentFacade attachmentFacade) {
        this.advertisementService = advertisementService;
        this.advertisementFacade = advertisementFacade;
        this.responseValidator = responseValidator;
        this.attachmentService = attachmentService;
        this.attachmentFacade = attachmentFacade;
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
    public ResponseEntity<List<AdvertisementDTO>> getAllAdvertisements(@RequestParam(required = false) String title,
                                                                       @RequestParam(required = false) String categoryName,
                                                                       @RequestParam(required = false) String currencyCode,
                                                                       @RequestParam(required = false, defaultValue = "false") boolean forCurrentUser,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "3") int size,
                                                                       Principal principal){
        Pageable paging = PageRequest.of(page, size);
        List<Advertisement> advertisements = new ArrayList<>();

        if(forCurrentUser) {
            advertisements = advertisementService.getAdvertisementsByAclUser(principal, paging);
        }

        if (!ObjectUtils.isEmpty(title)) {
            advertisements = advertisements.isEmpty()
                    ? advertisementService.getAdvertisementsByTitle(title, paging)
                    : advertisements.stream().filter(_ad -> _ad.getTitle().contains(title)).collect(Collectors.toList());
        }

        if(!ObjectUtils.isEmpty(categoryName)) {
            advertisements = advertisements.isEmpty()
                    ? advertisementService.getAdvertisementByCategory(categoryName, paging)
                    : advertisements.stream().filter(_ad -> _ad.getCategory().getCategoryName().contains(categoryName)).collect(Collectors.toList());
        }

        if(!ObjectUtils.isEmpty(currencyCode)) {
            advertisements = advertisements.isEmpty()
                    ? advertisementService.getAdvertisementByCurrency(currencyCode, paging)
                    : advertisements.stream().filter(_ad -> _ad.getCurrency().getCurrencyCode().contains(currencyCode)).collect(Collectors.toList());
        }

        if (advertisements.isEmpty()) {
            advertisements = advertisementService.getAllAdvertisements(paging);
        }

        List<AdvertisementDTO> advertisementDTOS = advertisementFacade.getDTOsList(advertisements);
        return new ResponseEntity<>(advertisementDTOS, HttpStatus.OK);
    }

    @PostMapping("{advertisementId}/addAttachment")
    public ResponseEntity<Object> addAttachment(@PathVariable("advertisementId") String advertisementId,
                                                @RequestParam("file") MultipartFile file,
                                                BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseValidator.validate(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        ClientMessageResponse msg = new ClientMessageResponse();
        Advertisement advertisement = advertisementService.getAdvertisementById(advertisementId);
        if (!ObjectUtils.isEmpty(advertisement)) {
            msg.setMessage("Advertisement not found! With id " + advertisementId);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        Attachment attachment = attachmentService.uploadAttachment(file);
        if (ObjectUtils.isEmpty(attachment)) {
            msg.setMessage("Attachment not save!");
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(attachment, HttpStatus.OK);
    }

    @GetMapping("/{advertisementId}/getAttachments")
    public ResponseEntity<List<AttachmentDTO>> getAttachmentsForAdvertisement(@PathVariable String advertisementId) {
        Advertisement advertisement = advertisementService.getAdvertisementById(advertisementId);
        if (ObjectUtils.isEmpty(advertisement)) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
        }

        List<Attachment> attachments = advertisement.getAttachments();
        List<AttachmentDTO> attachmentDTOS = attachmentFacade.getDTOsList(attachments);
        return new ResponseEntity<>(attachmentDTOS, HttpStatus.OK);
    }
}
