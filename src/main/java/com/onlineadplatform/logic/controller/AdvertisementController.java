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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    public static final Logger logger = LoggerFactory.getLogger(AdvertisementController.class);

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

    /*
    *Adding a new announcement. The following fields are sent in the AdvertisementDTO object:
    * @title - not null and unique
    * @description - not null
    * @price - not null
    * @currencyCodes - not null (e.g. USD, UAH, CAD)
    * @categoryName  - not null
    * */
    @PostMapping("/addAdvertisement")
    public ResponseEntity<Object> addAdvertisement(@Valid @RequestBody AdvertisementDTO advertisementDTO, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseValidator.validate(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        logger.info("AdvertisementDTO object received: " + advertisementDTO);
        Advertisement advertisement = null;
        try {
            advertisement = advertisementService.addAdvertisement(advertisementDTO, principal);
        } catch (ADAppException e) {
            logger.error("addAdvertisement: " + e);
            ClientMessageResponse msg = new ClientMessageResponse(e.getMessage());
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        AdvertisementDTO adDTO = advertisementFacade.getDTO(advertisement);
        return new ResponseEntity<>(adDTO, HttpStatus.OK);
    }

    /*
    * Returns a list of ads based on filter fields:
    * @title - ads are searched for which have this word in their titles
    * @categoryName - filter by category
    * @currencyCode - filter by currency
    * @forCurrentUser - if true - return ads only for current user, else return all ads
    * @page - number of pages
    * @size - nums ads on page
    * */
    @GetMapping("/getAllAdvertisements")
    public ResponseEntity<List<AdvertisementDTO>> getAllAdvertisements(@RequestParam(required = false) String title,
                                                                       @RequestParam(required = false) String categoryName,
                                                                       @RequestParam(required = false) String currencyCode,
                                                                       @RequestParam(required = false, defaultValue = "false") boolean forCurrentUser,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "3") int size,
                                                                       Principal principal) {
        Pageable paging = PageRequest.of(page, size);
        List<Advertisement> advertisements = new ArrayList<>();
        logger.info("Start filtering ads!");

        if (forCurrentUser) {
            logger.info("Filter by currentUser: forCurrentUser = " + forCurrentUser);
            advertisements = advertisementService.getAdvertisementsByAclUser(principal, paging);
        }

        if (!ObjectUtils.isEmpty(title)) {
            logger.info("Filter by title: title = " + title);
            advertisements = advertisements.isEmpty()
                    ? advertisementService.getAdvertisementsByTitle(title, paging)
                    : advertisements.stream().filter(_ad -> _ad.getTitle().contains(title)).collect(Collectors.toList());
        }

        if (!ObjectUtils.isEmpty(categoryName)) {
            logger.info("Filter by category: categoryName = " + categoryName);
            advertisements = advertisements.isEmpty()
                    ? advertisementService.getAdvertisementByCategory(categoryName, paging)
                    : advertisements.stream().filter(_ad -> _ad.getCategory().getCategoryName().contains(categoryName)).collect(Collectors.toList());
        }

        if (!ObjectUtils.isEmpty(currencyCode)) {
            logger.info("Filter by currency: currencyCode = " + currencyCode);
            advertisements = advertisements.isEmpty()
                    ? advertisementService.getAdvertisementByCurrency(currencyCode, paging)
                    : advertisements.stream().filter(_ad -> _ad.getCurrency().getCurrencyCode().contains(currencyCode)).collect(Collectors.toList());
        }

        if (advertisements.isEmpty()
                && ObjectUtils.isEmpty(title)
                && ObjectUtils.isEmpty(categoryName)
                && ObjectUtils.isEmpty(currencyCode)
                && !forCurrentUser) {
            logger.info("Filter not found. Return all ads.");
            advertisements = advertisementService.getAllAdvertisements(paging);
        }

        List<AdvertisementDTO> advertisementDTOS = advertisementFacade.getDTOsList(advertisements);
        return new ResponseEntity<>(advertisementDTOS, HttpStatus.OK);
    }

    /*
    * Adding files to the ad. Accepts files of different formats.
    * Max file size - 200MB
    * */
    @PostMapping("{advertisementId}/addAttachment")
    public ResponseEntity<Object> addAttachment(@PathVariable("advertisementId") String advertisementId,
                                                @Valid @RequestParam("file") MultipartFile file) {
        ClientMessageResponse msg = new ClientMessageResponse();
        Advertisement advertisement = advertisementService.getAdvertisementById(advertisementId);
        if (ObjectUtils.isEmpty(advertisement)) {
            logger.error("Advertisement not found! Id" + advertisementId);
            msg.setMessage("Advertisement not found! With id " + advertisementId);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        Attachment attachment = null;
        try {
            attachment = attachmentService.uploadAttachment(file);
        } catch (ADAppException e) {
            logger.error("addAttachment: " + e);
            msg.setMessage(e.getMessage());
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        if (ObjectUtils.isEmpty(attachment)) {
            logger.error("addAttachment: Attachment not save!");
            msg.setMessage("Attachment not save!");
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(attachment, HttpStatus.OK);
    }

    /*
    * Get list of attachments. AttachmentDTO object includes:
    * @attachmentName
    * @attachmentDownloadUri - url for download file
    * @attachmentType
    * @size
    * */
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


    /*
    * Get attachment file from attachmentDownloadUri
    * */
    @GetMapping("/file/{fileName}")
    public ResponseEntity<Object> getAttachment(@PathVariable String fileName, HttpServletRequest req) {
        try {
            Resource resource = attachmentService.findAttachment(fileName);
            if (ObjectUtils.isEmpty(resource)) {
                logger.error("File not found!");
                throw new ADAppException("File not found!");
            }
            String contentType = req.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (ObjectUtils.isEmpty(contentType)) {
                contentType = "application/octet-stream";
            }
            logger.info("Set content type: " + contentType);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            logger.error("getAttachment: " + e);
            ClientMessageResponse msg = new ClientMessageResponse("Something went wrong! File not found");
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }
}
