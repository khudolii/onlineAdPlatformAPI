package com.onlineadplatform.logic.service;

import com.onlineadplatform.logic.dto.AdvertisementDTO;
import com.onlineadplatform.logic.entities.ACLUser;
import com.onlineadplatform.logic.entities.Advertisement;
import com.onlineadplatform.logic.entities.Category;
import com.onlineadplatform.logic.entities.Currency;
import com.onlineadplatform.logic.exceptions.ADAppException;
import com.onlineadplatform.logic.repository.AdvertisementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdvertisementService {

    private final CategoryService categoryService;
    private final CurrencyService currencyService;
    private final ACLUserService aclUserService;
    private final AdvertisementRepository advertisementRepository;
    public static final Logger logger = LoggerFactory.getLogger(AdvertisementService.class);

    @Autowired
    public AdvertisementService(CategoryService categoryService,
                                CurrencyService currencyService, ACLUserService aclUserService,
                                AdvertisementRepository advertisementRepository) {
        this.categoryService = categoryService;
        this.currencyService = currencyService;
        this.aclUserService = aclUserService;
        this.advertisementRepository = advertisementRepository;
    }

    public Advertisement getAdvertisementById(String advertisingId) {
        return advertisementRepository.findById(advertisingId).orElse(null);
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

    public List<Advertisement> getAllAdvertisements() {
        return advertisementRepository.findAll();
    }

    public List<Advertisement> getAllAdvertisements(Pageable pageable) {
        Page<Advertisement> pages = advertisementRepository.findAll(pageable);
        return pages.getContent();
    }

    public List<Advertisement> getAdvertisementsByTitle(String title, Pageable pageable) {
        if (ObjectUtils.isEmpty(title) || ObjectUtils.isEmpty(pageable)) {
            logger.error("Search objects cannot be null! Title = " + title + "; pageable = " + pageable);
            return new ArrayList<>();
        }
        return advertisementRepository.findAllByTitleContains(title, pageable);
    }

    public List<Advertisement> getAdvertisementsByAclUser(Principal principal, Pageable pageable) {
        if (ObjectUtils.isEmpty(principal)) {
            logger.error("Search objects cannot be null! Principal = " + principal + "; pageable = " + pageable);
            return new ArrayList<>();
        }
        ACLUser aclUser = aclUserService.getCurrentUser(principal);
        return getAdvertisementsByAclUser(aclUser, pageable);
    }

    public List<Advertisement> getAdvertisementsByAclUser(ACLUser aclUser, Pageable pageable) {
        if (ObjectUtils.isEmpty(aclUser) || ObjectUtils.isEmpty(pageable)) {
            logger.error("Search objects cannot be null! ACLUser = " + aclUser + "; pageable = " + pageable);
            return new ArrayList<>();
        }
        return advertisementRepository.findAllByAclUser(aclUser, pageable);
    }

    public List<Advertisement> getAdvertisementByCategory(String categoryName, Pageable pageable) {
        if (ObjectUtils.isEmpty(categoryName)) {
            logger.error("Search objects cannot be null! Category = " + categoryName + "; pageable = " + pageable);
            return new ArrayList<>();
        }
        Category category = categoryService.findCategoryByName(categoryName);
        return getAdvertisementByCategory(category, pageable);
    }

    public List<Advertisement> getAdvertisementByCategory(Category category, Pageable pageable) {
        if (ObjectUtils.isEmpty(category) || ObjectUtils.isEmpty(pageable)) {
            logger.error("Search objects cannot be null! Category = " + category + "; pageable = " + pageable);
            return new ArrayList<>();
        }
        return advertisementRepository.findAllByCategory(category, pageable);
    }

    public List<Advertisement> getAdvertisementByCurrency(String currencyCode, Pageable pageable) {
        if (ObjectUtils.isEmpty(currencyCode)) {
            logger.error("Search objects cannot be null! CurrencyCode = " + currencyCode + "; pageable = " + pageable);
            return new ArrayList<>();
        }
        Currency currency = currencyService.getCurrencyByCode(currencyCode);
        return getAdvertisementByCurrency(currency, pageable);
    }

    public List<Advertisement> getAdvertisementByCurrency(Currency currency, Pageable pageable) {
        if (ObjectUtils.isEmpty(currency) || ObjectUtils.isEmpty(pageable)) {
            logger.error("Search objects cannot be null! Currency = " + currency + "; pageable = " + pageable);
            return new ArrayList<>();
        }
        return advertisementRepository.findAllByCurrency(currency, pageable);
    }
}
