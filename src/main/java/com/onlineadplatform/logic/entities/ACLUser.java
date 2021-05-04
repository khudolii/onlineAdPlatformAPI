package com.onlineadplatform.logic.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Document (collection = "ACLUser")
@Getter
@Setter
@NoArgsConstructor
public class ACLUser implements UserDetails {

    @MongoId
    @Field(targetType = FieldType.OBJECT_ID)
    @Indexed(unique = true)
    private String userId;

    @Field
    @Indexed(unique = true)
    private String userName;

    @Field
    private String password;

    @Field
    private String firstName;

    @Field
    private String lastName;

    @DBRef
    private Set<UserRole> userRoles = new HashSet<>();

    private Collection<? extends GrantedAuthority> authorities;

    public ACLUser(String userId, String userName, String password, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
