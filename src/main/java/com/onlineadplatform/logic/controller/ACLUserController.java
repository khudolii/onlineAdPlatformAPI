package com.onlineadplatform.logic.controller;

import com.onlineadplatform.logic.dto.ACLUserDTO;
import com.onlineadplatform.logic.entities.ACLUser;
import com.onlineadplatform.logic.facade.ACLUserFacade;
import com.onlineadplatform.logic.payload.ClientMessageResponse;
import com.onlineadplatform.logic.payload.ResponseValidator;
import com.onlineadplatform.logic.service.ACLUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class ACLUserController {

    private final ACLUserService aclUserService;
    private final ResponseValidator responseValidator;
    private final ACLUserFacade aclUserFacade;
    public static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    public ACLUserController(ACLUserService aclUserService, ResponseValidator responseValidator, ACLUserFacade aclUserFacade) {
        this.aclUserService = aclUserService;
        this.responseValidator = responseValidator;
        this.aclUserFacade = aclUserFacade;
    }

    /*Get user profile for current user in session*/
    @GetMapping("/getCurrentUser")
    public ResponseEntity<ACLUserDTO> getCurrentUser(Principal principal) {
        ACLUser user = aclUserService.getCurrentUser(principal);
        ACLUserDTO userDTO = aclUserFacade.getDTO(user);
        if (ObjectUtils.isEmpty(userDTO)) {
            logger.error("User not found!");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    /*
    * All users can update their profile! Users cant update:
    * @firstName
    * @lastName
    * */
    @PostMapping("/updateUser")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody ACLUserDTO userDTO, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseValidator.validate(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        ACLUser user = aclUserService.updateACLUser(userDTO, principal);
        ACLUserDTO aclUserDTO = aclUserFacade.getDTO(user);
        if (ObjectUtils.isEmpty(userDTO)) {
            logger.error("User didn't update!" + user);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(aclUserDTO, HttpStatus.OK);
    }

    /*
    * Only admin can delete user! But admin cannot delete himself.
    * */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("{userId}/delete")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") String userId, Principal principal) {
        ClientMessageResponse msg = new ClientMessageResponse();
        try {
            ACLUser currentUser = aclUserService.getCurrentUser(principal);
            if (currentUser.getUserId().equals(userId)) {
                msg.setMessage("User cannot delete himself!");
                logger.info(msg.getMessage());
                return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
            }
            ACLUser user = aclUserService.getUserByUserId(userId);
            boolean isDeleted = aclUserService.deleteACLUser(user);
            if(isDeleted){
                msg.setMessage("User was deleted!");
                return new ResponseEntity<>(msg, HttpStatus.OK);
            } else {
                msg.setMessage("Something went wrong. User not deleted!");
                return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("deleteUser: " + e);
            msg.setMessage("Something went wrong. User not deleted!");
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
    }
}
