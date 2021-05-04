package com.onlineadplatform.logic.controller;

import com.onlineadplatform.logic.dto.ACLUserDTO;
import com.onlineadplatform.logic.entities.ACLUser;
import com.onlineadplatform.logic.payload.ResponseValidator;
import com.onlineadplatform.logic.service.ACLUserService;
import com.onlineadplatform.logic.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class ACLUserController extends EntityService<ACLUser, ACLUserDTO> {

    private final ACLUserService aclUserService;
    private final ResponseValidator responseValidator;

    @Autowired
    public ACLUserController(ACLUserService aclUserService, ResponseValidator responseValidator) {
        this.aclUserService = aclUserService;
        this.responseValidator = responseValidator;
    }

    @GetMapping("/getCurrentUser")
    public ResponseEntity<ACLUserDTO> getCurrentUser(Principal principal) {
        ACLUser user = aclUserService.getCurrentUser(principal);
        ACLUserDTO userDTO = getDTO(user);
        if (ObjectUtils.isEmpty(userDTO)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/updateUser")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody ACLUserDTO userDTO, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseValidator.validate(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        ACLUser user = aclUserService.updateACLUser(userDTO, principal);
        ACLUserDTO aclUserDTO = getDTO(user);
        if (ObjectUtils.isEmpty(userDTO)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(aclUserDTO, HttpStatus.OK);
    }
}
