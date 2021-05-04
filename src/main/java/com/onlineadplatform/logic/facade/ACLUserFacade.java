package com.onlineadplatform.logic.facade;

import com.onlineadplatform.logic.dto.ACLUserDTO;
import com.onlineadplatform.logic.entities.ACLUser;
import com.onlineadplatform.logic.service.EntityService;
import org.springframework.stereotype.Component;

@Component
public class ACLUserFacade extends EntityService<ACLUser, ACLUserDTO> {
}
