package com.onlineadplatform.logic.controller;

import com.onlineadplatform.logic.AppConstants;
import com.onlineadplatform.logic.exceptions.ADAppException;
import com.onlineadplatform.logic.jwt.JWTTokenProvider;
import com.onlineadplatform.logic.payload.ClientMessageResponse;
import com.onlineadplatform.logic.payload.LoginRequest;
import com.onlineadplatform.logic.payload.RegistrationRequest;
import com.onlineadplatform.logic.payload.ResponseValidator;
import com.onlineadplatform.logic.service.ACLUserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthenticationController {
    private final JWTTokenProvider jwtTokenProvider;
    private final ResponseValidator responseValidator;
    private final AuthenticationManager authenticationManager;
    private final ACLUserService aclUserService;

    @Autowired
    public AuthenticationController(JWTTokenProvider jwtTokenProvider, ResponseValidator responseValidator,
                                    AuthenticationManager authenticationManager, ACLUserService aclUserService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.responseValidator = responseValidator;
        this.authenticationManager = authenticationManager;
        this.aclUserService = aclUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseValidator.validate(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = AppConstants.SECURITY_TOKEN_PREFIX + jwtTokenProvider.generateNewJWTToken(authentication);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", jwt);
        return ResponseEntity.ok(jsonObject.toString());
    }


    @PostMapping("/registration")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseValidator.validate(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        ClientMessageResponse msg = new ClientMessageResponse();
        try {
            aclUserService.addACLUser(registrationRequest);
        } catch (ADAppException e) {
            msg.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(msg);
        }
        msg.setMessage("User registered successfully!");
        return ResponseEntity.ok(msg);
    }
}
