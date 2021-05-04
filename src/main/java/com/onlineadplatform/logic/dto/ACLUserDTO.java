package com.onlineadplatform.logic.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ACLUserDTO implements Serializable {
    private String userId;
    private String username;
    private String firstName;
    private String lastName;
}
