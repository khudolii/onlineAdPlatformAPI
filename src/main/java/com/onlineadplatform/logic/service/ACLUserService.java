package com.onlineadplatform.logic.service;

import com.onlineadplatform.logic.AppConstants;
import com.onlineadplatform.logic.dto.ACLUserDTO;
import com.onlineadplatform.logic.entities.ACLUser;
import com.onlineadplatform.logic.entities.UserRole;
import com.onlineadplatform.logic.exceptions.ADAppException;
import com.onlineadplatform.logic.exceptions.UserExistException;
import com.onlineadplatform.logic.payload.RegistrationRequest;
import com.onlineadplatform.logic.repository.ACLUserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Principal;

@Service
public class ACLUserService {

    private static final Class<ACLUserService> CLAZZ = ACLUserService.class;
    private static final Logger logger = Logger.getLogger(CLAZZ);

    private final ACLUserRepository aclUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRoleService userRoleService;

    @Autowired
    public ACLUserService(ACLUserRepository aclUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserRoleService userRoleService) {
        this.aclUserRepository = aclUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRoleService = userRoleService;
    }

    public ACLUser addACLUser (RegistrationRequest registrationReq) throws ADAppException {
        if (!registrationReq.getPassword().equals(registrationReq.getConfirmPassword())) {
            throw new ADAppException("Passwords not equals!");
        }
        ACLUser aclUser = new ACLUser();
        aclUser.setUserName(registrationReq.getUsername());
        aclUser.setFirstName(registrationReq.getFirstname());
        aclUser.setLastName(registrationReq.getLastname());
        aclUser.setPassword(bCryptPasswordEncoder.encode(registrationReq.getPassword()));
        UserRole userRole = userRoleService.getUserRoleByName(AppConstants.USER_ROLE);
        if (ObjectUtils.isEmpty(userRole)) {
            userRole = userRoleService.addRole(AppConstants.USER_ROLE);
        }
        aclUser.getUserRoles().add(userRole);

        try {
            return aclUserRepository.save(aclUser);
        } catch (Exception e) {
            throw new UserExistException("The user " + aclUser.getUsername() + " already exist.");
        }
    }

    private ACLUser getUserByPrincipal(Principal principal) throws UsernameNotFoundException{
        String username = principal.getName();
        ACLUser user = aclUserRepository.findACLUserByUserName(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("Username not found with username " + username);
        }
        return user;
    }

    public ACLUser updateACLUser (ACLUserDTO aclUserDTO, Principal principal) {
        ACLUser user = getUserByPrincipal(principal);
        user.setFirstName(aclUserDTO.getFirstName());
        user.setLastName(aclUserDTO.getLastName());
        return aclUserRepository.save(user);
    }

    public ACLUser getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

}
