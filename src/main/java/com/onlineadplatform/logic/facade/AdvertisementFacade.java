package com.onlineadplatform.logic.facade;

import com.onlineadplatform.logic.dto.AdvertisementDTO;
import com.onlineadplatform.logic.dto.AttachmentDTO;
import com.onlineadplatform.logic.dto.CategoryDTO;
import com.onlineadplatform.logic.dto.CurrencyDTO;
import com.onlineadplatform.logic.entities.Advertisement;
import com.onlineadplatform.logic.entities.Attachment;
import com.onlineadplatform.logic.entities.Category;
import com.onlineadplatform.logic.entities.Currency;
import com.onlineadplatform.logic.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class AdvertisementFacade extends EntityService<Advertisement, AdvertisementDTO> {

    private final AttachmentFacade attachmentFacade;
    private final CategoryFacade categoryFacade;
    private final CurrencyFacade currencyFacade;
    private final ACLUserFacade aclUserFacade;

    @Autowired
    public AdvertisementFacade(AttachmentFacade attachmentFacade, CategoryFacade categoryFacade, CurrencyFacade currencyFacade,
                               ACLUserFacade aclUserFacade) {
        this.attachmentFacade = attachmentFacade;
        this.categoryFacade = categoryFacade;
        this.currencyFacade = currencyFacade;
        this.aclUserFacade = aclUserFacade;
    }

    @Override
    public Advertisement getEntity(AdvertisementDTO advertisementDTO) {
        Advertisement advertisement = super.getEntity(advertisementDTO);

        if (!ObjectUtils.isEmpty(advertisementDTO.getCurrency())) {
            Currency currency = currencyFacade.getEntity(advertisementDTO.getCurrency());
            advertisement.setCurrency(currency);
        }

        if (!ObjectUtils.isEmpty(advertisementDTO.getCategory())) {
            Category category = categoryFacade.getEntity(advertisementDTO.getCategory());
            advertisement.setCategory(category);
        }

        if (!ObjectUtils.isEmpty(advertisementDTO.getAttachments())) {
            List<Attachment> attachments = attachmentFacade.getEntitiesList(advertisementDTO.getAttachments());
            advertisement.setAttachments(attachments);
        }
        return advertisement;
    }

    @Override
    public AdvertisementDTO getDTO(Advertisement advertisement) {
        AdvertisementDTO advertisementDTO = super.getDTO(advertisement);

        if (!ObjectUtils.isEmpty(advertisement.getCurrency())) {
            CurrencyDTO currency = currencyFacade.getDTO(advertisement.getCurrency());
            advertisementDTO.setCurrency(currency);
        }

        if (!ObjectUtils.isEmpty(advertisement.getCategory())) {
            CategoryDTO category = categoryFacade.getDTO(advertisement.getCategory());
            advertisementDTO.setCategory(category);
        }

        if (!ObjectUtils.isEmpty(advertisement.getAttachments())) {
            List<AttachmentDTO> attachments = attachmentFacade.getDTOsList(advertisement.getAttachments());
            advertisementDTO.setAttachments(attachments);
        }
        return advertisementDTO;
    }
}
