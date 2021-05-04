package com.onlineadplatform.logic.service;

import com.onlineadplatform.logic.dto.AdvertisementDTO;
import com.onlineadplatform.logic.entities.ACLUser;
import com.onlineadplatform.logic.entities.Advertisement;
import com.onlineadplatform.logic.entities.Category;
import com.onlineadplatform.logic.entities.Currency;
import com.onlineadplatform.logic.exceptions.ADAppException;
import com.onlineadplatform.logic.facade.AdvertisementFacade;
import com.onlineadplatform.logic.repository.AdvertisementRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.List;

@Service
public class AdvertisementService {
    private final AttachmentService attachmentService;
    private final CategoryService categoryService;
    private final CurrencyService currencyService;
    private final ACLUserService aclUserService;
    private final AdvertisementRepository advertisementRepository;

    @Autowired
    public AdvertisementService(AttachmentService attachmentService, CategoryService categoryService,
                                CurrencyService currencyService, ACLUserService aclUserService,
                                AdvertisementRepository advertisementRepository) {
        this.attachmentService = attachmentService;
        this.categoryService = categoryService;
        this.currencyService = currencyService;
        this.aclUserService = aclUserService;
        this.advertisementRepository = advertisementRepository;
    }

    public Advertisement addAdvertisement(AdvertisementDTO advertisementDTO, Principal principal) throws ADAppException {
        Category category = categoryService.findCategoryByName(advertisementDTO.getCategoryName());

        if (ObjectUtils.isEmpty(category)) {
            throw new ADAppException("Category not found!");
        }

        Currency currency = currencyService.getCurrencyByCode(advertisementDTO.getCurrencyCode());
        if (ObjectUtils.isEmpty(currency)) {
            throw new ADAppException("Currency code cannot be empty!");
        }
        ACLUser aclUser = aclUserService.getCurrentUser(principal);
        if (ObjectUtils.isEmpty(aclUser)) {
            throw new ADAppException("User cannot be empty!");
        }

        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(advertisementDTO.getTitle());
        advertisement.setDescription(advertisementDTO.getDescription());
        advertisement.setPrice(advertisementDTO.getPrice());
        advertisement.setCurrency(currency);
        advertisement.setCategory(category);
        advertisement.setAclUser(aclUser);
        return advertisementRepository.save(advertisement);
    }

    public List<Advertisement> getAllAdvertisements(){
        return advertisementRepository.findAll();
    }
}
