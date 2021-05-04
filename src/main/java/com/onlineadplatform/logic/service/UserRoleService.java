package com.onlineadplatform.logic.service;

import com.onlineadplatform.logic.entities.UserRole;
import com.onlineadplatform.logic.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public UserRole addRole(String roleName) {
        UserRole role = new UserRole();
        role.setRoleName(roleName);
        return userRoleRepository.save(role);
    }

    public UserRole getUserRoleByName(String roleName) {
        return userRoleRepository.findUserRoleByRoleName(roleName);
    }
}
