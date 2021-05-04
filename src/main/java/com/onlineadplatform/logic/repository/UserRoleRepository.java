package com.onlineadplatform.logic.repository;

import com.onlineadplatform.logic.entities.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRoleRepository extends MongoRepository<UserRole, String> {
    UserRole findUserRoleByRoleName(String roleName);
}
