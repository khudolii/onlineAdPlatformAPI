package com.onlineadplatform.logic.service;

import com.onlineadplatform.logic.entities.ACLUser;
import com.onlineadplatform.logic.entities.UserRole;
import com.onlineadplatform.logic.repository.ACLUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ACLUserDetailsService implements UserDetailsService {
    private final ACLUserRepository aclUserRepository;

    @Autowired
    public ACLUserDetailsService(ACLUserRepository aclUserRepository) {
        this.aclUserRepository = aclUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        ACLUser user = aclUserRepository.findACLUserByUserName(s);
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("User not found! User name = " + s);
        }
        return build(user);
    }

    public ACLUser findACLUserById(String userId) {
        return aclUserRepository.findACLUserByUserName(userId);
    }

    public static ACLUser build(ACLUser user) {
        Set<UserRole> roles = CollectionUtils.isEmpty(user.getUserRoles()) ? user.getUserRoles() : Collections.emptySet();
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());

        return new ACLUser(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                authorities);
    }
}
