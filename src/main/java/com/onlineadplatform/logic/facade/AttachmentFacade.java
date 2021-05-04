package com.onlineadplatform.logic.facade;

import com.onlineadplatform.logic.dto.AttachmentDTO;
import com.onlineadplatform.logic.entities.Attachment;
import com.onlineadplatform.logic.service.EntityService;
import org.springframework.stereotype.Component;

@Component
public class AttachmentFacade extends EntityService<Attachment, AttachmentDTO> {
}
